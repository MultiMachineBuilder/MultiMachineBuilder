/**
 * 
 */
package mmb.WORLD.gui;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JDialog;

import mmb.WORLD.inventory.CreativeWithdrawalInventory;
import mmb.WORLD.inventory.Inventory;
import net.miginfocom.swing.MigLayout;

/**
 * @author oskar
 *
 */
public class InventoryDialog extends JDialog {
	private static final CreativeWithdrawalInventory cwi = new CreativeWithdrawalInventory();
	private boolean isCreative;
	/**
	 * @return the isCreative
	 */
	public boolean isCreative() {
		return isCreative;
	}
	/**
	 * @param isCreative the isCreative to set
	 */
	public void setCreative(boolean isCreative) {
		this.isCreative = isCreative;
		if(isCreative) {
			
		}else {
			
		}
	}

	public final Set<Inventory> invs;
	public InventoryDialog(Inventory... inventories) {
		invs = new HashSet<>(Arrays.asList(inventories));
		initialize();
	}
	public InventoryDialog(Collection<Inventory> inventories) {
		invs = new HashSet<>(inventories);
		initialize();
	}
	/**
	 * Create the dialog.
	 */
	public InventoryDialog() {
		invs = new HashSet<>();
		initialize();
	}
	private void initialize() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new MigLayout("", "[]", "[]"));
	}
}
