/**
 * 
 */
package mmb.WORLD.inventory;

import mmb.WORLD.inventory.items.Items;

/**
 * @author oskar
 *
 */
public class CreativeWithdrawalInventory extends Inventory {

	public ItemType[] itemBuffer = Items.getItems();
	@Override
	public int getMaxSlots() {
		return itemBuffer.length;
	}

	@Override
	public int slotsInUse() {
		return itemBuffer.length;
	}

	@Override
	public void setContents(Item[] itms) {}

	@Override
	public void setContents(ItemStack[] itms) {}

	@Override
	public void ensureCapacity(int capacity) {}

	@Override
	public boolean isStackable() {
		return true;
	}

	@Override
	public void setLimited(int limit) {}

	@Override
	public int insert(Item itm, int amount) {
		return 0;
	}

	@Override
	public ItemStack[] getContents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack withdraw(int slot, int amount) {
		return new ItemStack(itemBuffer[slot], amount);
	}

	@Override
	public boolean withdraw(Item itm) {
		return true;
	}

	@Override
	public int withdraw(Item itm, int amount) {
		return amount;
	}

	@Override
	public Item withdraw(int slot) {
		return itemBuffer[slot];
	}

	@Override
	public boolean contains(Item itm) {
		return true;
	}


}
