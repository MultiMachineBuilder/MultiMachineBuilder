/**
 * 
 */
package mmbbase.cgui;

import mmbbase.menu.wtool.ToolSelectionModel;

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
