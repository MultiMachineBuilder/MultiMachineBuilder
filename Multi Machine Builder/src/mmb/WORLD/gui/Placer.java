/**
 * 
 */
package mmb.WORLD.gui;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.Icon;

import mmb.BEANS.Titled;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.worlds.world.BlockArrayProvider;
import mmb.WORLD.worlds.world.BlockMap;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public interface Placer extends Titled {
	public Icon getIcon();
	public default void place(int x, int y, BlockArrayProvider that) {
		place(x, y, that.getOwner());
	}
	public void place(int x, int y, World that);

	public void openGUI(WorldWindow window);
	public void closeGUI(WorldWindow window);
	
	public void preview(Graphics g, Point renderStartPos, BlockMap map, Point targetLocation);
	
	@FunctionalInterface
	/**
	 * This is a functional interface for preview generation
	 * @author oskar
	 */
	public static interface Previewer{
		public void draw(Graphics g, Point renderStartPos, BlockMap map, Point targetLocation);
	}
}
