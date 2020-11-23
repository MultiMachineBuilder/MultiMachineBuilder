/**
 * 
 */
package mmb.WORLD.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.google.common.collect.Lists;

import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public abstract class Inventory implements InventoryHandler{
	static Debugger debug = new Debugger("Inventories");
	public double capacity = 1;
	protected boolean isLimited;
	protected double usedVolume;
	
	public boolean insert(Item itm) {
		return insert(itm, 1) == 1;
	}

	/**
	 * @return the capacity
	 */
	@Override
	public double getCapacity() {
		return capacity;
	}

	/**
	 * @param capacity the capacity to set
	 */
	@Override
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public int insert(ItemStack itms) {
		if(itms == null) return 0;
		return insert(itms.item, itms.amount);
	}
	public boolean moveFrom(ItemStack itms) {
		return moveFrom(itms, 1) == 1;
	}
	public int moveFrom(ItemStack itms, int amount) {
		int x = insert(itms.item, amount);
		itms.amount -= x;
		return x;
	}
	public int empty(ItemStack itms) {
		return moveFrom(itms, itms.amount);
	}
	abstract public ItemStack[] getContents();

	public boolean getLimited() {
		return isLimited;
	}
	public double leftoverVolume() {
		return capacity - usedVolume();
	}
	public double usedVolume() {
		return usedVolume;
	}
	public void calcUsedVolume() {
		usedVolume = 0;
		for(ItemStack itms: getContents()) {
			usedVolume += itms.volume();
		}
	}
	
	public boolean withdraw(Item itm) {
		if(itm == null) return false;
		return withdraw(itm, 1) == 1;
	}
	public int withdraw(Item itm, int amount) {
		if(itm == null) return 0;
		ItemStack[] is = getContents();
		int j = 0;
		for(int i = 0; i < is.length; i++) {
			if(is[i].item == itm) {
				j += withdraw(i, amount).amount;
			}
		}
		return j;
	}
	public Item withdraw(int slot) {
		ItemStack withdraw = withdraw(slot, 1);
		if(withdraw.amount == 1) return withdraw.item;
		return null;
	}
}
