/**
 * 
 */
package mmb.content.old;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JLabel;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Color;

import javax.swing.JProgressBar;

import mmb.NN;
import mmb.content.ditems.Stencil;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.item.ItemEntry;
import mmb.menu.world.inv.CraftGUI;
import mmb.menu.world.inv.InventoryController;
import mmb.menu.world.inv.MoveItems;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;

/**
 * A manual autocrafterGUI
 * @author oskar
 *
 */
class AutoCraftGUI extends GUITab {
	private transient AutoCrafter crafter;
	public AutoCraftGUI(AutoCrafter crafter, WorldWindow window) {
		this.crafter = crafter;
		setLayout(new MigLayout("", "[grow][grow][grow]", "[][grow][][][grow]"));
		
		JLabel lbl0 = new JLabel($res("wguim-craftaid"));
		lbl0.setBackground(new Color(0, 139, 139));
		lbl0.setOpaque(true);
		add(lbl0, "flowx,cell 0 0,growx");
		
		JLabel lbl1 = new JLabel($res("wguim-in"));
		lbl1.setBackground(Color.ORANGE);
		add(lbl1, "cell 1 0 2 1,growx");
		lbl1.setOpaque(true);
		
		InventoryController ctrl = new InventoryController(window.getPlayer().inv);
		CraftGUI craftGUI = new CraftGUI(3, null, window, ctrl);
		craftGUI.inventoryController.setTitle($res("player"));
		add(craftGUI, "cell 0 1 1 4,grow");
		
		JButton button = new JButton($res("exit"));
		button.setBackground(Color.RED);
		button.addActionListener(e -> window.closeWindow(this));
		craftGUI.buttonbar.add(button);
		
		invIn = new InventoryController();
		invIn.setInv(crafter.incoming);
		add(invIn, "cell 2 1,grow");
		
		MoveItems moveIn = new MoveItems(ctrl, invIn);
		add(moveIn, "flowy,cell 1 1,grow");
		
		JLabel lbl2 = new JLabel($res("wguim-progress"));
		add(lbl2, "flowx,cell 1 2,growx");
		
		progressBar = new JProgressBar();
		progressBar.setMaximum(50);
		add(progressBar, "cell 2 2,growx");
		
		JLabel lbl3 = new JLabel($res("wguim-out"));
		lbl3.setForeground(new Color(255, 255, 0));
		lbl3.setBackground(Color.BLUE);
		add(lbl3, "cell 1 3 2 1,growx");
		lbl3.setOpaque(true);
		
		invOut = new InventoryController();
		invOut.setInv(crafter.output);
		add(invOut, "cell 2 4,grow");
		
		MoveItems moveOut = new MoveItems(ctrl, invOut, MoveItems.LEFT);
		add(moveOut, "cell 1 4,grow");
		
		//Manipulate the internal stencil
		JButton setStencil = new JButton($res("wguim-insstencil"));
		setStencil.setBackground(new Color(255, 215, 0));
		setStencil.addActionListener(e -> {
			if(crafter.getStencil() != null) return;
			ItemRecord irecord = craftGUI.inventoryController.getSelectedValue();
			if(irecord == null) return;
			ItemEntry item = irecord.item();
			if(!(item instanceof Stencil)) return;
			
			Stencil stencil = (Stencil)item;
			boolean insertible = crafter.setStencil(stencil);
			if(!insertible) {
				crafter.setStencil(null);
				return;
			}
			int extract = irecord.extract(1);
			if(extract == 0) {
				crafter.setStencil(null);
				return;
			}
			ctrl.refresh();
		});
		craftGUI.buttonbar.add(setStencil);
		
		JButton getStencil = new JButton($res("wguim-exstencil"));
		getStencil.setBackground(new Color(50, 205, 50));
		getStencil.addActionListener(e -> {
			Stencil stencil = crafter.getStencil();
			if(stencil == null) return;
			Inventory inv = window.getPlayer().inv;
			int insert = inv.insert(stencil, 1);
			if(insert == 1) crafter.setStencil(null);
			ctrl.refresh();
		});
		craftGUI.buttonbar.add(getStencil);
		
	}
	private static final long serialVersionUID = 6896680900244482487L;
	@NN private InventoryController invOut;
	@NN private InventoryController invIn;
	@NN private JProgressBar progressBar;
	/**
	 * 
	 */
	public void refresh() {
		progressBar.setValue(50-crafter.remaining);
	}
	public InventoryController getInvOut() {
		return invOut;
	}
	public InventoryController getInvIn() {
		return invIn;
	}
	
	@Override
	public void close(WorldWindow window) {
		crafter.closeWindow();
	}
}
