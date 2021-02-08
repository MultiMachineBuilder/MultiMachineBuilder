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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import mmb.GameObject;
import mmb.COLLECTIONS.Collects;
import mmb.DATA.json.JsonTool;
import mmb.ERRORS.UnsupportedObjectException;
import mmb.RUNTIME.RuntimeManager;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.machine.Machine;
import mmb.WORLD.machine.MachineModel;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class BlockMap implements GameObject{
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
	/** @return the owner*/
	@Override
	public World getContainer() {
		return owner;
	}
	/** @param owner the owner to set*/
	@Override
	public void setContainer(@Nonnull GameObject owner) {
		Objects.requireNonNull(owner, "The container must not be null");
		if(!(owner instanceof World)) throw new UnsupportedObjectException(owner.getClass().toString()+" container is not supported");
		this.owner = (World) owner;
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
			for(BlockEntry ent: requests) {
				that2.set(ent.x, ent.y, ent);
			}
			that2 = null;
		}
		@Override
		public void setImmediately(BlockType block, int x, int y) {
			if(that2 == null) return;
			BlockEntry be = BlockEntry.defaultProperties(x, y, that2, block);
			that2.place(x, y, block);
		}
		@Override
		public void placeImmediately(BlockType block, int x, int y) {
			//TODO block behavior
			setImmediately(block, x, y);
		}
		@Override
		public void placeImmediatelyCreative(BlockType block, int x, int y) {
			//TODO block behavior
			setImmediately(block, x, y);
		}
		@Override
		public void placeImmediately(BlockType block, int x, int y, Inventory inv) {
			//0.5 TODO Inventories
			//TODO block behavior
			setImmediately(block, x, y);
		}
		@Override
		public BlockEntry getBlock(int x, int y) {
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
		public void set(BlockType block, int x, int y) {
			if(block == null) return;
			requests.add(BlockEntry.defaultProperties(x, y, that2, block));
		}
		@Override
		public void place(BlockType block, int x, int y) {
			set(block, x, y);
		}
		@Override
		public void placeCreative(BlockType block, int x, int y) {
			//TODO block behavior
			set(block, x, y);
		}
		@Override
		public void place(BlockType block, int x, int y, Inventory inv) {
			//0.5 TODO Inventories
			//TODO block behavior
			set(block, x, y);
		}

		private List<BlockEntry> requests = new ArrayList<>();
	}
	//[end]
	//[start] serialization
	/**
	 * sizeX: x size
	 * sizeY: y size
	 * world: world data
	 * @return serialized JSON data
	 */
	public JsonObject serialize() {
		JsonObject result = new JsonObject();
		result.addProperty("sizeX", width);
		result.addProperty("sizeY", height);
		result.addProperty("startX", startX);
		result.addProperty("startY", startY);
		int sizeX = entries.length;
		int sizeY = entries[0].length;
		JsonArray world = new JsonArray();
		for(int y = 0; y < sizeY; y++) {
			for(int x = 0; x < sizeX; x++) {
				BlockEntry ent = entries[x][y];
				if(ent == null) world.add(JsonNull.INSTANCE);
				else world.add(ent.save());
			}
		}
		result.add("world", world);
		//save machines
		JsonArray savedMachines = new JsonArray();
		for(Machine m: machines) {
			JsonElement element = m.save();
			try {
				m.onShutdown();
			} catch (Exception e) {
				debug.pstm(e, "Failed to shut down the machine");
			}
			JsonArray savedMachine = new JsonArray();
			//format: [id, x, y, data]
			savedMachine.add(m.name());
			savedMachine.add(m.posX());
			savedMachine.add(m.posY());
			savedMachine.add(element);
			savedMachines.add(savedMachine);
		}
		result.add("machines", savedMachines);
		return result;
	}
	/**
	 * @param jsonElement
	 * @param name 
	 * @return deserialized block map
	 */
	public static BlockMap deserialize(JsonElement jsonElement, String name) {
		JsonObject obj = jsonElement.getAsJsonObject();
		int sizeX = obj.get("sizeX").getAsInt();
		int sizeY = obj.get("sizeY").getAsInt();
		int startX = JsonTool.requestInt("startX", obj, 0);
		int startY = JsonTool.requestInt("startY", obj, 0);
		JsonArray machines = JsonTool.requestArray("machines", obj);
		JsonArray dataArray = obj.get("world").getAsJsonArray();
		BlockMap result = new BlockMap(sizeX, sizeY, startX, startY);
		result.setName(name);
		//read machines
		for(JsonElement e: machines) {
			JsonArray array = e.getAsJsonArray();
			String id = array.get(0).getAsString();
			int x = array.get(1).getAsInt();
			int y = array.get(2).getAsInt();
			Machine machine = MachineModel.forID(id).initialize(x, y, array.get(3));
			result.placeMachine0(machine);
		}
		//read the world
		for(int y = startY, n = 0, j = 0; j < sizeY; y++, j++) {
			for(int x = startX, i = 0 ; i < sizeX; x++, i++, n++) {
				result.entries[i][j] = BlockEntry.load(dataArray.get(n), x, y, result);
			}
		}
		//read machines
		return result;
	}
	//[end]
	//[start] block access
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
	 * Set given block location to a given value.
	 * If given entry has a non-compliant position, it will cause severe logspam.
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param data new block entry
	 * @throws ArrayIndexOutOfBoundsException if coordinates are outside the world
	 */
	public void set(int x, int y, BlockEntry data) {
		if(data == null) return;
		entries[x-startX][y-startY] = data;
	}
	/**
	 * Places given block into a given location.
	 * @param x
	 * @param y
	 * @param typ block type to be placed
	 */
	public void place(int x, int y, BlockType typ) {
		if(typ == null) return;
		entries[x-startX][y-startY] = BlockEntry.defaultProperties(x, y, that, typ);
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
	 * Set given position to given block entry
	 * @param p position
	 * @param data new block entry
	 */
	public void set(Point p, BlockEntry data) {
		if(data == null) return;
		entries[p.x-startX][p.y-startY] = data;
	}
	/**
	 * Place given block at given position
	 * @param p
	 * @param typ new block type
	 */
	public void place(Point p, BlockType typ) {
		if(typ == null) return;
		entries[p.x-startX][p.y-startY] = BlockEntry.defaultProperties(p.x, p.y, that, typ);
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
			for(int i = 0; i < entries.length; i++) {
				for(int j = 0; j < entries[i].length; j++) {
					runBlock(entries[i][j],proxy);
				}
			}
		} catch (Exception e) {
			debug.pstm(e, "Unable to run the map.");
		}
	}
	private void runBlock(BlockEntry ent, MapProxy proxy) { //Run a single block
		if(ent != null) try {
			/*Run the block. Inputs:
			 * 1. block entry
			 * 2. map proxy
			 */
			BiConsumer<BlockEntry, MapProxy> run = ent.type.onUpdate;
			if(run != null)
				run.accept(ent, proxy);
		}catch(Exception e) {
			debug.pstm(e, "Failed to run a block ("+ent.type.id+") at ["+ent.x+","+ent.y+"]");
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
			if(running) return;
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
		for(int i = 0; i < width; i++) {
			BlockEntry[] ents = entries[i];
			for(int j = 0; j < height; j++) {
				BlockEntry ent = ents[j];
					if(ent != null){
						Consumer<BlockEntry> con = ent.type.shutdown;
					if(con != null) con.accept(ent);
					}
			}
		}
		entries = null;
	}
	//[end]
	//[start] machines
	private Map<Point, Machine> machinesPoints = new HashMap<>();
	private Set<Machine> machines = new HashSet<>();
	public Iterator<Machine> iteratorMachines() {
		return machines.iterator();
	}
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
		int posY= machine.posY();
		int mendX = posX + machine.sizeX();
		int mendY = posY + machine.sizeY();
		try {
			machine.onRemove(null);
		}catch(Exception e) {
			debug.pstm(e, "Failed to remove "+machine.name()+" at ["+posX+","+posY+"]");
			return false;
		}
		
		//Remove
		for(int x = posX; x < mendX; x++) {
			for(int y = posY; y < mendY; y++) {
				Point pt = new Point(x, y);
				machinesPoints.remove(pt);
			}
		}
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
		return placeMachine0(machine);
	}
	private Machine placeMachine0(Machine machine) {
		machines.add(machine);
		int posX = machine.posX();
		int posY= machine.posY();
		int mendX = posX + machine.sizeX();
		int mendY = posY + machine.sizeY();
		try {
			machine.onPlace(null);
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
			debug.pstm(e, "Failed to place "+machine.name()+" at ["+posX+","+posY+"]");
			return null; //null indicates that machine was not placed
		}
		return machine;
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
	@Override
	public String identifier() {
		return name;
	}
	@Override
	public Iterator<GameObject> iterator() {
		return Collects.<GameObject>downcastIterator(machines.iterator());
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
	@Override
	public boolean add(GameObject obj) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean remove(GameObject obj) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean contains(GameObject obj) {
		return false;
	}
	@Override
	public Set<GameObject> contents() {
		return Collections.unmodifiableSet(machines);
	}
}