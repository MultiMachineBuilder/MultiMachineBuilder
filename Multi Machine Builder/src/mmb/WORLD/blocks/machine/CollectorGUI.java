/**
 * 
 */
package mmb.WORLD.blocks.machine;

import javax.swing.JPanel;

import mmb.WORLD.gui.window.WorldWindow;
import net.miginfocom.swing.MigLayout;
import mmb.WORLD.gui.inv.InventoryController;
import mmb.WORLD.gui.inv.MoveItems;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author oskar
 *
 */
public class CollectorGUI extends JPanel {
	public CollectorGUI(Collector collector, WorldWindow window) {
		setLayout(new MigLayout("", "[][][]", "[grow][]"));
		
		InventoryController playerInv = new InventoryController();
		playerInv.setInv(window.getPlayer().inv);
		add(playerInv, "cell 0 0,grow");
		
		InventoryController collectorInv = new InventoryController();
		collectorInv.setInv(collector.getInventory());
		add(collectorInv, "cell 2 0,grow");
		
		MoveItems moveItems = new MoveItems(playerInv, collectorInv, MoveItems.LEFT);
		add(moveItems, "cell 1 0,grow");
		
		JButton btnNewButton = new JButton("Exit");
		btnNewButton.addActionListener(e -> {
			collector.gui = null;
			window.closeWindow(this);
		});
		btnNewButton.setBackground(Color.RED);
		add(btnNewButton, "cell 0 1 3 1,growx");
		
		
	}
}
