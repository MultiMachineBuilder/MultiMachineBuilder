/**
 * 
 */
package mmbeng.worlds;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collection;

import mmbeng.block.BlockEntry;
import mmbeng.block.BlockType;
import mmbeng.inv.Inventory;
import mmbeng.rotate.Side;
import mmbeng.visuals.Visual;
import mmbeng.worlds.world.World;

/**
 * @author oskar
 *
 */
public interface MapProxy extends AutoCloseable{
	
	//Immediate setters
	/** Place the block immediately using survival rules, draining block's or player's inventory*/
	public void placeImmediately(BlockType block, int x, int y);
	/** Place the block immediately using survival rules, draining block's or player's inventory*/
	public default void placeImmediately(BlockType block, Point p) {placeImmediately(block, p.x, p.y);}
	
	/** Place the block immediately using creative rules*/
	public void placeImmediatelyCreative(BlockType block, int x, int y);
	/** Place the block immediately using creative rules*/
	public default void placeImmediatelyCreative(BlockType block, Point p) {placeImmediatelyCreative(block, p.x, p.y);}
	
	/** Place the block immediately using survival rules, draining specific inventory*/
	public void placeImmediately(BlockType block, int x, int y, Inventory inv);
	/** Place the block immediately using survival rules, draining specific inventory*/
	public default void placeImmediately(BlockType block, Point p, Inventory inv) {placeImmediately(block, p.x, p.y, inv);}
	
	//Intermediate setters
	/** Place the block when map updates using survival rules, draining block's or player's inventory*/
	public void place(BlockType block, int x, int y);
	/** Place the block when map updates using survival rules, draining block's or player's inventory*/
	default public void place(BlockType block, Point p) {place(block, p.x, p.y);}
	
	/** Place the block when map updates using creative rules*/
	public void placeCreative(BlockType block, int x, int y);
	/** Place the block when map updates using creative rules*/
	default public void placeCreative(BlockType block, Point p) {placeCreative(block, p.x, p.y);}
	
	/** Place the block when map updates using survival rules, draining specific inventory*/
	public void place(BlockType block, int x, int y, Inventory inv);
	/** Place the block when map updates using survival rules, draining specific inventory*/
	public default void place(BlockType block, Point p, Inventory inv) {place(block, p.x, p.y, inv);}
	
	//Getters
	/** Get a block in specific location.
	 * @throws ArrayIndexOutOfBoundsException if given location is out of bounds
	 */
	public BlockEntry get(int x, int y);
	/** Get a block in specific location.
	 * @throws ArrayIndexOutOfBoundsException if given location is out of bounds
	 */
	public default BlockEntry get(Point p) {return get(p.x, p.y);}
	public default BlockEntry getAtSide(int x, int y, Side s) {
		return get(s.offset(x, y));
	}
	
	/** Checks if given location is in bounds */
	public boolean inBounds(int x, int y);
	/** Checks if given location is in bounds */
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
	
	/** Run given action when map proxy is closed */
	public void later(Runnable r);
	
	/** @return associated block map*/
	public World getMap();
	
	//Visuals
	public void add(Visual vis);
	public default void adds(Visual... vis) {
		adds(Arrays.asList(vis));
	}
	public void adds(Collection<Visual> vis);
	public void remove(Visual vis);
	public default void removes(Visual... vis) {
		removes(Arrays.asList(vis));
	}
	public void removes(Collection<Visual> vis);
}
