/**
 * 
 */
package mmb.WORLD.blocks.chest;

import mmb.BEANS.Colorable;
import mmb.WORLD.inventory.Inventory;

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
