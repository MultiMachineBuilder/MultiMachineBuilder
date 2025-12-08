/**
 * 
 */
package mmb.content.modular.gui;

import java.awt.Component;

import mmb.annotations.NN;
import mmb.menu.world.inv.InventoryController;

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
	 * @param element source part
	 * @return a new editor component
	 */
	@NN public Tgui newComponent(InventoryController invctrl, Telement element);
}
