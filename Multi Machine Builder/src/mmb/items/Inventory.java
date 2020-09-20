/**
 * 
 */
package mmb.items;

import java.io.Closeable;

/**
 * @author oskar
 *
 */
public interface Inventory extends Closeable {
	public boolean isVolatile();
	/**
	 * Inserts item into inventory
	 * @param input item to be inserted
	 * @return items inserted
	 */
	public Item insert(Item input);
	public ItemStack insert(ItemStack input);
	public ItemStack extract(Item target, int amount);
	public ItemStack extract(int slot, int amount);
	public ItemStack[] contents();
}
