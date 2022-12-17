/**
 * 
 */
package mmb.engine.visuals;

import java.awt.Graphics;

import com.github.davidmoten.rtree2.geometry.Geometry;

import mmbbase.menu.world.window.WorldFrame;

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
