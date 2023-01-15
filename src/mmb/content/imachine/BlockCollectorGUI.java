/**
 * 
 */
package mmb.content.imachine;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Color;
import javax.swing.JSpinner;

import mmb.menu.databind.IntVarSpinnerModel;
import mmb.menu.world.inv.InventoryController;
import mmb.menu.world.inv.MoveItems;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;

import javax.swing.JLabel;

/**
 * @author oskar
 *
 */
public class BlockCollectorGUI extends GUITab {
	private static final long serialVersionUID = 2506447463267036557L;
	
	private final transient ToItemUnifiedCollector coll;
	private final transient IntVarSpinnerModel spinnerModel;
	
	/**
	 * Creates an item collector GUI
	 * @param collector item collector
	 * @param window world window
	 */
	public BlockCollectorGUI(BlockCollector collector, WorldWindow window) {
		coll = collector;
		spinnerModel = new IntVarSpinnerModel(collector.period);
		
		setLayout(new MigLayout("", "[][][grow]", "[][grow][]"));
		
		JLabel lblPeriod = new JLabel($res("colperiod"));
		add(lblPeriod, "cell 1 0");
		
		JSpinner spinner = new JSpinner(spinnerModel);
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
	public void close(WorldWindow window) {
		coll.destroyTab(this);
		spinnerModel.close();
	}
}
