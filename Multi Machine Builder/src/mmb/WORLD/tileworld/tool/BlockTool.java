/**
 * 
 */
package mmb.WORLD.tileworld.tool;

import mmb.WORLD.tileworld.BlockDrawer;

/**
 * @author oskar
 *
 */
public interface BlockTool {
	
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
	
	BlockDrawer texture();
}
