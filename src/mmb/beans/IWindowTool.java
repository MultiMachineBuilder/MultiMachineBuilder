package mmb.beans;

import mmb.menu.wtool.WindowTool;

/**
 * Assigns a window tool to an item
 */
public interface IWindowTool {
	/** Gets the window tool */
	public WindowTool getWindowTool();
	/**
	 * Sets the window tool
	 * @param tool new window tool
	 * @throws NullPointerException if {@code tool} is null
	 */
	public void setWindowTool(WindowTool tool);
}
