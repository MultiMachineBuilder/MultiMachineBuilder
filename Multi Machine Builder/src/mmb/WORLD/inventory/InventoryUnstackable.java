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
		int j = 0;
		for(int i = 0; i < amount; i++) {
			if(insert(itm)) j++;
		}
		return j;
	}

	@Override
	public boolean insert(Item itm) {
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

}
