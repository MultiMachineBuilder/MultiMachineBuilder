/**
 * 
 */
package mmb.world.modulars.gui;

import java.awt.Component;

/**
 * Handles the module GUIs
 * @author oskar
 * @param <Telement> type of block modules
 * @param <Tgui> The type of components used by the config handler
 */
public interface ModuleConfigHandler<Telement, Tgui extends Component> {
	/**
	 * Creates a new component to be placed by module editor
	 * @return a new editor component
	 */
	public Tgui newComponent();
	/**
	 * Creates a replacement element using settings from GUI
	 * @param gui GUI to load new setting from
	 * @param oldElement old element to modify from (used to retain items after reconfig)
	 * @throws Exception when rejecting a proposition
	 * @return a new element
	 */
	public Telement elementFromGUI(Tgui gui, Telement oldElement) throws Exception;
	/**
	 * Loads a GUI from an element
	 * @param element
	 * @param gui
	 */
	public void loadGUI(Telement element, Tgui gui);
}
