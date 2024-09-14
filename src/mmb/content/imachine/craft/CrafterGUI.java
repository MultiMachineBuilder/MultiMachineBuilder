/**
 * 
 */
package mmb.content.imachine.craft;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JLabel;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Color;

import javax.swing.JProgressBar;

import mmb.NN;
import mmb.content.craft.CraftGUI;
import mmb.content.ditems.Stencil;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.item.ItemEntry;
import mmb.menu.world.inv.InventoryController;
import mmb.menu.world.inv.MoveItems;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;
import mmb.menu.world.inv.AbstractInventoryController;
import mmb.menu.world.inv.SingleInventoryController;
import mmb.engine.inv.storage.SingleItemInventory;

/**
 * A manual CrafterGUI
 * @author oskar
 *
 */
class CrafterGUI extends GUITab {
	private transient Crafter crafter;
	public CrafterGUI(Crafter crafter, WorldWindow window) {
		this.crafter = crafter;
		setLayout(new MigLayout("", "[grow][grow][][grow]", "[][grow][][][grow]"));
		
		CraftGUI craftGUI = new CraftGUI(3, window.getPlayer().inv, null, window);
		add(craftGUI, "cell 0 1 2 1,grow");
		
		JLabel lbl0 = new JLabel($res("wguim-craftaid"));
		lbl0.setBackground(new Color(0, 139, 139));
		lbl0.setOpaque(true);
		add(lbl0, "flowx,cell 0 0 2 1,growx");
		
		JLabel lbl1 = new JLabel($res("wguim-in"));
		lbl1.setBackground(Color.ORANGE);
		add(lbl1, "cell 2 0 2 1,growx");
		lbl1.setOpaque(true);
		
		JButton button = new JButton($res("exit"));
		button.setBackground(Color.RED);
		button.addActionListener(e -> window.closeWindow(this));
		craftGUI.buttonbar1.add(button);
		
		invIn = new InventoryController(crafter.incoming);
		add(invIn, "cell 3 1,grow");
		
		invOut = new InventoryController();
		invOut.setInv(crafter.output);
		add(invOut, "cell 3 4,grow");
		
		JLabel lbl2 = new JLabel($res("wguim-progress"));
		add(lbl2, "flowx,cell 2 2,growx");
		
		progressBar = new JProgressBar();
		progressBar.setMaximum(crafter.delay);
		add(progressBar, "cell 3 2,growx");
		
		JLabel lblScds = new JLabel($res("stencil"));
		lblScds.setBackground(Color.RED);
		lblScds.setOpaque(true);
		add(lblScds, "cell 0 3 2 1,growx");
		
		JLabel lbl3 = new JLabel($res("wguim-out"));
		lbl3.setForeground(new Color(255, 255, 0));
		lbl3.setBackground(Color.BLUE);
		add(lbl3, "cell 2 3 2 1,growx");
		lbl3.setOpaque(true);
		
		SingleInventoryController invStencil = new SingleInventoryController(crafter.stencil);
		add(invStencil, "cell 1 4,grow");
		
		MoveItems moveStencil = new MoveItems(craftGUI.inventoryController, invStencil);
		add(moveStencil, "cell 0 4,grow");

		MoveItems moveOut = new MoveItems(craftGUI.inventoryController, invOut, MoveItems.LEFT);
		add(moveOut, "cell 2 4,grow");
		
		MoveItems moveIn = new MoveItems(craftGUI.inventoryController, invIn);
		add(moveIn, "flowy,cell 2 1,grow");
	}
	private static final long serialVersionUID = 6896680900244482487L;
	private InventoryController invOut;
	private InventoryController invIn;
	private JProgressBar progressBar;

	public void refresh() {
		progressBar.setValue(crafter.getTimer());
		invIn.refresh();
		invOut.refresh();
	}
	public InventoryController getInvOut() {
		return invOut;
	}
	public InventoryController getInvIn() {
		return invIn;
	}
	
	@Override
	public void close(WorldWindow window) {
		crafter.gui = null;
	}
}
