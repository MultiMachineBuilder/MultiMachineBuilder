/**
 * 
 */
package mmb.beans;

import mmb.cgui.DestroyTab;
import mmb.world.inventory.Inventory;

/**
 * @author oskar
 */
public interface ToItemUnifiedCollector extends Resizable, DestroyTab{
	public Inventory inv();
}
