/**
 * 
 */
package mmb.BEANS;

import mmb.MENU.world.machine.FilterGUI;
import mmb.MENU.world.window.GUITab;

/**
 * @author oskar
 * Allows shared GUI tabs to reset a block for next use
 */
public interface DestroyTab {
	/**
	 * @param filterGUI used for verification
	 * @throws IllegalStateException when the given GUi is wrong
	 */
	public void destroyTab(GUITab filterGUI);
}
