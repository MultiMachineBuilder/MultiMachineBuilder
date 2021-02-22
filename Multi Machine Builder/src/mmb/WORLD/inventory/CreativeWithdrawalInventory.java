/**
 * 
 */
package mmb.WORLD.inventory;

import mmb.WORLD.item.Item;

/**
 * @author oskar
 * An inventory which allows infinite item withdrawal
 */
public class CreativeWithdrawalInventory implements Inventory{

	@Override
	public ItemToken retrieve(ItemEntry item) {
		return new CreativeWithdrawalItemToken(item);
	}

	@Override
	public boolean contains(ItemEntry item) {
		return true;
	}

	@Override
	public ItemStack withdraw(ItemEntry slot) {
		return new ItemStack(slot, 1);
	}

	@Override
	public ItemStack withdraw(ItemEntry item, int amount) {
		return new ItemStack(item, amount);
	}

	@Override
	public int put(ItemEntry item, int amount) {
		return 0;
	}
	@Override
	public double capacity() {
		return Double.POSITIVE_INFINITY;
	}
}
