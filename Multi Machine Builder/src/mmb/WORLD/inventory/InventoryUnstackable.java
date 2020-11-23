/**
 * 
 */
package mmb.WORLD.inventory;

import java.util.Arrays;
import java.util.List;

import mmb.DATA.LimitedArrayList;

/**
 * @author oskar
 *
 */
public class InventoryUnstackable extends Inventory{


	/**
	 * The list contains items
	 */
	protected LimitedArrayList<Item> items = new LimitedArrayList<Item>();
	@Override
	public int insert(Item itm, int amount) {
		int fits = ItemStack.fitsInto(itm, amount, leftoverVolume());
		if(fits > items.maxAmount) fits = items.maxAmount;
		for(int i = 0; i < fits; i++) {
			insert(itm);
		}
		return fits;
	}

	@Override
	public boolean insert(Item itm) {
		if(itm == null) return false;
		calcUsedVolume();
		if(itm.getVolume() > leftoverVolume()) return false;
		return items.add(itm);
	}

	@Override
	public ItemStack[] getContents() {
		ItemStack[] results = new ItemStack[items.size()];
		for(int i = 0; i < results.length; i++) {
			results[i]=new ItemStack(items.get(i), 1);
		}
		return results;
	}

	@Override
	public void setLimited(int limit) {
		int x = Integer.MAX_VALUE;
		if(limit >= 0) x = limit;
		items = new LimitedArrayList<Item>(items, x);
	}

	@Override
	public void setContents(Item[] itms) {
		ensureCapacity(itms.length);
		items = new LimitedArrayList<Item>(Arrays.asList(itms), items.maxAmount);
	}

	@Override
	public void setContents(ItemStack[] itms) {
		Item[] result = new Item[itms.length];
		for(int i = 0; i < itms.length; i++) {
			result[i] = itms[i].item;
		}
		setContents(result);
	}

	@Override
	public void ensureCapacity(int capacity) {
		if(capacity < 0) setLimited(capacity);
		items.ensureCapacity(capacity);
	}

	@Override
	public int getMaxSlots() {
		return items.maxAmount;
	}

	@Override
	public int slotsInUse() {
		return items.size();
	}

	@Override
	public boolean isStackable() {
		return false;
	}

	@Override
	public ItemStack withdraw(int slot, int amount) {
		if(amount == 0) return null;
		Item result = withdraw(slot);
		if(result == null) return null;
		return new ItemStack(result, 1);
	}

	@Override
	public boolean withdraw(Item itm) {
		if(itm == null) return false;
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i) == itm) {
				items.remove(i);
				return true;
			}
		}
		return false;
	}

	@Override
	public int withdraw(Item itm, int amount) {
		if(itm == null) return 0;
		int j = amount;
		for(int i = 0; (i < items.size()) && j > 0; i++) {
			if(items.get(i) == itm) {
				items.remove(i);
				j--;
			}
		}
		return amount - j;
	}

	@Override
	public Item withdraw(int slot) {
		return items.remove(slot);
	}

	@Override
	public boolean contains(Item itm) {
		return items.contains(itm);
	}
}
