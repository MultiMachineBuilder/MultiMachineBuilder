/**
 * 
 */
package mmb.WORLD.tileworld.tool;

import mmb.WORLD.tileworld.BlockDrawer;
import mmb.WORLD.tileworld.TileGUI;

/**
 * @author oskar
 *
 */
public interface BlockTool {
	
	default String name() {
		return getClass().getName();
	}
	
	void setProxy(ToolProxy proxy);
	/**
	 * Update the tool on every frame.
	 * @param e event data
	 */
	default void update(ToolEvent e) {}
	
	/**
	 * Used when mouse pressed
	 * @param e event data
	 * @param button mouse button
	 */
	default void mousePress(ToolEvent e, int button) {}
	default void mouseDrag(ToolEvent e) {}
	default void mouseRelease(ToolEvent e, int button) {}
	default void set(TileGUI gui) {}
	default void reset() {}
	default void mouseMoved(ToolEvent e) {};
	
	BlockDrawer texture();
}
