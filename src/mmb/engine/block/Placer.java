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
 * @author oskar
 *
 */
public interface Placer extends Titled {
	public Icon getIcon();

	/**
	 * @param x X coordinate of the block
	 * @param y Y coordinate of the block
	 * @param that world, which contains the block
	 * @return placed block, or null if placement failed
	 */
	public BlockEntry place(int x, int y, World that);

	public void openGUI(WorldWindow window);
	public void closeGUI(WorldWindow window);
	
	public void preview(Graphics g, Point renderStartPos, World map, Point targetLocation, int side);
	
	@FunctionalInterface
	/**
	 * This is a functional interface for preview generation
	 * @author oskar
	 */
	public static interface Previewer{
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
