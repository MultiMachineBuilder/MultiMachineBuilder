/**
 * 
 */
package mmb.engine.block;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.Icon;

import mmb.beans.Titled;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * Places blocks. Currently used only by block types
 * @author oskar
 *
 */
public interface Placer extends Titled {
	/** @return preview icon */
	public Icon getIcon();

	/**
	 * @param x X coordinate of the block
	 * @param y Y coordinate of the block
	 * @param that world, which contains the block
	 * @return placed block, or null if placement failed
	 */
	public BlockEntry place(int x, int y, World that);

	/**
	 * Invoked when selected
	 * @param window world window
	 */
	public void openGUI(WorldWindow window);
	/**
	 * Invoked when deselected
	 * @param window world window
	 */
	public void closeGUI(WorldWindow window);
	
	/**
	 * Displays a block preview in the world
	 * @param g graphics context
	 * @param renderStartPos location of the preview
	 * @param map world
	 * @param targetLocation location of the block
	 * @param side preview size
	 */
	public void preview(Graphics g, Point renderStartPos, World map, Point targetLocation, int side);
	
	@FunctionalInterface
	/**
	 * This is a functional interface for preview generation.
	 * Used only for multimachines now
	 * @author oskar
	 */
	public static interface Previewer{
		/**
		 * @param g
		 * @param renderStartPos
		 * @param map
		 * @param targetLocation
		 */
		public void draw(Graphics g, Point renderStartPos, World map, Point targetLocation);
	}

	/**
	 * @param pos position of the block
	 * @param map map, which contains the block
	 * @return placed block, or null if placement failed
	 */
	public default BlockEntry place(Point pos, World map) {
		return place(pos.x, pos.y, map);
	}
}
