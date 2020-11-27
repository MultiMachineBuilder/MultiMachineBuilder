/**
 * 
 */
package mmb.RUNTIME.actions;

import java.awt.Graphics;

import mmb.WORLD.tileworld.TileGUI;

/**
 * @author oskar
 *
 */
@FunctionalInterface
public interface WorldRenderAction {
	public void render(Graphics g, TileGUI gui);
}
