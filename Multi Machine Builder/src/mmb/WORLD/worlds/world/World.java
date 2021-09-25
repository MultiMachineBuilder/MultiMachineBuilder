/**
 * 
 */
package mmb.WORLD.worlds.world;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import io.vavr.Tuple2;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import mmb.Bitwise;
import mmb.Vector2iconst;
import mmb.BEANS.BlockActivateListener;
import mmb.DATA.json.JsonTool;
import mmb.RUNTIME.TaskLoop;
import mmb.WORLD.block.BlockEntity;
import mmb.WORLD.gui.FPSCounter;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockLoader;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.machine.Machine;
import mmb.WORLD.machine.MachineModel;
import mmb.WORLD.player.Player;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.DataLayers;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.universe.Universe;
import mmb.debug.Debugger;
import monniasza.collects.Identifiable;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 * <br>
 * A {@code BlockMap} is a representation of ingame 2D voxel map.
 * <br> To load the map, create and the use the {@code saveLoad} module
 * <br> ,its {@code getLoaded()} method for getting loaded {@code BlockMap}
 * <br> ,its {@code load()} method to load data
 * <br> and at first {@code setName()} to set target name
 */
public class World implements Identifiable<String>{
	//Debugging
	protected Debugger debug = new Debugger("MAP anonymous");
	
	//Naming
	private String name;
	/**
	 * @return this world's name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name new name
	 */
	public void setName(@Nullable String name) {
		this.name = name;
		if(name == null) {
			debug.printl("Became anonymous");
			debug.id = "MAP anonymous";
		}else{
			debug.printl("Renamed to: "+name);
			debug.id = "MAP - " + name;
		}
	}
	@Override
	public String id() {
		return name;
	}
	
	//Outer universe
	private Universe owner;
	/** @param owner new outer universe */
	public void setOwner(Universe owner) {
		this.owner = owner;
	}
	/** @return the associated universe */
	public Universe getUniverse() {
		return owner;
	}
	
	//Constructors
	/**
	 * Creates a world with given entries and specific starting position
	 * @param entries world data
	 * @param startX starting X
	 * @param startY starting Y
	 */
	public World(BlockEntry[][] entries, int startX, int startY) {
		this.entries = new FixedGrid<>(entries);
		this.startX = startX;
		this.startY = startY;
		sizeX = entries.length;
		sizeY = entries[0].length;
		endX = startX + sizeX;
		endY = startY + sizeY;
	}
	/**
	 * Create an empty world with given bounds
	 * @param sizeX horizontal size
	 * @param sizeY vertical size
	 * @param startX starting X
	 * @param startY starting Y
	 */
	public World(int sizeX, int sizeY, int startX, int startY) {
		entries = new FixedGrid<>(sizeX, sizeY);
		entries.fill(ContentsBlocks.grass);
		this.startX = startX;
		this.startY = startY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		endX = startX + sizeX;
		endY = startY + sizeY;
	}
	
	//Serialization
	/**
	 * Loads a world from JSON
	 * @param json JSON data to load
	 * @return a world from JSON file
	 * @throws NullPointerException if JSON data is null
	 */
	public static World load(JsonNode json) {
		Objects.requireNonNull(json, "World is loaded with null"); //unlikely to occur
		//Dimensions
		int sizeX = json.get("sizeX").asInt();
		int sizeY = json.get("sizeY").asInt();
		int startX = json.get("startX").asInt();
		int startY = json.get("startY").asInt();
		int endX = sizeX + startX;
		int endY = sizeY + startY;
		
		//Prepare the map
		World world = new World(sizeX, sizeY, startX, startY);
		
		//Blocks
		ArrayNode worldArray = (ArrayNode) json.get("world");
		BlockLoader bloader = new BlockLoader();
		bloader.map = world;
		Iterator<JsonNode> iter = worldArray.elements();
		for(int y = startY; y < endY; y++) {
			for(int x = startX; x < endX; x++) {
				JsonNode node = iter.next();
				bloader.x = x;
				bloader.y = y;
				BlockEntry block = bloader.load(node);
				world.set(block, x, y);
			}
		}
		
		//Dropped items
		ArrayNode drops = JsonTool.requestArray("drops", (ObjectNode) json);
		for(JsonNode drop: drops) {
			int x = drop.get(0).asInt();
			int y = drop.get(1).asInt();
			ItemEntry item = ItemEntry.loadFromJson(drop.get(2));
			if(item == null) continue;
			world.dropItem(item, x, y);
		}
		
		//Machines
		ArrayNode machines = (ArrayNode) json.get("machines");
		for(JsonNode machineNode: machines) {
			ArrayNode aMachine = (ArrayNode) machineNode;
			String type = aMachine.get(0).asText();
			int x = aMachine.get(1).asInt();
			int y = aMachine.get(2).asInt();
			JsonNode machineData  = aMachine.get(3);
			try {
				Machine machine = MachineModel.forID(type).initialize(x, y, machineData);
				world.placeMachine(machine);
			} catch(Exception e) {
				world.debug.pstm(e, "Failed to place a machine of type "+type+" at ["+x+","+y+"]");
			}
		}
		
		//Data layers
		ObjectNode dataNode = (ObjectNode) json.get("data");
		world.data.addAll(DataLayers.createAllMapDataLayers());
		for(WorldDataLayer dl: world.data) {
			JsonNode datalayerData = dataNode.get(dl.id());
			try {
				if(!datalayerData.isMissingNode()) dl.load(datalayerData);
				dl.afterMapLoaded(world);
			} catch (Exception e) {
				world.debug.pstm(e, "Failed to load data layer "+dl.title());
			}
		}
		
		WorldEvents.load.trigger(new Tuple2<World, ObjectNode>(world, (ObjectNode) json));
		
		//Player data
		world.player.load(JsonTool.requestObject("player", (ObjectNode) json));
		
		//After loading process
		WorldEvents.afterLoad.trigger(world);
		
		return world;
	}
	/**
	 * Saves a given world
	 * @param world world to save
	 * @return a JSON representation of a world
	 */
	public static JsonNode save(World world) {
			//Master node
			ObjectNode master = JsonTool.newObjectNode();
			
			//Dimensions
			int sX = world.sizeX;
			int sY = world.sizeY;
			int pX = world.startX;
			int pY = world.startY;
			int eX = sX + pX;
			int eY = sY + pY;
			master.put("sizeX", sX);
			master.put("sizeY", sY);
			master.put("startX", pX);
			master.put("startY", pY);
			
			//Blocks
			ArrayNode blockArrayNode = JsonTool.newArrayNode();
			for(int y = pY; y < eY; y++) {
				for(int x = pX; x < eX; x++) {
					BlockEntry ent = world.get(x, y);
					blockArrayNode.add(ent.save());
				}
			}
			master.set("world", blockArrayNode);
			
			//Dropped items
			ArrayNode dropsNode = JsonTool.newArrayNode();
			for(Entry<Vector2iconst, ItemEntry> entry: world.drops.entries()) {
				if(entry.getValue() == null) continue;
				int x = entry.getKey().x;
				int y = entry.getKey().y;
				ItemEntry item = entry.getValue();
				JsonNode itemSaved = ItemEntry.saveItem(item);
				ArrayNode array = JsonTool.newArrayNode();
				array.add(x).add(y).add(itemSaved);
				dropsNode.add(array);
			}
			master.set("drops", dropsNode);
			
			//Machines
			ArrayNode machineArrayNode = JsonTool.newArrayNode();
			for(Machine m: world._machines) {
				ArrayNode array = JsonTool.newArrayNode();
				//format: [id, x, y, data]
				array.add(m.id());
				array.add(m.posX());
				array.add(m.posY());
				array.add(m.save());
				machineArrayNode.add(array);
			}
			master.set("machines", machineArrayNode);
			
			//Data layers
			ObjectNode dataNode = JsonTool.newObjectNode();
			for(WorldDataLayer mdl: world.data) {
				dataNode.set(mdl.id(), mdl.save());
			}
			master.set("data", dataNode);
			
			//Player
			master.set("player", world.player.save());
			
			WorldEvents.save.trigger(new Tuple2<World, ObjectNode>(world, master));
			return master;
		}
	
	//Activity
	private final static long PERIOD = 20_000_000; //nanoseconds
	private TaskLoop timer = new TaskLoop(() -> update(), PERIOD);
	/** The Ticks Per Second counter */
	public final FPSCounter tps = new FPSCounter();
	@SuppressWarnings("null")
	private void update() {//Run the map
		if(entries == null) return;
		if(runLock != null) {
			Thread.currentThread().interrupt();
			synchronized(runLock){
				runLock.notifyAll();
			}
			return;
		}
		tps.count();
		//Set up the proxy
		try(MapProxy proxy = createProxy()){
			//Run every machine
			for(Machine m: _machines) {
				try {
					m.onUpdate(proxy);
				}catch(Exception e) {
					debug.pstm(e, "Failed to run a machine");
				}
			}
			//Run every block entity
			for(BlockEntity ent: _blockents) {
				try {
					long start = System.nanoTime();
					ent.onTick(proxy);
					long finish = System.nanoTime();
					long duration = finish - start;
					if(duration >= 1_000_000) {
						debug.printl("Block entity at ["+ent.posX()+","+ent.posY()+
							"] took exceptionally long to run");
					}
				}catch(Exception e){
					debug.pstm(e, "Failed to run block entity at ["
						+ent.posX()+","+ent.posY()+"]");
				}
			}
		}catch(Exception e) {
			debug.pstm(e, "Failed to run the map");
		}
	}
	/** @return is this world running? */
	public boolean isRunning() {
		return timer.getState() == 1;
	}
	/**
	 * Starts the timer.
	 * @throws IllegalStateException when timer is running or destroyed.
	 */
	public void startTimer() {
		timer.start();
	}
	/** Destroy all map resources */
	public void destroy() {
		debug.printl("Destroying");
		shutdown();
	}
	/** @return has this map shut down? */
	public boolean hasShutDown() {
		return timer.getState() == 2;
	}
	/** Shut down the map, but keep the resources */
	public void shutdown() {
		debug.printl("Shutdown");
		if(hasShutDown()) return;
		//Stop the game loop
		preventRuns();
		//Shut down block entities
		for(BlockEntity ent: _blockents) {
			try {
				ent.onShutdown(this);
			} catch (Exception e) {
				debug.pstm(e, "Failed to shut down block "+ent.type().id()+" at ["+ent.posX()+","+ent.posY()+"]");
			}
		}
		//Shut down machines
		for(Machine m: machines) {
			try {
				m.onShutdown();
			}catch(Exception e) {
				debug.pstm(e, "Failed to shut down machine "+m.id()+" at ["+m.posX()+","+m.posY()+"]");
			}
		}
		if(isRunning()) timer.destroy();
	}
	
	//Lock out the world
	private Object runLock;
	private void preventRuns() {
		runLock = new Object();
		synchronized(runLock) {
			try {
				runLock.wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	//Data layers
	public final SelfSet<String,WorldDataLayer> data = new HashSelfSet<>();
	
	//Player actions
	/**
	 * Left click the given block
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return did click pass through?
	 */
	public boolean click(int x, int y) {
		BlockEntry ent = get(x, y);
		boolean result = ent instanceof BlockActivateListener;
		if(result) {
			((BlockActivateListener) ent).click(x, y, this, null, 0, 0);
		}
		return result;
	}
	/**
	 * The player object for this world
	 */
	public final Player player = new Player();
	
	//Map proxy
	/**
	 * Create a map proxy intended for temporary use
	 * @return newly created map proxy
	 */
	public MapProxy createProxy() {
		return new WorldProxy(this);
	}
	
	//Block entities
	Set<BlockEntity> _blockents = new HashSet<>();
	/**
	 * an unmodifiable {@link Set} of {@link BlockEntity}s on this {@code BlockMap}
	 */
	public final Set<BlockEntity> blockents = Collections.unmodifiableSet(_blockents);
	
	//Block array
	Grid<@Nonnull BlockEntry> entries;
	/**
	 * Gets block at given location
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return a block at given location, or null if absent
	 * @throws IndexOutOfBoundsException if the coordinates are out of bounds
	 */
	@Nonnull public BlockEntry get(int x, int y) {
		return entries.get(x-startX, y-startY);
	}
	/**
	 * @param s side, from which to get
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return the block entry at given location from side
	 */
	public BlockEntry getAtSide(Side s, int x, int y) {
		switch(s) {
		case D:
			return get(x, y+1);
		case DL:
			return get(x-1, y+1);
		case DR:
			return get(x+1, y+1);
		case L:
			return get(x-1, y);
		case R:
			return get(x+1, y);
		case U:
			return get(x, y-1);
		case UL:
			return get(x-1, y-1);
		case UR:
			return get(x+1, y-1);
		default:
			return get(x, y);
		}
	}
	
	private BlockEntry set0(BlockEntry b, int x, int y) {
		BlockEntry old = get(x, y);
		//Remove old block entity
		if(old.isBlockEntity()) {
			BlockEntity old0 = old.asBlockEntity();
			try {
				old0.onBreak(this, null);
				_blockents.remove(old0);
			} catch (Exception e) {
				debug.pstm(e, "Failed to remove BlockEntity ["+x+","+y+"]");
				return null;
			}
		}
		//Add new block entity
		if(b.isBlockEntity()) {
			BlockEntity new0 = b.asBlockEntity();
			try {
				new0.onPlace(this, null);
				_blockents.add(new0);
			}catch(Exception e) {
				debug.pstm(e, "Failed to place BlockEntity ["+x+","+y+"]");
				entries.set(x-startX, y-startY, ContentsBlocks.grass);
				return null;
			}
		}
		//Set block
		entries.set(x-startX, y-startY, ContentsBlocks.grass);
		old.resetMap(null, 0, 0);
		b.resetMap(this, x, y);
		entries.set(x-startX, y-startY, b);
		return b;
	}
	/**
	 * Places a block of given type
	 * @param b a block entry to place
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return a new block entry, or null if placement failed
	 */
	public BlockEntry set(BlockEntry b, int x, int y) {
		AtomicReference<BlockEntry> result = new AtomicReference<>();
		reserveAndDo(x, y, unused -> result.set(set0(b, x, y)));
		return result.get();
	}
	
	/**
	 * Places a block of given type
	 * @param type a block type to place
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return a new block entry, or null if placement failed
	 */
	public BlockEntry place(BlockType type, int x, int y) {
		BlockEntry blockent = type.createBlock();
		return set(blockent, x, y);
	}
	/** @return is given map usable? */
	public boolean isValid() {
		return entries != null;
	}
	
	//Dropped items
	
	
	//Bounds
	/** Leftmost X coordinate, inclusive */ public final int startX;
	/** @return leftmost X coordinate, inclusive */ public int startX() {return startX;}
	/** Uppermost Y coordinate, inclusive */ public final int startY;
	/** @return uppermost Y coordinate, inclusive*/ public int startY() {return startY;}
	/** Map width */ public final int sizeX;
	/** @return map width */ public int sizeX() {return sizeX;}
	/** Map height */ public final int sizeY;
	/** @return map height */ public int sizeY() {return sizeY;}
	/** Rightmost X coordinate, exclusive */ public final int endX;
	/** @return rightmost X coordinate, exclusive */ public int endX() {return endX;}
	/** Lowermost Y coordinate, exclusive */ public final int endY;
	/** @return lowermost Y coordinate, exclusive */ public int endY() {return endY;}
	
	/** @return UL corner of this map as a new {@link Point}*/
	public Point start() {
		return new Point(startX, startY);
	}
	/** @return dimensions of this map as a new {@link Dimension} */
	public Dimension rectsize() {
		return new Dimension(sizeX, sizeY);
	}
	/** @return bounds of this map as a new {@link Rectangle} */
	public Rectangle bounds() {
		return new Rectangle(startX, startY, sizeX, sizeY);
	}
	
	/**
	 * Checks if given point is located inside the bounds
	 * @param p position
	 * @return is given point in bounds?
	 */
	public boolean inBounds(Point p) {
		return inBounds(p.x, p.y);
	}
	/**
	 * Checks if given point is located inside the bounds
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return is given point in bounds?
	 */
	public boolean inBounds(int x, int y) {
		if(x < startX) return false;
		if(y < startY) return false;
		if(x >= endX) return false;
		return y < endY;
	}

	//NEW machines
	/**
	 * Remove the machine at given location
	 * @param p location
	 * @return was machine removed?
	 */
	public boolean removeMachine(Point p) {
		Machine machine = machinesPoints.get(p);
		if(machine == null) return false;
		//Calculate coordinates
		int posX = machine.posX();
		int posY = machine.posY();
		int mendX = posX + machine.sizeX();
		int mendY = posY + machine.sizeY();
		try {
			machine.onRemove(null);
		}catch(Exception e) {
			debug.pstm(e, "Failed to remove "+machine.id()+" at ["+posX+","+posY+"]");
			return false;
		}
		
		//Remove
		for(int x = posX; x < mendX; x++) {
			for(int y = posY; y < mendY; y++) {
				Point pt = new Point(x, y);
				machinesPoints.remove(pt);
			}
		}
		_machines.remove(machine);
		return true;
	}
	/**
	 * Remove the machine at given location
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return was machine removed?
	 */
	public boolean removeMachine(int x, int y) {
		return removeMachine(new Point(x, y));
	}
	/**
	 * Place the machine, using the machine model, with default properties
	 * @param m machine model which creates the machine
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return newly placed machine, or null if placement failed
	 */
	public Machine placeMachine(MachineModel m, int x, int y) {
		Machine machine = m.place();
		if(machine == null) return null;
		machine.setPos(x, y);
		return placeMachine(machine);
	}
	/**
	 * Places a machine which is already located
	 * @param machine machine to place
	 * @return placed machine
	 */
	public Machine placeMachine(Machine machine) {
		return placeMachine(machine, false);
	}
	/**
	 * 
	 * @param machine machine to place
	 * @param setup if true, call onLoad. Else call onPlace
	 * @return newly placed machine, or null if placement failed
	 */
	public Machine placeMachine(Machine machine, boolean setup) {
		machine.setMap(this);
		int posX = machine.posX();
		int posY= machine.posY();
		int mendX = posX + machine.sizeX();
		int mendY = posY + machine.sizeY();
		try {
			if(!setup) machine.onPlace(null);
			//When successfull, place
			for(int x = posX; x < mendX; x++) {
				for(int y = posY; y < mendY; y++) {
					Point pt = new Point(x, y);
					Machine old = machinesPoints.put(pt, machine);
					if(old != null) {
						//Overwriting existing machine, do not place the machine
						machinesPoints.put(pt, old);
						return null;
					}
				}
			}
		}catch(Exception e) {
			debug.pstm(e, "Failed to place "+machine.id()+" at ["+posX+","+posY+"]");
			return null; //null indicates that machine was not placed
		}
		debug.printl("Placed machine");
		_machines.add(machine);
		return machine;
	}
	/**
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return machine, or null if not found
	 */
	@Nullable
	public Machine getMachine(int x, int y) {
		return getMachine(new Point(x, y));
	}
	/**
	 * Gets machine at given location
	 * @param p point to check
	 * @return machine at given point, or null if none
	 */
	@Nullable
	public Machine getMachine(Point p) {
		return machinesPoints.get(p);
	}
	/**
	 * Place the machine, using the machine model, with default properties
	 * @param m machine model which creates the machine
	 * @param p position
	 * @return newly placed machine, or null if placement failed
	 */
	public Machine placeMachine(MachineModel m, Point p) {
		return placeMachine(m, p.x, p.y);
	}
	/**
	 * @param machine machine to be placed
	 * @return newly placed machine, or null if placement failed
	 */
	public Machine placeMachineLoaded(Machine machine) {
		return placeMachine(machine, true);
	}
	
	Set<Machine> _machines = new HashSet<>();
	/**
	 * The immutable set of all machines
	 */
	public final Set<Machine> machines = Collections.unmodifiableSet(_machines);
	Map<Point, Machine> machinesPoints = new HashMap<>();
	
	//Dropped items
	/**
	 * @param item item to be dropped
	 * @param x X coordinate of the item
	 * @param y Y coordinate of the item
	 */
	public void dropItem(ItemEntry item, int x, int y) {
		Collection<ItemEntry> list = getDrops(x, y);
		list.add(item);
	}
	/**
	 * The multimap containing all dropped items.
	 * DO NOT CHANGE ANY VECTORS IN THIS MAP
	 */
	public final Multimap<Vector2iconst, ItemEntry> drops = ArrayListMultimap.create();
	@Nonnull public InventoryWriter createDropper(int x, int y) {
		Collection<ItemEntry> collect = getDrops(x, y);
		return (ent, amount) -> {
			for(int i = 0; i < amount; i++) {
				collect.add(ent);
			}
			return amount;
		};
	}
	/**
	 * @param x X coordinate of item drop(s)
	 * @param y Y coordinate of item drop(s)
	 * @return
	 */
	public Collection<ItemEntry> getDrops(int x, int y){
		return getDrops(new Vector2iconst(x, y));
	}
	/**
	 * @param v
	 * @return
	 */
	public Collection<ItemEntry> getDrops(Vector2iconst v) {
		return drops.get(v);
	}
	
	/**
	 * Gets block at given location
	 * @param p position
	 * @return a block at given location, or null if absent
	 * @throws IndexOutOfBoundsException if the coordinates are out of bounds
	 */
	@Nonnull public BlockEntry get(Point p) {
		return get(p.x, p.y);
	}
		
	/**
	 * Converts this block map to a grid.
	 * Any changes in the grid are represented in the block map and vice versa.
	 * @return the {@link Grid} representation of this block map
	 */
	public Grid<@Nonnull BlockEntry> toGrid() {
		return new Grid<@Nonnull BlockEntry>() {
			@Override
			public void set(int x, int y, BlockEntry data) {
				World.this.set(data, x+startX, y+startY);
			}

			@Override
			public @Nonnull BlockEntry get(int x, int y) {
				return World.this.get(x+startX, y+startY);
			}

			@Override
			public int width() {
				return sizeX;
			}

			@Override
			public int height() {
				return sizeY;
			}	
		};
	}
	
	//Block Allocation Table
	
	//Slot reservation
	private Long2ObjectMap<ReentrantLock> locks = new Long2ObjectOpenHashMap<>(); //Phase 1: obtain
	private Slot returnSlot(Lock lck, int x, int y) {
		return new Slot() {
			private boolean close = false;
			@Override
			public void close() {
				close = true;
				lck.unlock();
			}

			@Override
			public BlockEntry get() {
				return World.this.get(x, y);
			}

			@Override
			public BlockEntry set(BlockEntry block) {
				if(close) throw new IllegalStateException("This slot was closed");
				return set0(block, x, y);
			}

			@Override
			public World getMap() {
				return World.this;
			}	
		};
	}
	/**
	 * Reserves a slot on the map
	 * @param x X coordinate of the slot
	 * @param y Y coordinate of the slot
	 * @return the block slot
	 * @throws IllegalMonitorStateException if the same lock is taken more than once by a one thread without releasing it first.
	 */
	public Slot reserve(int x, int y) {
		long id = Bitwise.combine2I2L(x, y);
		ReentrantLock lock;
		synchronized(locks) {
			lock = locks.get(id);
			if(lock != null) {
				//A lock is already taken
				if(lock.isHeldByCurrentThread()) 
					//This thread holds the lock, fail
					throw new IllegalMonitorStateException("The lock is already held");
				//A lock is not held, obtain it
				lock.lock();
				return returnSlot(lock, x, y);
			}
			//A lock does not exist
			lock = new ReentrantLock();
			locks.put(id, lock);
			lock.lock();
			return returnSlot(lock, x, y);
		}
	}
	/**
	 * Optionally reserves a slot on the map
	 * @param x X coordinate of the slot
	 * @param y Y coordinate of the slot
	 * @return the slot, or null if not avaliable
	 */
	public Slot tryReserve(int x, int y) {
		long id = Bitwise.combine2I2L(x, y);
		ReentrantLock lock;
		synchronized(locks) {
			lock = locks.get(id);
			if(lock != null) {
				//A lock is already taken
				if(lock.isHeldByCurrentThread()) 
					//This thread holds the lock, fail
					return null;
				//A lock is not held, obtain it
				boolean attempt = lock.tryLock();
				if(!attempt) return null;
				return returnSlot(lock, x, y);
			}
			//A lock does not exist
			lock = new ReentrantLock();
			locks.put(id, lock);
			boolean attempt = lock.tryLock();
			if(!attempt) return null;
			return returnSlot(lock, x, y);
		}
	}
	
	private Slot returnSlot(int x, int y) {
		return new Slot() {
			private boolean close = false;
			@Override
			public void close() {
				close = true;
			}

			@Override
			public BlockEntry get() {
				return World.this.get(x, y);
			}

			@Override
			public BlockEntry set(BlockEntry block) {
				if(close) throw new IllegalStateException("This slot was closed");
				return set0(block, x, y);
			}

			@Override
			public World getMap() {
				return World.this;
			}	
		};
	}
	/**
	 * Reserves a slot and does given action
	 * @param x X coordinate of a block
	 * @param y Y coordinate of a block
	 * @param action
	 */
	public void reserveAndDo(int x, int y, Consumer<Slot> action) {
		long id = Bitwise.combine2I2L(x, y);
		ReentrantLock lock;
		synchronized(locks) {
			lock = locks.get(id);
			if(lock != null) {
				//A lock is already taken
				if(lock.isHeldByCurrentThread()) 
					//This thread holds the lock, fail
					throw new IllegalMonitorStateException("The lock is already held");
				//A lock is not held, obtain it
				lock.lock();
				try(Slot slot = returnSlot(x, y)) {
					action.accept(slot);
				}finally {
					lock.unlock();
				}
			}
			//A lock does not exist
			lock = new ReentrantLock();
			locks.put(id, lock);
			lock.lock();
			try(Slot slot = returnSlot(x, y)) {
				action.accept(slot);
			}finally {
				lock.unlock();
			}
		}
	}
	
}