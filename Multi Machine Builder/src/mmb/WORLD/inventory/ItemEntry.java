/**
 * 
 */
package mmb.WORLD.inventory;

import mmb.WORLD.item.ItemType;

/**
 * @author oskar
 * An item entry is intended for use in inventories.
 */
public class ItemEntry {
	/**
	 * An item stored in the inventory
	 */
	public ItemType item;
	/**
	 * Number of items in the entry
	 */
	public int amount;
	
	public double volume() {
		return item.volume * amount;
	}
}
