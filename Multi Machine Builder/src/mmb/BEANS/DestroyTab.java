/**
 * 
 */
package mmb.BEANS;

import mmb.WORLD.gui.machine.FilterGUI;
import mmb.WORLD.gui.window.GUITab;

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
