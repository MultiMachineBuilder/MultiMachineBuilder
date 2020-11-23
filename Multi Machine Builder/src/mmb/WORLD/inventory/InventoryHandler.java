/**
 * 
 */
package mmb.WORLD.inventory;

import java.util.Arrays;

/**
 * @author oskar
 *
 */
public interface InventoryHandler {
	public void setCapacity(double capacity);
	public double getCapacity();
	public int getMaxSlots();
	public int slotsInUse();
	public void setContents(Item[] itms);
	public void setContents(ItemStack[] itms);
	public void ensureCapacity(int capacity);
	public boolean isStackable();
	/**
	 * If input is less than 0, the,
	 * @param limit
	 */
	public void setLimited(int limit);
	public int insert(Item itm, int amount);
	public ItemStack withdraw(int slot, int amount);
	public boolean contains(Item itm);
}
