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
public abstract class Inventory {
	static Debugger debug = new Debugger("Inventories");
	public double capacity = 1;
	protected boolean isLimited;
	protected double usedVolume;
	
	public boolean insert(Item itm) {
		return insert(itm, 1) == 1;
	}
	abstract public int insert(Item itm, int amount);
	public int insert(ItemStack itms) {
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
	/**
	 * If input is less than 0, the,
	 * @param limit
	 */
	abstract public void setLimited(int limit);
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
	abstract public int getMaxSlots();
	abstract public int slotsInUse();
	abstract public void setContents(Item[] itms);
	abstract public void setContents(ItemStack[] itms);
	abstract public void ensureCapacity(int capacity);
	abstract public boolean isStackable();
}
