/**
 * 
 */
package mmb.WORLD.blocks.machine;

import net.miginfocom.swing.MigLayout;
import mmb.BEANS.ToItemUnifiedCollector;
import mmb.MENU.world.inv.InventoryController;
import mmb.MENU.world.inv.MoveItems;
import mmb.MENU.world.window.GUITab;
import mmb.MENU.world.window.WorldWindow;

import javax.swing.JButton;

import static mmb.GlobalSettings.$res;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * @author oskar
 *
 */
public class CollectorGUI extends GUITab {
	private static final long serialVersionUID = 2506447463267036557L;
	
	private final ToItemUnifiedCollector coll;
	public CollectorGUI(Collector collector, WorldWindow window) {
		coll = collector;
		setLayout(new MigLayout("", "[][][grow]", "[][grow][]"));
		
		JLabel lblPeriod = new JLabel($res("colperiod"));
		add(lblPeriod, "cell 1 0");
		
		JSpinner spinner = new JSpinner();
		spinner.addChangeListener(e -> {
			Integer value = (Integer) spinner.getValue();
			if(value == null) return;
			collector.setPeriod(value.intValue());
		});
		add(spinner, "cell 2 0,growx");
		
		
		InventoryController playerInv = new InventoryController();
		playerInv.setTitle($res("player"));
		playerInv.setInv(window.getPlayer().inv);
		add(playerInv, "cell 0 1,grow");
		
		InventoryController collectorInv = new InventoryController();
		collectorInv.setInv(collector.inv());
		add(collectorInv, "cell 2 1,grow");
		
		MoveItems moveItems = new MoveItems(playerInv, collectorInv, MoveItems.LEFT);
		add(moveItems, "cell 1 1,grow");
		
		JButton btnNewButton = new JButton($res("exit"));
		btnNewButton.addActionListener(e ->window.closeWindow(this));
		btnNewButton.setBackground(Color.RED);
		add(btnNewButton, "cell 0 2 3 1,growx");
	}

	@Override
	public void createTab(WorldWindow window) {
		//unused
	}

	@Override
	public void destroyTab(WorldWindow window) {
		coll.destroyTab(this);
	}
}
