/**
 * 
 */
package mmb.WORLD.visuals;

import java.awt.Graphics;

import com.github.davidmoten.rtree.geometry.Geometry;

import mmb.WORLD.gui.window.WorldFrame;

/**
 * @author oskar
 *
 */
public interface Visual {
	/**
	 * @return the geometry, in which the visual object fits
	 */
	public Geometry border();
	
	/**
	 * Renders the visual object in the window
	 * @param g
	 * @param frame
	 */
	public void render(Graphics g, WorldFrame frame);
}
