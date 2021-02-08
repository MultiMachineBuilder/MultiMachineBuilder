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
	
	/**
	 * @param item item type in this stack
	 * @param amount number of items in this stack
	 */
	public ItemEntry(ItemType item, int amount) {
		super();
		this.item = item;
		this.amount = amount;
	}

	/**
	 * @return the volume of item entry
	 */
	public double volume() {
		return item.volume * amount;
	}
}
