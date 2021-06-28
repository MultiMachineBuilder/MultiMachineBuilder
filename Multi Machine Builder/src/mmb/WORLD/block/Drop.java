/**
 * 
 */
package mmb.WORLD.block;

import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.world.BlockMap;

/**
 * @author oskar
 * Represents a dropped item
 */
@FunctionalInterface
public interface Drop {
	/**
	 * 
	 * @param inv inventory, to which item will be dropped
	 * @param map world
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return Were items dropped?
	 */
	public boolean drop(InventoryWriter inv, BlockMap map, int x, int y);

	public static boolean tryDrop(ItemEntry ent, InventoryWriter i, BlockMap map, int x, int y) {
		int inserted = i.write(ent);
		if(inserted == 0) map.dropItem(ent, x, y);
		return true;
	}
}
