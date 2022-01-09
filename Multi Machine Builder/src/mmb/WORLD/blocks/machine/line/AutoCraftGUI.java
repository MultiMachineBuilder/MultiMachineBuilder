/**
 * 
 */
package mmb.WORLD.blocks.machine.line;

import mmb.WORLD.gui.window.GUITab;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.items.data.Stencil;
import net.miginfocom.swing.MigLayout;
import mmb.WORLD.gui.inv.CraftGUI;

import javax.annotation.Nonnull;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import mmb.WORLD.gui.inv.MoveItems;
import mmb.WORLD.gui.inv.InventoryController;
import javax.swing.JProgressBar;

/**
 * @author oskar
 *
 */
class AutoCraftGUI extends GUITab {
	private AutoCrafter crafter;
	public AutoCraftGUI(AutoCrafter crafter, WorldWindow window) {
		this.crafter = crafter;
		setLayout(new MigLayout("", "[grow][grow][grow]", "[][grow][][][grow]"));
		
		JLabel lbl0 = new JLabel("A crafting GUI to help create recipes:");
		lbl0.setBackground(new Color(0, 139, 139));
		lbl0.setOpaque(true);
		add(lbl0, "flowx,cell 0 0,growx");
		
		JLabel lbl1 = new JLabel("Incoming items:");
		lbl1.setBackground(Color.ORANGE);
		add(lbl1, "cell 1 0 2 1,growx");
		lbl1.setOpaque(true);
		
		InventoryController ctrl = new InventoryController(window.getPlayer().inv);
		CraftGUI craftGUI = new CraftGUI(3, null, window, ctrl);
		add(craftGUI, "cell 0 1 1 4,grow");
		
		JButton button = new JButton("Exit");
		button.setBackground(Color.RED);
		button.addActionListener(e -> window.closeWindow(this));
		craftGUI.verticalBox.add(button);
		
		invIn = new InventoryController();
		invIn.setInv(crafter.incoming);
		add(invIn, "cell 2 1,grow");
		
		MoveItems moveIn = new MoveItems(ctrl, invIn);
		add(moveIn, "flowy,cell 1 1,grow");
		
		JLabel lbl2 = new JLabel("Progress:");
		add(lbl2, "flowx,cell 1 2,growx");
		
		progressBar = new JProgressBar();
		progressBar.setMaximum(50);
		add(progressBar, "cell 2 2");
		
		JLabel lbl3 = new JLabel("Outgoing items:");
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
		JButton setStencil = new JButton("Move this stencil to the crafter →");
		setStencil.setBackground(new Color(255, 215, 0));
		setStencil.addActionListener(e -> {
			if(crafter.getStencil() != null) return;
			ItemRecord record = craftGUI.inventoryController.getSelectedValue();
			ItemEntry item = record.item();
			if(!(item instanceof Stencil)) return;
			Stencil stencil = (Stencil)item;
			boolean insertible = crafter.setStencil(stencil);
			if(!insertible) {
				crafter.setStencil(null);
				return;
			}
			int extract = record.extract(1);
			if(extract == 0) {
				crafter.setStencil(null);
				return;
			}
			ctrl.refresh();
		});
		craftGUI.verticalBox.add(setStencil);
		
		JButton getStencil = new JButton("← Remove stencil from the crafter");
		getStencil.setBackground(new Color(50, 205, 50));
		getStencil.addActionListener(e -> {
			Stencil stencil = crafter.getStencil();
			if(stencil == null) return;
			Inventory inv = window.getPlayer().inv;
			int insert = inv.insert(stencil, 1);
			if(insert == 1) crafter.setStencil(null);
			ctrl.refresh();
		});
		craftGUI.verticalBox.add(getStencil);
		
	}
	private static final long serialVersionUID = 6896680900244482487L;
	@Nonnull private InventoryController invOut;
	@Nonnull private InventoryController invIn;
	@Nonnull private JProgressBar progressBar;
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
	public void createTab(WorldWindow window) {
		//unused
	}
	@Override
	public void destroyTab(WorldWindow window) {
		crafter.closeWindow();
	}
}
