/**
 * 
 */
package mmb.WORLD.inventory;

import mmb.WORLD.item.Item;

/**
 * @author oskar
 * An inventory which stores only one item type
 */
public class SingularInventory implements Inventory{
	private ItemStack stack;
	@Override
	public ItemToken retrieve(ItemEntry item) {
		if(stack.equalsType(item)) {
			return stack.r
		}
		return null;
	}
	@Override
	public boolean contains(ItemEntry item) {
		return stack.item.equals(item);
	}
	@Override
	public ItemStack withdraw(ItemEntry slot) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ItemStack withdraw(ItemEntry slot, int amount) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int put(ItemEntry item, int amount) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public double capacity() {
		// TODO Auto-generated method stub
		return 0;
	}
}
