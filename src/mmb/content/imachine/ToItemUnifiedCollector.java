/**
 * 
 */
package mmb.content.imachine;

import mmb.engine.inv.Inventory;
import mmbbase.beans.Resizable;
import mmbbase.cgui.DestroyTab;

/**
 * @author oskar
 */
public interface ToItemUnifiedCollector extends Resizable, DestroyTab{
	public Inventory inv();
}
