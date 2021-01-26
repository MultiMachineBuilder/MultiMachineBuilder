/**
 * 
 */
package mmb.WORLD.inventory;

import mmb.WORLD.item.ItemType;

/**
 * @author oskar
 *
 */
public interface Inventory {
	public ItemEntry withdraw(ItemType slot);
	public ItemEntry withdraw(ItemType slot, int amount);
	default ItemEntry withdraw(ItemEntry ent) {
		return withdraw(ent.item, ent.amount);
	}
	public ItemEntry put(ItemType item, int amount);
	default ItemEntry put(ItemEntry item) {return put(item.item, item.amount);}
}
