/**
 * 
 */
package mmb.engine.worlds;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collection;

import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.inv.Inventory;
import mmb.engine.rotate.Side;
import mmb.engine.visuals.Visual;
import mmb.engine.worlds.world.World;

/**
 * @author oskar
 *
 */
public interface MapProxy extends AutoCloseable{
	
	//Immediate setters
	/** 
	 * Place the block immediately using survival rules, draining block's or player's inventory
	 * @param block block to place
	 * @param x X coordinate of the block
	 * @param y Y coordinate of the block
	 */
	public void placeImmediately(BlockType block, int x, int y);
	/** Place the block immediately using survival rules, draining block's or player's inventory
	 * @param block block to place
	 * @param p block coordinates
	 */
	public default void placeImmediately(BlockType block, Point p) {placeImmediately(block, p.x, p.y);}
	
	/** Place the block immediately using creative rules
	 * @param block block to place
	 * @param x X coordinate of the block
	 * @param y Y coordinate of the block
	 */
	public void placeImmediatelyCreative(BlockType block, int x, int y);
	/** 
	 * Place the block immediately using creative rules
	 * @param block block to place
	 * @param p block coordinates
	 */
	public default void placeImmediatelyCreative(BlockType block, Point p) {placeImmediatelyCreative(block, p.x, p.y);}
	
	/** 
	 * Place the block immediately using survival rules, draining specific inventory
	 * @param block block to place
	 * @param x X coordinate of the block
	 * @param y Y coordinate of the block
	 * @param inv inventory to drain
	 */
	public void placeImmediately(BlockType block, int x, int y, Inventory inv);
	/** 
	 * Place the block immediately using survival rules, draining specific inventory
	 * @param block block to place
	 * @param p block coordinates
	 * @param inv inventory to drain
	 */
	public default void placeImmediately(BlockType block, Point p, Inventory inv) {placeImmediately(block, p.x, p.y, inv);}
	
	//Intermediate setters
	/** 
	 * Place the block when map updates using survival rules, draining block's or player's inventory
	 * @param block block to place
	 * @param x X coordinate of the block
	 * @param y Y coordinate of the block
	 */
	public void place(BlockType block, int x, int y);
	/** 
	 * Place the block when map updates using survival rules, draining block's or player's inventory
	 * @param block block to place
	 * @param p block coordinates
	 */
	public default void place(BlockType block, Point p) {place(block, p.x, p.y);}
	
	/** 
	 * Place the block when map updates using creative rules
	 * @param block block to place
	 * @param x X coordinate of the block
	 * @param y Y coordinate of the block
	 */
	public void placeCreative(BlockType block, int x, int y);
	/** 
	 * Place the block when map updates using creative rules
	 * Place the block immediately using creative rules
	 * @param block block to place
	 * @param p block coordinates
	 */
	public default void placeCreative(BlockType block, Point p) {placeCreative(block, p.x, p.y);}
	
	/** 
	 * Place the block when map updates using survival rules, draining specific inventory
	 * @param block block to place
	 * @param x X coordinate of the block
	 * @param y Y coordinate of the block
	 * @param inv inventory to drain
	 */
	public void place(BlockType block, int x, int y, Inventory inv);
	/** 
	 * Place the block when map updates using survival rules, draining specific inventory
	 * @param block block to place
	 * @param p block coordinates
	 * @param inv inventory to drain
	 */
	public default void place(BlockType block, Point p, Inventory inv) {place(block, p.x, p.y, inv);}
	
	//Getters
	/** Get a block in specific location.
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return block at a given location
	 * @throws ArrayIndexOutOfBoundsException if given location is out of bounds
	 */
	public BlockEntry get(int x, int y);
	/** 
	 * Get a block in specific location.
	 * @param p block coordinates
	 * @return block at a given location
	 * @throws ArrayIndexOutOfBoundsException if given location is out of bounds
	 */
	public default BlockEntry get(Point p) {return get(p.x, p.y);}
	/**
	 * Get a block at a specific side
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param s side to check
	 * @return a block at a given side
	 */
	public default BlockEntry getAtSide(int x, int y, Side s) {
		return get(s.offset(x, y));
	}
	
	/** Checks if given location is in bounds 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return is the block in bounds?
	 * */
	public boolean inBounds(int x, int y);
	/** Checks if given location is in bounds 
	 * @param p coordinates
	 * @return is the block in bounds?
	 * */
	public default boolean inBounds(Point p) {return inBounds(p.x, p.y);}
	
	/** @return Bounds of this map in form of a new {@link Rectangle} */
	public default Rectangle getBounds() {
		return new Rectangle(new Point(), getSize());
	}
	/** @return Width */
	public int sizeX();
	/** @return Width */
	public int sizeY();
	/** @return Leftmost X */
	public int startX();
	/** @return Uppermost Y */
	public int startY();
	/** @return Size of this map in form of a new {@link Dimension} */
	public default Dimension getSize() {
		return new Dimension(sizeX(), sizeY());
	}
	
	/** 
	 * Run given action when map proxy is closed 
	 * @param r action to run
	*/
	public void later(Runnable r);
	
	/** @return associated block map*/
	public World getMap();
	
	//Visuals
	/** Adds a visual object 
	 * @param vis visual object to add
	 */
	public void add(Visual vis);
	/**
	 * Adds visual objects
	 * @param vis visual object to add
	 */
	public default void adds(Visual... vis) {
		adds(Arrays.asList(vis));
	}
	/**
	 * Adds visual objects
	 * @param vis visual object to add
	 */
	public void adds(Collection<Visual> vis);
	/**
	 * Removes a visual object
	 * @param vis visual object to remove
	 */
	public void remove(Visual vis);
	/**
	 * Removes visual objects
	 * @param vis visual objects to remove
	 */
	public default void removes(Visual... vis) {
		removes(Arrays.asList(vis));
	}
	/**
	 * Removes visual objects
	 * @param vis visual objects to remove
	 */
	public void removes(Collection<Visual> vis);
}
