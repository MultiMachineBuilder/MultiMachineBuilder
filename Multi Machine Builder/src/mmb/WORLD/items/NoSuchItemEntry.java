/**
 * 
 */
package mmb.WORLD.items;

import mmb.WORLD.item.ItemType;

/**
 * @author oskar
 *
 */
public class NoSuchItemEntry implements ItemEntry {
	public static final NoSuchItemEntry INSTANCE = new NoSuchItemEntry();
	private NoSuchItemEntry() {}
	@Override
	public double volume() {
		return 0;
	}

	@Override
	public ItemEntry itemClone() {
		return this;
	}

	@Override
	public ItemType type() {
		return NoSuchItemType.INSTANCE;
	}
	@Override
	public boolean exists() {
		return false;
	}

}
