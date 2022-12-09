/**
 * 
 */
package mmbbase.menu.world.window;

import javax.swing.JPanel;

import mmb.content.modular.gui.SafeCloseable;

/**
 * @author oskar
 * A class for MultiMachineBuilder GUI tabs
 */
public abstract class GUITab extends JPanel{
	private static final long serialVersionUID = -5854934130210203539L;

	/**
	 * Invoked when GUI is closed
	 * @param window world window which contains the tab
	 */
	public abstract void close(WorldWindow window);
}
