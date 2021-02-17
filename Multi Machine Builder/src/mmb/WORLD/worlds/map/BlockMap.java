/**
 * 
 */
package mmb.WORLD.worlds.map;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mmb.GameObject;
import mmb.RunnableObject;
import mmb.BEANS.Saver;
import mmb.COLLECTIONS.HashSelfSet;
import mmb.COLLECTIONS.SelfSet;
import mmb.DATA.json.JsonTool;
import mmb.RUNTIME.RuntimeManager;
import mmb.WORLD.block.Block;
import mmb.WORLD.block.BlockEntity;
import mmb.WORLD.block.SkeletalBlockEntity;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.machine.Machine;
import mmb.WORLD.machine.MachineModel;
import mmb.WORLD.worlds.BlockChangeRequest;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 * <br>
 * A {@code BlockMap} is a representation of ingame 2D voxel map.
 * <br> To load the map, use {@link mmb.WORLD.worlds.world.BlockMapLoader}
 * <br> ,its {@code getLoaded()} method for getting loaded {@code BlockMap}
 * <br> ,its {@code load()} method to load data
 * <br> and at first {@code setName()} to set target name
 */
public class BlockMap implements RunnableObject, Saver<JsonNode>{
	//[start] naming
	private static final String txtMAP = "MAP - ";
	private String name;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
		if(name == null) {
			debug.printl("Became anonymous");
			debug.id = txtMAP + hashCode;
		}else{
			debug.printl("Renamed to: "+name);
			debug.id = txtMAP + name;
		}
		
	}
	//[end]
	//[start] identification
	private static Random r = new Random();
	private int hashCode = r.nextInt();
	private Debugger debug = new Debugger(txtMAP+hashCode);
	@Override
	public int hashCode() {
		return hashCode;
	}
	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}
	private World owner;
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(World owner) {
		this.owner = owner;
	}
	//[end]
	//[start] constructors
	/**
	 * Creates a world with given entries and starting at (0,0)
	 * @param entries world data
	 */
	public BlockMap(BlockEntry[][] entries) {
		this(entries, 0, 0);
	}
	
	/**
	 * Creates an empty world with given dimensions and starting at (0,0)
	 * @param sizeX horizontal size
	 * @param sizeY vertical size
	 */
	public BlockMap(int sizeX, int sizeY) {
		this(new BlockEntry[sizeX][sizeY], 0, 0);
	}
	/**
	 * Creates an empty world with given dimensions and starting at (0,0)
	 * @param dim dimensions
	 */
	public BlockMap(Dimension dim) {
		this(new BlockEntry[dim.width][dim.height], 0, 0);
	}
	
	/**
	 * Creates a world with given entries and specific starting position
	 * @param entries world data
	 * @param start starting coordinates
	 */
	public BlockMap(BlockEntry[][] entries, Point start) {
		this(entries, start.x, start.y);
	}
	/**
	 * Creates a world with given entries and specific starting position
	 * @param entries world data
	 * @param startX starting X
	 * @param startY starting Y
	 */
	public BlockMap(BlockEntry[][] entries, int startX, int startY) {
		super();
		this.entries = entries;
		this.startX = startX;
		this.startY = startY;
		width = entries.length;
		height = entries[0].length;
		endX = startX + width;
		endY = startY + height;
	}
	
	/**
	 * Create an empty world with given bounds
	 * @param sizeX horizontal size
	 * @param sizeY vertical size
	 * @param startX starting X
	 * @param startY starting Y
	 */
	public BlockMap(int sizeX, int sizeY, int startX, int startY) {
		this(new BlockEntry[sizeX][sizeY], startX, startY);
	}
	/**
	 * Create an empty world with given bounds
	 * @param dim dimensions
	 * @param start starting coordinates
	 */
	public BlockMap(Dimension dim, Point start) {
		this(new BlockEntry[dim.width][dim.height], start.x, start.y);
	}
	/**
	 * Create an empty world with given coordinates
	 * @param r bounds
	 */
	public BlockMap(Rectangle r) {
		this(new BlockEntry[r.width][r.height], r.x, r.y);
	}
	//[end]
	//[start] proxy
	/**
	 * Create a map proxy intended for temporary use
	 * @return newly created map proxy
	 */
	public MapProxy createVolatileProxy() {
		return new BlockMapProxy();
	}
	private class BlockMapProxy implements MapProxy{
		private BlockMap that2 = that;
		@Override
		public void close() {
			for(BlockChangeRequest ent: requests) {
				ent.apply(that2);
			}
			that2 = null;
		}
		@Override
		public void placeImmediately(BlockType block, int x, int y) {
			if(that2 == null) return;
			block.place(x, y, that2);
		}
		@Override
		public void placeImmediatelyCreative(BlockType block, int x, int y) {
			//TODO block behavior
			placeImmediately(block, x, y);
		}
		@Override
		public void placeImmediately(BlockType block, int x, int y, Inventory inv) {
			//0.5 TODO Inventories
			//TODO block behavior
			placeImmediately(block, x, y);
		}
		@Override
		public BlockEntry get(int x, int y) {
			return that2.entries[x-startX][y-startY];
		}
		@Override
		public boolean inBounds(int x, int y) {
			return that2.inBounds(x, y);
		}
		@Override
		public Dimension getSize() {
			return new Dimension(that2.entries.length, that2.entries[0].length);
		}

		@Override
		public void place(BlockType block, int x, int y) {
			if(block == null) return;
			requests.add(new BlockChangeRequest(x, y, block));
		}
		@Override
		public void placeCreative(BlockType block, int x, int y) {
			//TODO block behavior
			place(block, x, y);
		}
		@Override
		public void place(BlockType block, int x, int y, Inventory inv) {
			//0.5 TODO Inventories
			//TODO block behavior
			place(block, x, y);
		}

		private List<BlockChangeRequest> requests = new ArrayList<>();
	}
	//[end]
	//[start] serialization
	@Override
	public JsonNode save() {
		//Master node
		ObjectNode master = JsonTool.newObjectNode();
		
		//Dimensions
		master.set("sizeX", new IntNode(width));
		master.set("sizeY", new IntNode(height));
		master.set("startX", new IntNode(startX));
		master.set("startY", new IntNode(startY));
		//Blocks
		ArrayNode blockArrayNode = JsonTool.newArrayNode();
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				BlockEntry ent = entries[x][y];
				if(ent == null) blockArrayNode.add(NullNode.getInstance());
				else blockArrayNode.add(ent.save());
			}
		}
		master.set("world", blockArrayNode);
		
		//Machines
		ArrayNode machineArrayNode = JsonTool.newArrayNode();
		for(Machine m: _machines) {
			ArrayNode array = JsonTool.newArrayNode();
			//format: [id, x, y, data]
			array.add(m.identifier());
			array.add(m.posX());
			array.add(m.posY());
			array.add(m.save());
			machineArrayNode.add(array);
		}
		master.set("machines", machineArrayNode);
		
		//Data layers
		ObjectNode dataNode = JsonTool.newObjectNode();
		for(MapDataLayer mdl: data) {
			dataNode.set(mdl.identifier(), mdl.save());
		}
		master.set("data", dataNode);
		
		return master;
	}
	//[end]
	//[start] block access
	private Set<BlockEntity> _blockents = new HashSet<>();
	/**
	 * an unmodifiable {@link Set} of {@link SkeletalBlockEntity}s on this {@code BlockMap}
	 */
	public final Set<BlockEntity> blockents = Collections.unmodifiableSet(_blockents);
	private BlockEntry[][] entries;
	private BlockMap that = this;
	/**
	 * Gets a block located at given position
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @throws ArrayIndexOutOfBoundsException if coordinates are outside the world
	 * @return block at given position
	 */
	public BlockEntry get(int x, int y) {
		return entries[x-startX][y-startY];
	}
	/**
	 * Places given block into a given location.
	 * @param x
	 * @param y
	 * @param typ block type to be placed
	 */
	public void place(int x, int y, BlockEntityType typ) {
		if(typ == null) return;
		set(typ.create(x, y, this), x, y);
		
	}
	/**
	 * Get the block at given position
	 * @param p position
	 * @return block at given position
	 */
	public BlockEntry get(Point p) {
		return entries[p.x-startX][p.y-startY];
	}
	/**
	 * @param b block entry to place
	 * @param x X coordinate of block
	 * @param y Y coordinate of block
	 * @discouraged Use place() methods instead
	 */
	public void set(BlockEntry b, int x, int y) {
		BlockEntry old = entries[x-startX][y-startY];
		if(old != null && old.isBlockEntity()) {
			BlockEntity old0 = old.asBlockEntity();
			try {
				old0.onBreak(that, null);
				_blockents.remove(old0);
			} catch (Exception e) {
				debug.pstm(e, "Failed to remove BlockEntity ["+x+","+y+"]");
				return;
			}
		}
		if(b.isBlockEntity()) {
			BlockEntity new0 = b.asBlockEntity();
			try {
				new0.onPlace(that, owner);
				_blockents.add(new0);
			}catch(Exception e) {
				debug.pstm(e, "Failed to place BlockEntity ["+x+","+y+"]");
				place(ContentsBlocks.grass, x, y);
			}
		}
		entries[x-startX][y-startY] = b;
	}
	public void place(Block b, int x, int y) {
		set(b, x, y);
	}
	public void place(BlockType type, int x, int y) {
		BlockEntry blockent = type.create(x, y, this);
		set(blockent, x, y);
	}
	/**
	 * This method checks if map data is valid.
	 * When a map is destroyed, the block data is deleted
	 * @return is map valid?
	 */
	public boolean isValid() {
		return entries != null;
	}
	//[end]	
	//[start] bounds
	/** Starting X coordinate, inclusive */
	public final int startX;
	/** Starting Y coordinate, inclusive */
	public final int startY;
	/** Map width */
	public final int width;
	/** Map height */
	public final int height;
	/** Ending X coordinate, exclusive*/
	public final int endX;
	/** Ending Y coordinate, exclusive*/
	public final int endY;
	/**
	 * Checks if given point is located inside the bounds
	 * @param p position
	 * @return is point inside the bounds?
	 */
	public boolean inBounds(Point p) {
		return inBounds(p.x, p.y);
	}
	/**
	 * Checks if given point is located inside the bounds
	 * @param x X position
	 * @param y Y position
	 * @return is point inside the bounds?
	 */
	public boolean inBounds(int x, int y) {
		if(x < startX) return false;
		if(y < startY) return false;
		if(x >= endX) return false;
		return y < endY;
	}
	/**
	 * Gets the dimensions of the world
	 * NOTE: The dimension is not connected to world dimensions.
	 * @return world (width, height) dimensions
	 */
	public Dimension size() {
		return new Dimension(width, height);
	}
	/**
	 * Return world start coordinates in a single object form factor.
	 * NOTE: The point is not connected to the world start coordinates.
	 * @return world (x, y) starting point
	 */
	public Point startCoords() {
		return new Point(startX, startY);
	}
	/**
	 * Return the world bound in form of {@code Rectangle}
	 * @return world bounds
	 */
	public Rectangle bounds() {
		return new Rectangle(startX, startY, width, height);
	}
	//[end]
	//[start] activity
	private void update() {//Run the map
		//Set up the proxy
		try(MapProxy proxy = createVolatileProxy()){
			if(entries == null) return;
			//Run every block
			for(BlockEntity ent: blockents) {
				try {
					ent.onTick(proxy);
				}catch(Exception e) {
					debug.pstm(e, "Failed to run block entity at ["+ent.posX()+","+ent.posY()+"]");
				}
			}
			//Run every machine
			for(Machine m: _machines) {
				m.onUpdate(proxy);
			}
		}
	}
	private Timer timer = new Timer();
	private boolean running = false;
	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}
	/**
	 * @param b the running to set
	 */
	public void setRunning(boolean b) {
		if(b) {
			if(running || hasShutDown) return;
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					update();
				}
				
			}, 0, 20);
		}else {
			if(running) timer.cancel();
		}
		running = b;
	}
	/**
	 * Disables the map
	 */
	public void dispose() {
		setRunning(false);
	}
	/**
	 * Enables the map
	 */
	public void activate() {
		setRunning(true);
	}
	/**
	 * Destroy all map resources
	 */
	@Override
	public void destroy() {
		setRunning(false);
		shutdown();
		entries = null;
	}
	private boolean hasShutDown = false;
	/**
	 * @return the hasShutDown
	 */
	public boolean hasShutDown() {
		return hasShutDown;
	}
	/**
	 * Shut down the map, but keep the resouirces
	 */
	public void shutdown() {
		if(hasShutDown) return;
		//Shut down block entities
		for(BlockEntity ent: blockents) {
			try {
				ent.onShutdown(this);
			} catch (Exception e) {
				debug.pstm(e, "Failed to shut down block "+ent.type().identifier()+" at ["+ent.posX()+","+ent.posY()+"]");
			}
		}
		//Shut down machines
		for(Machine m: machines) {
			try {
				m.onShutdown();
			}catch(Exception e) {
				debug.pstm(e, "Failed to shut down machine "+m.identifier()+" at ["+m.posX()+","+m.posY()+"]");
			}
		}
		hasShutDown = true;
		setRunning(false);
	}
	//[end]
	//[start] machines
	private Map<Point, Machine> machinesPoints = new HashMap<>();
	private Set<Machine> _machines = new HashSet<>();
	/**
	 * An unmodifiable set of all machines
	 */
	public final Set<Machine> machines = Collections.unmodifiableSet(_machines);
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
			debug.pstm(e, "Failed to remove "+machine.identifier()+" at ["+posX+","+posY+"]");
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
			debug.pstm(e, "Failed to place "+machine.identifier()+" at ["+posX+","+posY+"]");
			return null; //null indicates that machine was not placed
		}
		debug.printl("Placed machine");
		_machines.add(machine);
		return machine;
	}
	public Machine placeMachineLoaded(Machine machine) {
		return placeMachine(machine, true);
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
	 * Get a machine based on coordinates
	 * @param p machine coordinates
	 * @return machine, or null if not found
	 */
	@Nullable
	public Machine getMachine(Point p) {
		return machinesPoints.get(p);
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
	//[end]
	//[start] data layers
		public final SelfSet<String,MapDataLayer> data = new HashSelfSet<>();
	//[end]
	//[start] GameObject
	@Override
	public String identifier() {
		return name;
	}
	@Override
	public String getUTID() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public RuntimeManager getRuntimeManager() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public GameObject getOwner() {
		return null;
	}
	//[end]
	
}