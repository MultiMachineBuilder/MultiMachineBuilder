/**
 * 
 */
package mmbgame.imachine;

import mmb.beans.Resizable;
import mmb.cgui.DestroyTab;
import mmbeng.inv.Inventory;

/**
 * @author oskar
 */
public interface ToItemUnifiedCollector extends Resizable, DestroyTab{
	public Inventory inv();
}
