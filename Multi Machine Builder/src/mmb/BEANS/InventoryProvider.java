/**
 * 
 */
package mmb.BEANS;

import java.util.Collection;
import mmb.WORLD.inventory.Inventory;

/**
 * @author oskar
 *
 */
public interface InventoryProvider {
	public Collection<Inventory> provideExposedInventories();
	public Collection<Inventory> provideHiddedInventories();
}
