/**
 * 
 */
package mmb.WORLD.block;

import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 * Represents a dropped item
 */
public interface Drop {
	/**
	 * 
	 * @param inv inventory, to which item will be dropped
	 * @param w world
	 * @return Were items dropped?
	 */
	public boolean drop(Inventory inv, World w);
}
