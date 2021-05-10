/**
 * 
 */
package mmb.WORLD.block;

import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemEntry;
import mmb.WORLD.worlds.world.World.BlockMap;

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
	public boolean drop(Inventory inv, BlockMap map, int x, int y);

	public static boolean tryDrop(ItemEntry ent, Inventory inv, BlockMap map, int x, int y) {
		int inserted = inv.insert(ent, 1);
		if(inserted == 0) map.dropItem(ent, x, y);
		return true;
	}
}
