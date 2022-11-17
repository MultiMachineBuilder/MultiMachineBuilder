/**
 * 
 */
package mmb.beans;

import java.util.Collection;

import mmb.world.inventory.Inventory;

/**
 * @author oskar
 * Describes an object with inventories
 */
public interface InventoryProvider {
	/**
	 * @return inventories normally available to the public
	 */
	public Collection<Inventory> provideExposedInventories();
	/**
	 * @return inventories normally used only inside the object
	 */
	public Collection<Inventory> provideHiddenInventories();
}