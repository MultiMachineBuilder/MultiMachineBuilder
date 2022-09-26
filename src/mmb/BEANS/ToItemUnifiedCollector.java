/**
 * 
 */
package mmb.BEANS;

import mmb.WORLD.inventory.Inventory;

/**
 * @author oskar
 *
 */
public interface ToItemUnifiedCollector extends Resizable, DestroyTab{
	public Inventory inv();
}
