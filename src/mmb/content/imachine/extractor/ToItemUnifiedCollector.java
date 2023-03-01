/**
 * 
 */
package mmb.content.imachine.extractor;

import mmb.beans.Resizable;
import mmb.cgui.DestroyTab;
import mmb.engine.inv.Inventory;

/**
 * An interface for item collectors
 * @author oskar
 */
public interface ToItemUnifiedCollector extends Resizable, DestroyTab{
	/** @return the target inventory for the item collector */
	public Inventory inv();
}
