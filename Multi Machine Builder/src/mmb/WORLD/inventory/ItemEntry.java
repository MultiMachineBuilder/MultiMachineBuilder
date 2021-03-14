/**
 * 
 */
package mmb.WORLD.inventory;

import mmb.WORLD.item.ItemType;

/**
 * @author oskar
 * An item entry representing a single unit of item
 */
public interface ItemEntry {

	/**
	 * @return the volume of single given {@code ItemEntry}
	 */
	public double volume();
	public default double volume(int amount) {
		return volume() * amount;
	}
	/**
	 * Clone this {@code ItemEntry}
	 * @return a copy of given ItemEntry
	 */
	public ItemEntry itemClone();
	public ItemType type();
	default public boolean exists() {
		return true;
	}
}
