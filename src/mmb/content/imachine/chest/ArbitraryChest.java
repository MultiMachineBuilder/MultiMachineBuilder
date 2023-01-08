/**
 * 
 */
package mmb.content.imachine.chest;

import mmb.beans.Colorable;
import mmb.engine.inv.Inventory;

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
