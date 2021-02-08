/**
 * 
 */
package mmb.WORLD.inventory;

import mmb.WORLD.item.ItemType;

/**
 * @author oskar
 *
 */
public interface ItemStorage {
	/**
	 * Retrieve the item with given type
	 * @param item item type to retrieve
	 * @return item token to transfer and count items, or null if item is absent
	 */
	public ItemToken retrieve(ItemType item);
	
	/**
	 * @param item item type to check
	 * @return is given item type present
	 */
	public boolean contains(ItemType item);
}
