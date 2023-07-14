/**
 * 
 */
package mmb.content.electric.machines;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import mmb.NN;
import mmb.content.electric.Electricity;
import mmb.menu.world.inv.InventoryController;
import mmb.menu.world.inv.MoveItems;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;

import javax.swing.JCheckBox;

/**
 * Allows to configure a digger and extract items from it
 * @author oskar
 */
public class GUIDigger extends GUITab {
	private static final long serialVersionUID = 2506447463267036557L;
	
	@NN private final transient BlockDigger coll;
	@NN private JProgressBar progressEnergy;
	@NN private JCheckBox checkActive;
	@NN private InventoryController collectorInv;
	@NN private JProgressBar progressHammer;
	public GUIDigger(BlockDigger collector, WorldWindow window) {
		coll = collector;
		setLayout(new MigLayout("", "[][][grow]", "[][][grow][]"));
		
		checkActive = new JCheckBox($res("digactive"));
		checkActive.addActionListener(e -> coll.setActive(checkActive.isSelected()));
		add(checkActive, "cell 0 0");
		
		JLabel lblPeriod = new JLabel($res("wguim-energy"));
		add(lblPeriod, "flowy,cell 1 0");
		
		progressEnergy = new JProgressBar();
		progressEnergy.setStringPainted(true);
		progressEnergy.setForeground(Color.ORANGE);
		add(progressEnergy, "cell 2 0,grow");
		
		JLabel lblHammer = new JLabel($res("wguim-hammer"));
		add(lblHammer, "cell 1 1");
		
		progressHammer = new JProgressBar();
		progressHammer.setForeground(Color.RED);
		progressHammer.setStringPainted(true);
		add(progressHammer, "cell 2 1,grow");
		
		InventoryController playerInv = new InventoryController();
		playerInv.setTitle($res("player"));
		window.playerInventory(playerInv);
		add(playerInv, "cell 0 2,grow");
		
		collectorInv = new InventoryController();
		collectorInv.setInv(collector.inv());
		add(collectorInv, "cell 2 2,grow");
		
		MoveItems moveItems = new MoveItems(playerInv, collectorInv, MoveItems.LEFT);
		add(moveItems, "cell 1 2,grow");
		
		JButton btnNewButton = new JButton($res("exit"));
		btnNewButton.addActionListener(e ->window.closeWindow(this));
		btnNewButton.setBackground(Color.RED);
		add(btnNewButton, "cell 0 3 3 1,growx");
	}

	@Override
	public void close(WorldWindow window) {
		coll.destroyTab(this);
	}
	
	public void refreshEnergy() {
		double volts = coll.battery.voltage.volts;
		double max = volts * coll.battery.capacity;
		double amt = volts * coll.battery.stored;
		checkActive.setSelected(coll.isActive());
		collectorInv.refresh();
		Electricity.formatProgress(progressEnergy, amt, max);
		double maxh = volts * coll.hammer.capacity;
		double amth = volts * coll.hammer.stored;
		Electricity.formatProgress(progressHammer, amth, maxh);
	}
}
