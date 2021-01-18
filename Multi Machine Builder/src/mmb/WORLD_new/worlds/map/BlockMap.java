/**
 * 
 */
package mmb.WORLD_new.worlds.map;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import mmb.WORLD_new.block.BlockEntry;
import mmb.WORLD_new.block.BlockType;
import mmb.WORLD_new.inventory.Inventory;
import mmb.WORLD_new.worlds.MapProxy;
import mmb.WORLD_new.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class BlockMap extends TimerTask implements AutoCloseable {
	private Timer timer;
	private boolean running = false;
	
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
	
	private final BlockEntry[][] entries;
	
	private BlockMap that = this;
	
	//SECTION naming
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
			debug.printl("Renamed to: "+name);
			debug.id = "MAP - " + name;
		}else {
			debug.printl("Became anonymous");
			debug.id = "MAP - " + hashCode;
		}
		
	}
	
	//SECTION identification
	private static Random r = new Random();
	private int hashCode = r.nextInt();
	private Debugger debug = new Debugger("MAP - "+hashCode);
	@Override
	public int hashCode() {
		return hashCode;
	}
	@Override
	public boolean equals(Object obj) {
		return this == obj;
		/*if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockMap other = (BlockMap) obj;
		return hashCode == other.hashCode;*/
	}
	
	//SECTION constructors
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
	
	
	
	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}
	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean b) {
		if(b) {
			if(running) return;
			timer.scheduleAtFixedRate(this, 0, 20);
		}else {
			if(running) timer.cancel();
		}
		running = b;
	}

	public World owner;
	
	public void update() {
		//Run the map
		
		//Set up the proxy
		try(MapProxy mp = createVolatileProxy()){
			//Run every block
			for(int i = 0, x = 0; i < entries.length; i++, x++) {
				for(int j = 0, y = 0; i < entries[i].length; j++, y++) {
					BlockEntry be = entries[x][y];
					try {
						/*Run the block. Inputs:
						 * 1. block entry
						 * 2. current map
						 * 3. current world
						 * 
						 * The function should return
						 */
					}catch(Exception e) {
						debug.pstm(e, "Failed to run a block ("+be.getType().id+") at ["+x+","+y+"]");
					}
				}
			}
		} catch (Exception e) {
			debug.pstm(e, "Unable to run the map.");
		}
	}
	/**
	 * @return
	 */
	public MapProxy createVolatileProxy() {
		return new BlockMapProxy();
	}
	@Override
	public void run() {
		update();
	}
	private class BlockMapProxy implements MapProxy{
		private BlockMap that2 = that;
		@Override
		public void close() {
			that2 = null;
		}
		@Override
		public void setImmediately(BlockType block, int x, int y) {
			if(that2 == null) return;
			BlockEntry be = BlockEntry.defaultProperties(x, y, that2, block);
			that2.entries[x][y] = be;
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
			// TODO Inventories
			//TODO block behavior
			setImmediately(block, x, y);
		}
		@Override
		public BlockEntry getBlock(int x, int y) {
			return that2.entries[x][y];
		}
		@Override
		public boolean inBounds(int x, int y) {
			return that2.inBounds(x, y);
		}
		@Override
		public Dimension getSize() {
			return new Dimension(that2.entries.length, that2.entries[0].length);
		}	
	}
	
	/**
	 * sizeX: x size
	 * sizeY: y size
	 * world: world data
	 */
	public JsonObject serialize() {
		JsonObject result = new JsonObject();
		result.addProperty("sizeX", entries.length);
		result.addProperty("sizeY", entries[0].length);
		int sizeX = entries.length;
		int sizeY = entries[0].length;
		JsonArray world = new JsonArray();
		for(int y = 0; y < sizeY; y++) {
			for(int x = 0; x < sizeX; x++) {
				BlockEntry ent = entries[x][y];
				if(ent == null) world.add(new JsonNull());
				else world.add(ent.save());
			}
		}
		result.add("world", world);
		return result;
	}
	/**
	 * @param jsonElement
	 * @param name 
	 * @return
	 */
	public static BlockMap deserialize(JsonElement jsonElement, String name) {
		JsonObject obj = jsonElement.getAsJsonObject();
		int sizeX = obj.get("sizeX").getAsInt();
		int sizeY = obj.get("sizeY").getAsInt();
		JsonArray dataArray = obj.get("world").getAsJsonArray();
		BlockMap result = new BlockMap(sizeX, sizeY);
		for(int y = 0, n = 0; y < sizeY; y++) {
			for(int x = 0; x < sizeX; x++) {
				result.entries[x][y] = BlockEntry.load(dataArray.get(n++), x, y, result);
			}
		}
		return result;
	}
	
	//SECTION access
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
		entries[x-startX][y-startY] = data;
	}
	/**
	 * Places given block into a given location.
	 * @param x
	 * @param y
	 * @param typ block type to be placed
	 */
	public void place(int x, int y, BlockType typ) {
		entries[x-startX][y-startY] = BlockEntry.defaultProperties(x, y, that, typ);
	}
	public BlockEntry get(Point p) {
		return entries[p.x-startX][p.y-startY];
	}
	public void set(Point p, BlockEntry data) {
		entries[p.x-startX][p.y-startY] = data;
	}
	public void place(Point p, BlockType typ) {
		entries[p.x-startX][p.y-startY] = BlockEntry.defaultProperties(p.x, p.y, that, typ);
	}
	
	
	//SECTION bounds
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
		if(y >= endY) return false;
		return true;
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
	public Rectangle bounds() {
		return new Rectangle(startX, startY, width, height);
	}
	/**
	 * @return
	 */
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	//A map daemon is an object which runs the map.
}
