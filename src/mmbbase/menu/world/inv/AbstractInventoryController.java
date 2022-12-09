/**
 * 
 */
package mmb.menu.world.inv;

import java.util.List;

import mmbeng.inv.Inventory;
import mmbeng.inv.ItemRecord;

/**
 * @author oskar
 *
 */
public interface AbstractInventoryController {
	public Inventory getInv();
	public InvType getInvType();
	public void refresh();
	/**
	 * @return all sselected items
	 */
	public List<ItemRecord> getSelectedValuesList();
	/**
	 * @return selected item, or null if unselected
	 */
	public ItemRecord getSelectedValue();
	/**
	 * @author oskar
	 * Returns the inventory control type
	 */
	public enum InvType{
		/** Indicates an inventory with support for mutiple stacks */
		SIMPLE,
		/** Indicates an inventory with support for single stack of items */
		SINGLESTACK,
		/** Indicates an inventory with support for a single item*/
		SINGLE;
	}
	
}
