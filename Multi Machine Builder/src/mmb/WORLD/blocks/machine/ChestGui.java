/**
 * 
 */
package mmb.WORLD.blocks.machine;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import mmb.WORLD.gui.ColorGUI;
import mmb.WORLD.player.Player;
import mmb.WORLD.gui.inv.InventoryController;
import mmb.WORLD.gui.inv.InventoryOrchestrator;
import mmb.WORLD.gui.window.WorldWindow;

import javax.swing.JButton;
import javax.annotation.Nonnull;
import java.awt.Color;
import mmb.WORLD.gui.inv.MoveItems;

/**
 * @author oskar
 *
 */
public class ChestGui extends JPanel{
	public ChestGui(Chest chest, WorldWindow window) {
		setLayout(new MigLayout("", "[300.00,grow][132.00,grow][300.00,grow]", "[grow][30.00]"));
		
		Player p = window.getPlayer();
		
		color = new JButton("Change color");
		color.addActionListener(e -> {
			ColorGUI gui = new ColorGUI(chest.getColorVariable(), window);
			window.openAndShowWindow(gui, "Chest color");
		});
		
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
		
		close = new JButton("Close this GUI");
		close.addActionListener(e -> window.closeWindow(this));
		close.setBackground(Color.RED);
		
		moveItems = new MoveItems(playerCtrl, chestCtrl);
		moveItems.addAdditionalComponent(color);
		moveItems.addAdditionalComponent(close);
		add(moveItems, "cell 1 0,growx,aligny center");
		
		refresh();
	}
	private static final long serialVersionUID = -3527290050616724746L;
	private InventoryController playerCtrl;
	@Nonnull private InventoryOrchestrator inventoryOrchestrator;
	private InventoryController chestCtrl;
	private JButton close;
	private JButton color;
	private MoveItems moveItems;
	
	private void refresh() {
		chestCtrl.refresh();
		playerCtrl.refresh();
	}

}
