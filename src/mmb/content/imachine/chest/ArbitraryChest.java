/**
 * 
 */
package mmb.content.imachine.chest;

import mmb.engine.inv.Inventory;
import mmbbase.beans.Colorable;

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
