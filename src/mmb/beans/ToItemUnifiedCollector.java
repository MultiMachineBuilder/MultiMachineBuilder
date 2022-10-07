/**
 * 
 */
package mmb.beans;

import mmb.world.inventory.Inventory;

/**
 * @author oskar
 *
 */
public interface ToItemUnifiedCollector extends Resizable, DestroyTab{
	public Inventory inv();
}
