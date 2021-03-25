/**
 * 
 */
package mmb.WORLD.worlds.world;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.Side;
import mmb.WORLD.block.BlockEntity;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;

/**
 * @author oskar
 *
 */
public interface BlockArrayProvider {
	/**
	 * Gets block at given location
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return a block at given location, or null if absent
	 * @throws MapCoordinatesOutOfBoundsException if the coordinates are out of bounds
	 */
	@Nullable public BlockEntry get(int x, int y);
	/**
	 * Places a block of given type
	 * @param block a block entry to place
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return a new block entry, or null if placement failed
	 */

	@Nullable public BlockEntry set(BlockEntry block, int x, int y);
	/**
	 * Places a block of given type
	 * @param block a block type to place
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return a new block entry, or null if placement failed
	 */

	@Nullable public BlockEntry place(BlockType block, int x, int y);
	/**
	 * @return an immutable set view of all block entities.
	 */
	public Set<BlockEntity> getBlockEntities();
	/** @return this map's leftmost X coordinate */
	public int startX();
	/** @return this map's uppermost Y coordinate */
	public int startY();
	/** @return this map's width */
	public int sizeX();
	/** @return this map's height */
	public int sizeY();
	/** @return this map's UL corner in form of a new {@link Point} */
	@Nonnull default public Point start() {
		return new Point(startX(), startY());
	}
	/** @return this map's size in form of a new {@link Dimension} */
	@Nonnull default public Dimension size() {
		return new Dimension(sizeX(), sizeY());
	}
	/** @return this map's bounds in form of a new {@link Rectangle} */
	@Nonnull default public Rectangle bounds() {
		return new Rectangle(startX(), startY(), sizeX(), sizeY());
	}
	/** @return the parent {@link World} of this map, or null if absent*/
	@Nullable public World parent();
	/** @return is given map usable? */
	public boolean isValid();
	/**
	 * Checks if given point is located inside the bounds
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return is given point in bounds?
	 */
	public boolean inBounds(int x, int y);
	/**
	 * @param s side, from which to get
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return the block entry at given location from side
	 */
	public default BlockEntry getAtSide(Side s, int x, int y) {
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
	/**
	 * Left click the given block
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return did click pass through?
	 */
	public default boolean click(int x, int y) {
		BlockEntry ent = get(x, y);
		if(ent == null) return false;
		boolean result = ent instanceof BlockActivateListener;
		if(result) {
			((BlockActivateListener) ent).click(x, y, parent(), null);
		}
		return result;
	}
	/**
	 * @return this map's owner
	 */
	@Nonnull public World getOwner();
}
