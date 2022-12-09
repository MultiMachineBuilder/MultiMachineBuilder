/**
 * 
 */
package mmbmods.stn;

import java.awt.Color;

import javax.swing.JButton;

import mmb.engine.inv.Inventory;
import mmbbase.menu.helper.MenuHelper;
import mmbbase.menu.world.inv.InventoryController;
import mmbmods.stn.network.STNNetworkInventory;

/**
 * @author oskar
 *
 */
public class STNInventoryController extends InventoryController {
	private static final long serialVersionUID = 5289202043220848886L;

	public STNInventoryController() {
		super();
		initstn();
	}

	public STNInventoryController(Inventory out) {
		super(out);
		initstn();
	}

	public STNInventoryController(InventoryController other) {
		super(other);
		initstn();
	}

	private void initstn() {
		JButton rebuild = MenuHelper.newButton("#stnm-reset", Color.orange, e -> {
			Inventory inv = getInv();
			if(inv instanceof STNNetworkInventory) {
				((STNNetworkInventory) inv).rebuild();
			}
		});
		ubox.add(rebuild);
	}
	
	

}
