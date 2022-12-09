/**
 * 
 */
package mmb.cgui;

import mmb.menu.world.window.GUITab;
import mmbgame.imachine.filter.FilterGUI;

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
