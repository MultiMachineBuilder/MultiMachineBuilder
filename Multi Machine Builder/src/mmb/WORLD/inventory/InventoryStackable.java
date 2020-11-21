/**
 * 
 */
package mmb.WORLD.inventory;

import java.util.Arrays;

import mmb.DATA.LimitedArrayList;

/**
 * @author oskar
 *
 */
public class InventoryStackable extends Inventory {

	protected LimitedArrayList<ItemStack> items = new LimitedArrayList<ItemStack>();
	@Override
	public int insert(Item itm, int amount) {
		int fits = ItemStack.fitsInto(itm, amount, capacity);
		for(int i = 0 ; i < items.size(); i++) {
			ItemStack ent = items.get(i);
			if(ent.item == itm) {
				ent.amount += fits;
				return fits;
			}
		}
		//if no slots accept given item
		if(fits == 0) return 0;
		if(items.add(new ItemStack(itm, amount))) {
			return fits;
		}
		return 0;
	}

	@Override
	public ItemStack[] getContents() {
		return (ItemStack[]) items.toArray();
	}

	@Override
	public void setLimited(int limit) {
		int x = Integer.MAX_VALUE;
		if(limit >= 0) x = limit;
		items = new LimitedArrayList<ItemStack>(items, x);
	}

	@Override
	public void setContents(Item[] itms) {
		ItemStack[] result = new ItemStack[itms.length];
		for(int i = 0; i < itms.length; i++) {
			result[i] = new ItemStack(itms[i]);
		}
		setContents(result);
	}

	@Override
	public void setContents(ItemStack[] itms) {
		ensureCapacity(itms.length);
		items = new LimitedArrayList<ItemStack>(Arrays.asList(itms), items.maxAmount);
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
		return true;
	}

}
