/**
 * 
 */
package mmb.WORLD.inventory;

import mmb.WORLD.item.ItemType;

/**
 * @author oskar
 * An inventory which allows infinite item withdrawal
 */
public class CreativeWithdrawalInventory implements Inventory{

	@Override
	public ItemToken retrieve(ItemType item) {
		return new CreativeWithdrawalItemToken(item);
	}

	@Override
	public boolean contains(ItemType item) {
		return true;
	}

	@Override
	public ItemEntry withdraw(ItemType slot) {
		return new ItemEntry(slot, 1);
	}

	@Override
	public ItemEntry withdraw(ItemType slot, int amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemEntry put(ItemType item, int amount) {
		// TODO Auto-generated method stub
		return null;
	}

}
