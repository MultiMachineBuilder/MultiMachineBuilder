/**
 * 
 */
package mmb.BEANS;

import javax.annotation.Nullable;

import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.worlds.world.World;

/**
 * This interface provides ability for blocks to perform an action or open a GUI when clicked
 * @author oskar
 */
@FunctionalInterface
public interface BlockActivateListener {
	/**
	 * Invoked when this block is clicked
	 * @param blockX X coordinate of the block
	 * @param blockY Y coordinate of this block
	 * @param map world, in which block resides
	 * @param window window used to open this block, or null if activated by a Block Clicking Claw
	 * @param partX partial X coordinate, on this block
	 * @param partY partial Y coordinate, on this block
	 */
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY);
	/**
	 * @param blockX X coordinate of the block
	 * @param blockY Y coordinate of this block
	 * @param map world, in which block resides
	 * @param partX partial X coordinate, on this block
	 * @param partY partial Y coordinate, on this block
	 */
	public default void click(int blockX, int blockY, World map, double partX, double partY) {
		click(blockX, blockY, map, null, 0, 0);
	}
}

