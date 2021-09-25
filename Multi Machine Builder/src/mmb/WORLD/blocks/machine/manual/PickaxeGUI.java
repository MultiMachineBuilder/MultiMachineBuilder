/**
 * 
 */
package mmb.WORLD.blocks.machine.manual;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.items.ItemEntry;
import net.miginfocom.swing.MigLayout;
import mmb.LAMBDAS.Consumers;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.gui.Variable;
import mmb.WORLD.gui.inv.InventoryController;
import mmb.WORLD.gui.inv.ItemSelectionSlot;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;

/**
 * @author oskar
 *
 */
public class PickaxeGUI extends JPanel {
	private ItemType crafted = null;
	public PickaxeGUI(WorldWindow window) {
		setLayout(new MigLayout("", "[grow][]", "[grow][][][]"));
		
		InventoryController inventoryController = new InventoryController();
		add(inventoryController, "cell 0 0 1 4,grow");
		inventoryController.setInv(window.getPlayer().inv);
		
		ItemSelectionSlot slot = new ItemSelectionSlot();
		add(slot, "cell 1 0");
		slot.setSelectionSrc(Variable.delegate(() -> {
			ItemRecord sel = inventoryController.getSelectedValue();
			if(sel == null) return null;
			return sel.item();
		}, Consumers.doNothing()));
		Border border = new BevelBorder(BevelBorder.LOWERED);slot.setBorder(border);
		Dimension dim = new Dimension(40, 40);
		slot.setMinimumSize(dim);
		slot.setMaximumSize(dim);
		slot.setPreferredSize(dim);
		slot.stateChanged.addListener(ent -> {
			crafted = PickaxeWorkbench.recipes.get(ent);
		});
		
		JLabel lblNewLabel = new JLabel("crafted: ");
		add(lblNewLabel, "cell 1 1");
		
		JButton btnCraft = new JButton("< Craft");
		btnCraft.addActionListener(e -> {
			if(crafted != null) {
				ItemEntry item = crafted.create();
				Craftings.transact(slot.getSelection(), item, inventoryController.getInv(), inventoryController.getInv());
				inventoryController.refresh();
			}
		});
		btnCraft.setBackground(new Color(50, 205, 50));
		add(btnCraft, "cell 1 2,growx");
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(e -> {
			window.closeWindow(this);
		});
		btnExit.setBackground(Color.RED);
		add(btnExit, "cell 1 3,growx");
		
	}
}
