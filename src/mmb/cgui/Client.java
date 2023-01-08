/**
 * 
 */
package mmb.cgui;

import mmb.menu.wtool.ToolSelectionModel;

/**
 * A Client GUI-compliant player implementation
 * @author oskar
 * 
 */
public interface Client {
	/**
	 * @return the tool selection
	 */
	public ToolSelectionModel getSelector();
}
