/**
 * 
 */
package mmb.WORLD.blocks.machine;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import mmb.WORLD.gui.WorldWindow;
import mmb.WORLD.gui.ColorGUI;
import mmb.WORLD.gui.InventoryController;
import mmb.WORLD.inventory.Inventories;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.player.Player;
import mmb.WORLD.gui.InventoryOrchestrator;
import mmb.WORLD.gui.Variable;

import javax.swing.JButton;
import javax.annotation.Nonnull;
import javax.swing.BoxLayout;
import java.awt.Color;
import javax.swing.JColorChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author oskar
 *
 */
public class ChestGui extends JPanel{
	public ChestGui(Chest chest, WorldWindow window) {
		setLayout(new MigLayout("", "[grow][132.00,grow][]", "[grow][30.00]"));
		
		Player p = window.getPlayer();
		
		inventoryOrchestrator = new InventoryOrchestrator();
		add(inventoryOrchestrator, "cell 0 1 3 1,growx");
		
		chestCtrl = new InventoryController();
		chestCtrl.setInv(chest.inv);
		chestCtrl.setOrchestrator(inventoryOrchestrator);
		chestCtrl.setTitle(" Chest ");
		add(chestCtrl, "cell 2 0,grow");
		
		playerCtrl = new InventoryController();
		playerCtrl.setTitle(" Player ");
		playerCtrl.setInv(p.inv);
		playerCtrl.setOrchestrator(inventoryOrchestrator);
		add(playerCtrl, "cell 0 0,grow");
		
		panel = new JPanel();
		add(panel, "cell 1 0,grow");
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		btnAllL = new JButton("<< All");
		btnAllL.addActionListener(e -> {
			Inventories.transfer(chest.inv, p.inv);
			refresh();
		});
		panel.add(btnAllL);
		
		btnSelL = new JButton("<< Selection");
		btnSelL.addActionListener(e -> {
			for(ItemRecord selRecord: chestCtrl.getSelectedValuesList()) {
				Inventories.transfer(selRecord, p.inv);
			}
			refresh();
		});
		panel.add(btnSelL);
		
		btnOPSelL = new JButton("<< Once per selection");
		panel.add(btnOPSelL);
		btnOPSelL.addActionListener(e -> {
			for(ItemRecord selRecord: chestCtrl.getSelectedValuesList()) {
				Inventories.transfer(selRecord, p.inv, 1);
			}
			refresh();
		});
		
		btnOneL = new JButton("<< One");
		btnOneL.addActionListener(e -> {
			Inventories.transfer(chestCtrl.getSelectedValue(), p.inv, 1);
			refresh();
		});
		panel.add(btnOneL);
		
		btnOneR = new JButton("One >>");
		btnOneR.addActionListener(e -> {
			Inventories.transfer(playerCtrl.getSelectedValue(), chest.inv, 1);
			refresh();
		});
		panel.add(btnOneR);
		
		JButton btnOPSelR = new JButton("Once per selection >>");
		panel.add(btnOPSelR);
		btnOPSelR.addActionListener(e -> {
			for(ItemRecord selRecord: playerCtrl.getSelectedValuesList()) {
				Inventories.transfer(selRecord, chest.inv, 1);
			}
			refresh();
		});
		
		btnSelR = new JButton("Selection >>");
		btnSelR.addActionListener(e -> {
			for(ItemRecord selRecord: playerCtrl.getSelectedValuesList()) {
				Inventories.transfer(selRecord, chest.inv);
			}
			refresh();
		});
		panel.add(btnSelR);
				
		btnAllR = new JButton("All >>");
		btnAllR.addActionListener(e -> {
			Inventories.transfer(p.inv, chest.inv);
			refresh();
		});
		panel.add(btnAllR);
		
		btnNewButton = new JButton("Close this GUI");
		btnNewButton.addActionListener(e -> window.closeWindow(this));
		btnNewButton.setBackground(Color.RED);
		panel.add(btnNewButton);
		
		Variable<Color> vcolor = Variable.delegate(() -> chest.getColor(), c -> chest.setColor(c));
		btnNewButton_1 = new JButton("Change color");
		btnNewButton_1.addActionListener(e -> {
			ColorGUI gui = new ColorGUI(vcolor, window);
			window.openAndShowWindow(gui, "Chest color");
		});
		panel.add(btnNewButton_1);
		
		refresh();
	}
	private static final long serialVersionUID = -3527290050616724746L;
	private InventoryController playerCtrl;
	@Nonnull private InventoryOrchestrator inventoryOrchestrator;
	private InventoryController chestCtrl;
	private JButton btnAllL;
	private JButton btnOneL;
	private JPanel panel;
	private JButton btnSelL;
	private JButton btnOPSelL;
	private JButton btnOneR;
	private JButton btnSelR;
	private JButton btnAllR;
	private JButton btnNewButton;
	private JColorChooser colorChooser;
	private JButton btnNewButton_1;
	
	private void refresh() {
		chestCtrl.refresh();
		playerCtrl.refresh();
	}

}
