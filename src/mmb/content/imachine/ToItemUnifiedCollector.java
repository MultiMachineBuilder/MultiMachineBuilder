/**
 * 
 */
package mmb.content.imachine;

import mmb.beans.Resizable;
import mmb.cgui.DestroyTab;
import mmb.engine.inv.Inventory;

/**
 * @author oskar
 */
public interface ToItemUnifiedCollector extends Resizable, DestroyTab{
	public Inventory inv();
}
