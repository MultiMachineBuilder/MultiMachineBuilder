/**
 * 
 */
package mmb.engine.worlds.world;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.davidmoten.rtree2.Entries;
import com.github.davidmoten.rtree2.RTree;
import com.github.davidmoten.rtree2.geometry.Geometry;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.vavr.Tuple2;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mmb.NN;
import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.engine.GameEvents;
import mmb.engine.Vector2iconst;
import mmb.engine.block.BlockEntity;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockLoader;
import mmb.engine.block.BlockType;
import mmb.engine.block.Blocks;
import mmb.engine.chance.Chance;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.io.Dropper;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.item.ItemEntry;
import mmb.engine.json.JsonTool;
import mmb.engine.mbmachine.Machine;
import mmb.engine.mbmachine.MachineModel;
import mmb.engine.recipe.RecipeOutput;
import mmb.engine.rotate.Side;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.visuals.Visual;
import mmb.engine.worlds.DataLayers;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.universe.Universe;
import mmb.menu.world.FPSCounter;
import monniasza.collects.Identifiable;
import monniasza.collects.alloc.Allocator;
import monniasza.collects.alloc.Indexable;
import monniasza.collects.alloc.SimpleAllocator;
import monniasza.collects.datalayer.IndexedDatalayerMap;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 * <br>
 * A {@code BlockMap} is a representation of ingame 2D voxel map.
 * <br> To load the map, create and the use the {@code saveLoad} module
 * <br> ,its {@code getLoaded()} method for getting loaded {@code BlockMap}
 * <br> ,its {@code load()} method to load data
 * <br> and at first {@code setName()} to set target name
 */
public class World implements Identifiable<String>, Indexable{
	//Allocator & data layers
	@NN private static SimpleAllocator<@NN World> allocator0 = new SimpleAllocator<>();
	/** Allocator for world */
	@NN public static final Allocator<@NN World> allocator = allocator0.readonly();
	private int ordinal; //ordinal, set to -1 to prevent abuse after universe dies
	@Override
	public int ordinal() {
		return ordinal;
	}
	@Override
	public Object index() {
		return allocator;
	}
	
	//Debugging
	protected Debugger debug = new Debugger("MAP anonymous");
	
	//Naming
	private String name;
	/** @return this world's name */
	public String getName() {
		return name;
	}
	/**
	 * Renames the world
	 * @param name new name
	 */
	public void setName(@Nil String name) {
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
	public World(BlockEntry @NN [] @NN [] entries, int startX, int startY) {
		this.entries = new FixedGrid<>(entries);
		this.startX = startX;
		this.startY = startY;
		sizeX = entries.length;
		sizeY = entries[0].length;
		endX = startX + sizeX;
		endY = startY + sizeY;
		LODs = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
		ordinal = allocator0.allocate(this);
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
		entries.fill(Blocks.grass);
		this.startX = startX;
		this.startY = startY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		endX = startX + sizeX;
		endY = startY + sizeY;
		LODs = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
		ordinal = allocator0.allocate(this);
	}
	
	//Serialization
	/**
	 * Thrown when a malformed world is loaded
	 * @author oskar
	 */
	public static class WorldLoadException extends RuntimeException{
		private static final long serialVersionUID = -3479151473029171459L;
		public WorldLoadException() {
			super();
		}
		public WorldLoadException(String message, Throwable cause) {
			super(message, cause);
		}
		public WorldLoadException(String message) {
			super(message);
		}
		public WorldLoadException(Throwable cause) {
			super(cause);
		}
	}
	/**
	 * Loads a world from JSON
	 * @param json JSON data to load
	 * @return a world from JSON file
	 * @throws WorldLoadException when world misses critical information
	 * @throws NullPointerException if JSON data is null
	 */
	public static World load(JsonNode json) throws WorldLoadException {
		Objects.requireNonNull(json, "World is loaded with null"); //unlikely to occur
		
		//Dimensions
		JsonNode _sizeX = json.get("sizeX");
		JsonNode _sizeY = json.get("sizeY");
		JsonNode _startX = json.get("startX");
		JsonNode _startY = json.get("startY");
		//Verify dimensions
		if(_sizeX == null) throw new WorldLoadException("No X size");
		if(_sizeY == null) throw new WorldLoadException("No Y size");
		if(_startX == null) throw new WorldLoadException("No X start pos");
		if(_startY == null) throw new WorldLoadException("No Y start pos");
		//Set dimensions
		int sizeX = _sizeX.asInt();
		int sizeY = _sizeY.asInt();
		int startX = _startX.asInt();
		int startY = _startY.asInt();
		int endX = sizeX + startX;
		int endY = sizeY + startY;
		
		//Prepare the map
		World world = new World(sizeX, sizeY, startX, startY);
		
		//Blocks
		ArrayNode worldArray = (ArrayNode) json.get("world");
		if(worldArray == null) throw new WorldLoadException("No world array");
		Iterator<JsonNode> iter = worldArray.elements();
		for(int y = startY; y < endY; y++) {
			for(int x = startX; x < endX; x++) {
				JsonNode node = iter.next();

				BlockEntry block = BlockLoader.load(node, x, y, world);
				if(block == null) block = Blocks.grass;
				block.onStartup(world, 0, 0);
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
			if(type == null || machineData == null) continue;
			try {
				Machine machine = MachineModel.forID(type).initialize(x, y, machineData);
				if(machine != null) world.placeMachine(machine);
			} catch(Exception e) {
				world.debug.stacktraceError(e, "Failed to place a machine of type "+type+" at ["+x+","+y+"]");
			}
		}
		
		//Player data
		world.player.load(JsonTool.requestObject("player", (ObjectNode) json));
		
		//After loading process
		GameEvents.onWorldLoad.trigger(new Tuple2<>(world, (ObjectNode) json));
		
		//Postload the blocks
		for(int y = startY; y < endY; y++) {
			for(int x = startX; x < endX; x++) {
				BlockEntry block = world.get(x, y);
				block.postLoad(world, 0, 0);
			}
		}

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
			for(Machine m: world.machines0) {
				ArrayNode array = JsonTool.newArrayNode();
				//format: [id, x, y, data]
				array.add(m.id());
				array.add(m.posX());
				array.add(m.posY());
				array.add(m.save());
				machineArrayNode.add(array);
			}
			master.set("machines", machineArrayNode);
						
			//Player
			master.set("player", world.player.save());
			
			GameEvents.onWorldSave.trigger(new Tuple2<>(world, master));
			return master;
		}
	
	//Activity
	private static final long PERIOD = 20_000_000; //nanoseconds
	private TaskLoop timer = new TaskLoop(World.this::update, PERIOD);
	/** The Ticks Per Second counter */
	public final FPSCounter tps = new FPSCounter();
	private volatile boolean stopping = false;
	@SuppressWarnings("null")
	private void update() {//Run the map
		if(entries == null) return;
		tps.count();
		//Set up the proxy
		try(MapProxy proxy = createProxy()){
			//Run every machine
			for(Machine m: machines0) {
				try {
					m.onUpdate(proxy);
				}catch(Exception e) {
					debug.stacktraceError(e, "Failed to run a machine");
				}
			}
			//Run every block entity
			for(BlockEntity ent: _blockents) {
				//debug.printl("Running "+ent.type()+" @ "+UnitFormatter.formatPoint(ent.posX(), ent.posY()));
				try {
					long start = System.nanoTime();
					ent.onTick(proxy);
					long finish = System.nanoTime();
					long duration = finish - start;
					if(GlobalSettings.logExcessiveTime.getValue() && duration >= 1_000_000) {
						debug.printl("Block entity at ["+ent.posX()+","+ent.posY()+
							"] took exceptionally long to run");
					}
				}catch(Exception|StackOverflowError e){
					debug.stacktraceError(e, "Failed to run block entity at ["
						+ent.posX()+","+ent.posY()+"]");
				}
			}
			//Run every data layer
			for(IndexedDatalayerMap<World, ? extends DataLayer<World>> dls: DataLayers.layersWorld) {
				DataLayer<World> dl = dls.get(this);
				if(dl == null) throw new InternalError("Data layer found must not be null for a valid world");
				dl.cycle();
			}
		}catch(Exception e) {
			debug.stacktraceError(e, "Failed to run the map");
		}
		//Run the player
		player.onTick(this);
		if(stopping) {
			Thread.currentThread().interrupt();
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
		//This sometimes is stuck
		debug.printl("Shutdown");
		if(hasShutDown()) return;
		//Stop the game loop
		preventRuns();
		//Shut down block entities
		for(BlockEntity ent: _blockents) {
			try {
				ent.onShutdown(this);
			} catch (Exception e) {
				debug.stacktraceError(e, "Failed to shut down block "+ent.type().id()+" at ["+ent.posX()+","+ent.posY()+"]");
			}
		}
		//Shut down machines
		for(Machine m: machines) {
			try {
				m.onShutdown();
			}catch(Exception e) {
				debug.stacktraceError(e, "Failed to shut down machine "+m.id()+" at ["+m.posX()+","+m.posY()+"]");
			}
		}
		if(timer.getState() == 1) timer.destroy();
		GameEvents.onWorldDie.trigger(this);
	}
	
	//Lock out the world
	private void preventRuns() {
		if(timer.getState() == 0) return;
		stopping = true;
		try {
			timer.join();
		} catch (InterruptedException e) {
			debug.stacktraceError(e, "Interrupted!");
			Thread.currentThread().interrupt();
		}
	}
		
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
	/** The player object for this world */
	@NN public final Player player = new Player(this);
	
	//Map proxy
	/**
	 * Create a map proxy intended for temporary use
	 * @return newly created map proxy
	 */
	public MapProxy createProxy() {
		return new WorldProxy(this);
	}
	
	//Block entities
	@NN Set<BlockEntity> _blockents = new HashSet<>();
	/** an unmodifiable {@link Set} of {@link BlockEntity}s on this {@code BlockMap} */
	@NN public final Set<BlockEntity> blockents = Collections.unmodifiableSet(_blockents);
	
	//Block array
	private final Object blockLock = new Object();
	Grid<@NN BlockEntry> entries;
	/**
	 * Gets block at given location
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return a block at given location, or null if absent
	 */
	@NN public BlockEntry get(int x, int y) {
		if(!inBounds(x, y)) return Blocks.blockVoid;
		return entries.get(x-startX, y-startY);
	}
	/**
	 * Gets block off a given location, in given direction
	 * @param s side, from which to get
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return the block entry at given location from side
	 */
	public BlockEntry getAtSide(Side s, int x, int y) {
		return get(x+s.blockOffsetX, y+s.blockOffsetY);
	}
	
	BlockEntry set0(BlockEntry b, int x, int y) {
		BlockEntry old = get(x, y);
		//Remove old block entity
		if(old instanceof BlockEntity) {
			BlockEntity old0 = (BlockEntity) old;
			try {
				old0.onBreak(this, x, y);
				_blockents.remove(old0);
			} catch (Exception e) {
				debug.stacktraceError(e, "Failed to remove BlockEntity ["+x+","+y+"]");
				return null;
			}
		}
		//Add new block entity
		if(b instanceof BlockEntity) {
			BlockEntity new0 = (BlockEntity) b;
			try {
				new0.onPlace(this, x, y);
				_blockents.add(new0);
			}catch(Exception e) {
				debug.stacktraceError(e, "Failed to place BlockEntity ["+x+","+y+"]");
				entries.set(x-startX, y-startY, Blocks.grass);
				return null;
			}
		}
		//Set block
		entries.set(x-startX, y-startY, Blocks.grass);
		old.resetMap(null, 0, 0);
		b.resetMap(this, x, y);
		entries.set(x-startX, y-startY, b);
		LODs.setRGB(x-startX, y-startY, b.type().getTexture().LOD());
		return b;
	}
	/**
	 * Places given block
	 * @param b a block entry to place
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return a new block entry, or null if placement failed
	 */
	public BlockEntry set(BlockEntry b, int x, int y) {
		synchronized(blockLock) {
			return set0(b, x, y);
		}
	}
	
	/**
	 * Places a block of given type
	 * @param type a block type to place
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return a new block entry, or null if placement failed
	 */
	public BlockEntry place(BlockType type, int x, int y) {
		try {
			BlockEntry blockent = type.createBlock();
			return set(blockent, x, y);
		}catch(Exception e) {
			debug.stacktraceError(e, "Failed to place a block at "+x+","+y);
			return null;
		}
		
	}
	BlockEntry place0(BlockType type, int x, int y) {
		try {
			BlockEntry blockent = type.createBlock();
			return set0(blockent, x, y);
		}catch(Exception e) {
			debug.stacktraceError(e, "Failed to place a block at "+x+","+y);
			return null;
		}
	}
	/** @return is given map usable? */
	public boolean isValid() {
		return entries != null;
	}
	
	//LOD buffer
	/** The LOD image for this world */
	public final BufferedImage LODs;
	
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

	//TO BE REMOVED IN 0.6 machines
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
			machine.onRemove();
		}catch(Exception e) {
			debug.stacktraceError(e, "Failed to remove "+machine.id()+" at ["+posX+","+posY+"]");
			return false;
		}
		
		//Remove
		for(int x = posX; x < mendX; x++) {
			for(int y = posY; y < mendY; y++) {
				Point pt = new Point(x, y);
				machinesPoints.remove(pt);
			}
		}
		machines0.remove(machine);
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
			if(!setup) machine.onPlace();
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
			debug.stacktraceError(e, "Failed to place "+machine.id()+" at ["+posX+","+posY+"]");
			return null; //null indicates that machine was not placed
		}
		debug.printl("Placed machine");
		machines0.add(machine);
		return machine;
	}
	/**
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return machine, or null if not found
	 */
	@Nil
	public Machine getMachine(int x, int y) {
		return getMachine(new Point(x, y));
	}
	/**
	 * Gets machine at given location
	 * @param p point to check
	 * @return machine at given point, or null if none
	 */
	@Nil
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
	
	Set<Machine> machines0 = new HashSet<>();
	/** The immutable set of all machines */
	public final Set<Machine> machines = Collections.unmodifiableSet(machines0);
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
	 * @param item item to be dropped
	 * @param amount amount of the item to be dropped
	 * @param x X coordinate of the item
	 * @param y Y coordinate of the item
	 */
	public void dropItem(ItemEntry item, int amount, int x, int y) {
		Collection<ItemEntry> list = getDrops(x, y);
		for(int i = 0; i < amount; i++) list.add(item);
	}
	/**
	 * @param item item to be dropped
	 * @param x X coordinate of the item
	 * @param y Y coordinate of the item
	 */
	public void dropItems(RecipeOutput item, int x, int y) {
		dropItems(item, 1, x, y);
	}
	/**
	 * @param item item to be dropped
	 * @param amount amount of the item to be dropped
	 * @param x X coordinate of the item
	 * @param y Y coordinate of the item
	 */
	public void dropItems(RecipeOutput item, int amount, int x, int y) {
		for(Object2IntMap.Entry<ItemEntry> entry: item.getContents().object2IntEntrySet()) {
			Collection<ItemEntry> list = getDrops(x, y);
			int amt2 = amount*entry.getIntValue();
			for(int i = 0; i < amt2; i++) list.add(entry.getKey());
		}
	}
	/**
	 * @param item item to be dropped
	 * @param x X coordinate of the item
	 * @param y Y coordinate of the item
	 */
	public void dropChance(Chance item, int x, int y) {
		item.drop(null, this, x, y);
	}
	/**
	 * @param item item to be dropped
	 * @param amount amount of the item to be dropped
	 * @param x X coordinate of the item
	 * @param y Y coordinate of the item
	 */
	public void dropChance(Chance item, int amount, int x, int y) {
		item.produceResults(createDropper(x, y), amount);
	}
	/**
	 * The multimap containing all dropped items.
	 * DO NOT CHANGE ANY VECTORS IN THIS MAP
	 */
	public final Multimap<Vector2iconst, ItemEntry> drops = ArrayListMultimap.create();
	/**
	 * Creates an {@code InventoryWriter} which drops items at given location
	 * @param x X coordinate of item drop(s)
	 * @param y Y coordinate of item drop(s)
	 * @return the inventory writer
	 */
	@NN public InventoryWriter createDropper(int x, int y) {
		return new Dropper(x, y, this);
	}
	/**
	 * @param x X coordinate of item drop(s)
	 * @param y Y coordinate of item drop(s)
	 * @return dropped item list at given location.
	 * The item list is bound to the location, so any changes which are made to the list are reflected in the world and vice versa.
	 */
	public Collection<ItemEntry> getDrops(int x, int y){
		return getDrops(new Vector2iconst(x, y));
	}
	/**
	 * @param v posiion of item drop(s)
	 * @return dropped item list at given location.
	 * The item list is bound to the location, so any changes which are made to the list are reflected in the world and vice versa.
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
	@NN public BlockEntry get(Point p) {
		return get(p.x, p.y);
	}
		
	/**
	 * Converts this block map to a grid.
	 * Any changes in the grid are represented in the block map and vice versa.
	 * @return the {@link Grid} representation of this block map
	 */
	@NN public Grid<@NN BlockEntry> toGrid() {
		return new Grid<>() {
			@Override
			public void set(int x, int y, BlockEntry data) {
				World.this.set(data, x+startX, y+startY);
			}

			@Override
			public @NN BlockEntry get(int x, int y) {
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
	
	//Visual objects
	private AtomicReference<RTree<Visual, Geometry>> visuals = new AtomicReference<>(RTree.star().create());
	/**
	 * Adds a visual object
	 * @param vis visual object to add
	 */
	public void addVisual(Visual vis) {
		visuals.updateAndGet(v -> v.add(vis, vis.border()));
	}
	/**
	 * Adds visual objects
	 * @param vis visual objects to add
	 */
	public void addVisuals(Visual... vis) {
		List<com.github.davidmoten.rtree2.Entry<Visual, Geometry>> list = visuals2list(vis);
		visuals.updateAndGet(v -> v.add(list));
	}
	/**
	 * Adds visual objects
	 * @param vis visual objects to add
	 */
	public void addVisuals(Collection<Visual> vis) {
		List<com.github.davidmoten.rtree2.Entry<Visual, Geometry>> list = visuals2list(vis);
		visuals.updateAndGet(v -> v.add(list));
	}
	/**
	 * Removes a visual object
	 * @param vis visual object to remove
	 */
	public void removeVisual(Visual vis) {
		visuals.updateAndGet(v -> v.delete(vis, vis.border()));
	}
	/**
	 * Removes visual objects
	 * @param vis visual objects to remove
	 */
	public void removeVisuals(Visual... vis) {
		List<com.github.davidmoten.rtree2.Entry<Visual, Geometry>> list = visuals2list(vis);
		visuals.updateAndGet(v -> v.delete(list));
	}
	/**
	 * Removes visual objects
	 * @param vis visual objects to remove
	 */
	public void removeVisuals(Collection<Visual> vis) {
		List<com.github.davidmoten.rtree2.Entry<Visual, Geometry>> list = visuals2list(vis);
		visuals.updateAndGet(v -> v.delete(list));
	}
	
	private static List<com.github.davidmoten.rtree2.Entry<Visual, Geometry>> visuals2list(Visual... vis) {
		return Arrays.stream(vis).map(
				val -> Entries.entry(val, val.border())
				).collect(Collectors.toList());
	}
	private static List<com.github.davidmoten.rtree2.Entry<Visual, Geometry>> visuals2list(Collection<Visual> vis) {
		return vis.stream().map(
				val -> Entries.entry(val, val.border())
				).collect(Collectors.toList());
	}
	
	/** @return current snapshot of visuals */
	public RTree<Visual, Geometry> visuals(){
		return visuals.get();
	}
}