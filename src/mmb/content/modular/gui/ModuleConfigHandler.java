/**
 * 
 */
package mmb.content.modular.gui;

import java.awt.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.engine.debug.Debugger;
import mmb.engine.item.ItemEntry;
import mmbbase.menu.world.inv.InventoryController;

/**
 * Handles the module GUIs
 * @author oskar
 * @param <Telement> type of block modules
 * @param <Tgui> The type of components used by the config handler
 */
public interface ModuleConfigHandler<Telement, Tgui extends Component&SafeCloseable> {
	/**
	 * Creates a new component to be placed by module editor
	 * @param invctrl inventory controller to take selected items from
	 * @return a new editor component
	 */
	@Nonnull public Tgui newComponent(InventoryController invctrl);
	/**
	 * Creates a replacement element using settings from GUI
	 * @param gui GUI to load new setting from
	 * @param oldElement old element to modify from (used to retain items after reconfig)
	 * @throws Exception when rejecting a proposition
	 * @return a new element
	 */
	@Nonnull public Telement elementFromGUI(Tgui gui, Telement oldElement) throws Exception;
	/**
	 * Loads a GUI from an element
	 * @param element
	 * @param gui
	 */
	public void loadGUI(Telement element, Tgui gui);
	/**
	 * Creates a replacement element using an upgrade
	 * @param element oldElement old element to modify from (used to retain items after reconfig)
	 * @param upgrade new upgrade to reset to
	 * @return element with replaced upgrades, or null if unsupported
	 */
	@Nullable public Telement replaceUpgradesWithinItem(Telement element, @Nullable ItemEntry upgrade);
	/**
	 * Gets upgrades from an element
	 * @param element element to check
	 * @return upgrades for an element, or null if none
	 */
	@Nullable public ItemEntry upgrades(Telement element);	
	/**
	 * Creates a new element and handles exception
	 * @param gui GUI to load from
	 * @param oldElement old element
	 * @param debug debugger to handle errors
	 * @param msg error message
	 * @return a new element, or null if error occured
	 */
	@Nullable public default Telement elementCreate(Tgui gui, Telement oldElement, Debugger debug, String msg) {
		try {
			return elementFromGUI(gui, oldElement);
		} catch (Exception e) {
			debug.pstm(e, msg);
			return null;
		}
	}
}
