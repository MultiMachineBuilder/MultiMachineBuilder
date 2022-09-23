/**
 * 
 */
package mmb.WORLD.blocks.machine.manual;

import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import mmb.WORLD.gui.window.GUITab;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.recipes.CraftingGroups;
import net.miginfocom.swing.MigLayout;
import mmb.DATA.variables.Variable;
import mmb.LAMBDAS.Consumers;
import mmb.WORLD.gui.inv.InventoryController;
import mmb.WORLD.gui.inv.ItemSelectionSlot;
import javax.swing.JLabel;
import javax.swing.JButton;

import static mmb.GlobalSettings.$res;

import java.awt.Color;
import java.awt.Dimension;

/**
 * @author oskar
 *
 */
public class PickaxeGUI extends GUITab {
	private static final long serialVersionUID = -7967104797035980435L;
	private ItemType crafted = null;
	public PickaxeGUI(WorldWindow window) {
		setLayout(new MigLayout("", "[grow][]", "[grow][][][]"));
		
		InventoryController inventoryController = new InventoryController();
		add(inventoryController, "cell 0 0 1 4,grow");
		Inventory inv= window.getPlayer().inv;
		inventoryController.setInv(inv);
		
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
		slot.stateChanged.addListener(ent -> crafted = CraftingGroups.pickaxes.recipes.get(ent).output);
		
		JLabel lblNewLabel = new JLabel("crafted: ");
		add(lblNewLabel, "cell 1 1");
		
		JButton btnCraft = new JButton($res("wguim-craft"));
		btnCraft.addActionListener(e -> {
			if(crafted != null) {
				ItemEntry result = crafted.create();
				ItemEntry sel = slot.getSelection();
				if(sel == null) return;
				CraftingGroups.transact(sel, result.single(), inv, inv);
				inventoryController.refresh();
			}
		});
		btnCraft.setBackground(new Color(50, 205, 50));
		add(btnCraft, "cell 1 2,growx");
		
		JButton btnExit = new JButton($res("exit"));
		btnExit.addActionListener(e -> window.closeWindow(this));
		btnExit.setBackground(Color.RED);
		add(btnExit, "cell 1 3,growx");
		
	}
	@Override
	public void createTab(WorldWindow window) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void destroyTab(WorldWindow window) {
		// TODO Auto-generated method stub
		
	}
}
