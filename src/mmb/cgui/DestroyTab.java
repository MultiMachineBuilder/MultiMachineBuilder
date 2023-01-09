/**
 * 
 */
package mmb.cgui;

import mmb.menu.world.window.GUITab;

/**
 * Allows shared GUI tabs to reset a block for next use
 * @author oskar
 */
public interface DestroyTab {
	/**
	 * @param filterGUI used for verification
	 * @throws IllegalStateException when the given GUi is wrong
	 */
	public void destroyTab(GUITab filterGUI);
}
