package mmb.inventory2;

import mmb.engine.item.ItemEntry;

/**
 * Handles events from item handlers
 */
@FunctionalInterface
public interface ItemListener {
	/**
	 * Called when contents of an inventory change
	 * @param inventory subject item handler
	 * @param item modified item stack type
	 * @param before amount before operation, 0 for newly added stacks
	 * @param after amount after operation, 0 for newly deleted stacks
	 */
	public void stackModified(ItemHandler inventory, ItemEntry item, int before, int after);
}
