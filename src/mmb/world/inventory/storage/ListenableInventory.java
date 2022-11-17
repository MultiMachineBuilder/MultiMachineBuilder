/**
 * 
 */
package mmb.world.inventory.storage;

import com.pploder.events.Event;

import mmb.world.inventory.Inventory;
import mmb.world.inventory.ItemStack;
import mmb.world.items.ItemEntry;
import rx.Observable;

/**
 * @author oskar
 * Provides listenability for inventories
 */
public interface ListenableInventory extends Inventory{
	/**
	 * @return an event publisher for item insertions
	 */
	public Event<ItemStack> additions();
	/**
	 * @return an event publisher for item removals
	 */
	public Event<ItemStack> removals();
	/**
	 * @return an event publisher for quantity updates
	 */
	public Event<ItemStack> quantityChanged();
	/**
	 * @return an event publisher for insertion of new items
	 */
	public Event<ItemEntry> newItems();
	/**
	 * @return an event publisher for removal of last unit of the old item
	 */
	public Event<ItemEntry> removedItems();
}