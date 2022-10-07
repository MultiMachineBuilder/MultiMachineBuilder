/**
 * 
 */
package mmb.world.blocks.chest;

import mmb.beans.Colorable;
import mmb.world.inventory.Inventory;

/**
 * @author oskar
 *
 */
public interface ArbitraryChest extends Colorable {
	/**
	 * @return the inventory of a chest
	 */
	public Inventory inv();
}
