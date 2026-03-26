/**
 * 
 */
package mmb.menu.world.inv;

import java.util.List;

import mmb.inventory.Inventory;
import mmb.inventory.ItemRecord;

/**
 * An abstraction over inventory controllers
 * @author oskar
 */
public interface AbstractInventoryController {
	/** @return current inventory*/
	public Inventory getInv();
	/** Refreshes currently displayed contents */
	public void refresh();
	/** @return all selected items */
	public List<ItemRecord> getSelectedValuesList();
	/** @return selected item, or null if unselected */
	public ItemRecord getSelectedValue();
}
