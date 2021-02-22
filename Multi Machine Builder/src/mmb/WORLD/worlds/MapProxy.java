/**
 * 
 */
package mmb.WORLD.worlds;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.inventory.Inventory;

/**
 * @author oskar
 *
 */
public interface MapProxy extends AutoCloseable{
	
	//Immediate setters
	/** Place the block immediately using survival rules, draining block's or player's inventory*/
	public void placeImmediately(BlockType block, int x, int y);
	/** Place the block immediately using survival rules, draining block's or player's inventory*/
	default public void placeImmediately(BlockType block, Point p) {placeImmediately(block, p.x, p.y);}
	
	/** Place the block immediately using creative rules*/
	public void placeImmediatelyCreative(BlockType block, int x, int y);
	/** Place the block immediately using creative rules*/
	default public void placeImmediatelyCreative(BlockType block, Point p) {placeImmediatelyCreative(block, p.x, p.y);}
	
	/** Place the block immediately using survival rules, draining specific inventory*/
	public void placeImmediately(BlockType block, int x, int y, Inventory inv);
	/** Place the block immediately using survival rules, draining specific inventory*/
	default public void placeImmediately(BlockType block, Point p, Inventory inv) {placeImmediately(block, p.x, p.y, inv);}
	
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
	default public void place(BlockType block, Point p, Inventory inv) {place(block, p.x, p.y, inv);}
	
	//Getters
	/** Get a block in specific location.
	 * @throws ArrayIndexOutOfBoundsException if given location is out of bounds
	 */
	public BlockEntry get(int x, int y);
	/** Get a block in specific location.
	 * @throws ArrayIndexOutOfBoundsException if given location is out of bounds
	 */
	default public BlockEntry get(Point p) {return get(p.x, p.y);}
	
	/** Checks if given location is in bounds */
	public boolean inBounds(int x, int y);
	/** Checks if given location is in bounds */
	default public boolean inBounds(Point p) {return inBounds(p.x, p.y);}
	
	/** Get bounds */
	default public Rectangle getBounds() {
		return new Rectangle(new Point(), getSize());
	}
	/** Get dimensions */
	public Dimension getSize();
	
	public void close();
	
	/** Run given action when map proxy is closed */
	public void later(Runnable r);
}
