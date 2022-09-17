/**
 * 
 */
package mmb.WORLD.inventory.storage;

import com.pploder.events.Event;

import mmb.CatchingEvent;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.items.ItemEntry;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class ListenableSimpleInventory extends SimpleInventory implements ListenableInventory {
	public ListenableSimpleInventory(Debugger debug) {
		super();
		additions = new CatchingEvent<>(debug, "Failed to process addition event");
		removals = new CatchingEvent<>(debug, "Failed to process removal event");
		quantityChange = new CatchingEvent<>(debug, "Failed to process update event");
		newItems = new CatchingEvent<>(debug, "Failed to process new item event");
		removedItems = new CatchingEvent<>(debug, "Failed to process item removal event");
	}

	public ListenableSimpleInventory(Debugger debug, Inventory inv) {
		super(inv);
		additions = new CatchingEvent<>(debug, "Failed to process addition event");
		removals = new CatchingEvent<>(debug, "Failed to process removal event");
		quantityChange = new CatchingEvent<>(debug, "Failed to process update event");
		newItems = new CatchingEvent<>(debug, "Failed to process new item event");
		removedItems = new CatchingEvent<>(debug, "Failed to process item removal event");
	}
	
	private final Event<ItemStack> additions;
	private final Event<ItemStack> removals;
	private final Event<ItemStack> quantityChange;
	private final Event<ItemEntry> newItems;
	private final Event<ItemEntry> removedItems;
	@Override
	public Event<ItemStack> additions() {return additions;}
	@Override
	public Event<ItemStack> removals() {return removals;}
	@Override
	public Event<ItemStack> quantityChanged() {return quantityChange;}
	@Override
	public Event<ItemEntry> newItems() {return newItems;}
	@Override
	public Event<ItemEntry> removedItems() {return removedItems;}
	
	@Override
	protected void OV_insert(ItemStack stk) {additions.trigger(stk);}
	@Override
	protected void OV_extract(ItemStack stk) {removals.trigger(stk);}
	@Override
	protected void OV_update(ItemStack stk) {quantityChange.trigger(stk);}
	@Override
	protected void OV_remove(ItemEntry item) {newItems.trigger(item);}
	@Override
	protected void OV_add(ItemEntry item) {removedItems.trigger(item);}
	
}
