/**
 * 
 */
package mmbgame.imachine.chest;

import mmb.beans.Colorable;
import mmbeng.inv.Inventory;

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
