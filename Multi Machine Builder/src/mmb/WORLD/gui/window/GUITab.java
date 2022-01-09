/**
 * 
 */
package mmb.WORLD.gui.window;

import javax.swing.JPanel;

/**
 * @author oskar
 * A class for MultiMachineBuilder GUI tabs
 */
public abstract class GUITab extends JPanel {
	private static final long serialVersionUID = -5854934130210203539L;

	/**
	 * Invoked when this GUI is opened
	 * @param window world window which contains the tab
	 */
	public abstract void createTab(WorldWindow window);
	/**
	 * Invoked when GUI is closed
	 * @param window world window which contains the tab
	 */
	public abstract void destroyTab(WorldWindow window);
}
