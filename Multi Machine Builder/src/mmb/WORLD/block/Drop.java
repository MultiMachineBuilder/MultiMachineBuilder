/**
 * 
 */
package mmb.WORLD.block;

import javax.annotation.Nullable;

import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 * Represents a dropped item
 */
@FunctionalInterface
public interface Drop {
	/**
	 * Drops an item
	 * @param inv inventory, to which item will be dropped. If it is null, the items are dropped into world
	 * @param map world
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return Were items dropped?
	 */
	public boolean drop(@Nullable InventoryWriter inv, World map, int x, int y);

	public static boolean tryDrop(ItemEntry ent, @Nullable InventoryWriter i, World map, int x, int y) {
		int inserted = 0;
		if(i != null) inserted = i.write(ent);
		if(inserted == 0) map.dropItem(ent, x, y);
		return true;
	}
}
