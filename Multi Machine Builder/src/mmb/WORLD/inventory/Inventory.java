/**
 * 
 */
package mmb.WORLD.inventory;

import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import mmb.WORLD.item.ItemType;

/**
 * @author oskar
 *
 */
public interface Inventory{
	@Nullable public ItemStack withdraw(ItemEntry slot);
	@Nullable public ItemStack withdraw(ItemEntry slot, int amount);
	public default void withdraw(int amount, Queue<ItemStack> q) {
		withdraw(amount, q, s -> true);
	}
	@Nullable public void withdraw(int amount, Queue<ItemStack> q, Predicate<ItemEntry> filter);
	public void put(Queue<ItemStack> q);
	public void putdel(Queue<ItemStack> q);
	@Nullable default ItemStack withdraw(ItemStack ent) {
		return withdraw(ent.item, ent.amount);
	}
	@Nullable public ItemStack withdrawByType(ItemType type);
	@Nullable public ItemStack withdrawByType(ItemType type, int amount);
	/**
	 * @param item
	 * @param amount
	 * @return amount of items actually stored in the inventory
	 */
	public int put(ItemEntry item, int amount);
	default ItemStack put(ItemStack item) {return new ItemStack(item.item, put(item.item, item.amount));}
	
	/**
	 * @return inventory's capacity in cubic meters
	 */
	public double capacity();
	/**
	 * Set given inventory's capacity
	 * @param capacity new capacity
	 * @throws IllegalArgumentException if capacity < 0
	 */
	public void setCapacity(double capacity);
	/**
	 * @param item item type to check
	 * @return is given item type present
	 */
	public boolean contains(ItemEntry item);
	public Set<ItemStack> contents();
	public boolean isEmpty();
	public double volume();
	
	
}
