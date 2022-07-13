/**
 * 
 */
package mmb.WORLD.blocks.machine;

import mmb.WORLD.gui.window.GUITab;
import mmb.WORLD.gui.window.WorldWindow;
import net.miginfocom.swing.MigLayout;
import mmb.BEANS.ToItemUnifiedCollector;
import mmb.WORLD.electric.Electricity;
import mmb.WORLD.gui.inv.InventoryController;
import mmb.WORLD.gui.inv.MoveItems;

import javax.annotation.Nonnull;
import javax.swing.JButton;

import static mmb.GlobalSettings.$res;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JProgressBar;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author oskar
 *
 */
public class DiggerGUI extends GUITab {
	private static final long serialVersionUID = 2506447463267036557L;
	
	private final Digger coll;
	@Nonnull private JProgressBar progressEnergy;
	private JCheckBox checkActive;
	private InventoryController collectorInv;
	public DiggerGUI(Digger collector, WorldWindow window) {
		coll = collector;
		setLayout(new MigLayout("", "[][][grow]", "[][grow][]"));
		
		checkActive = new JCheckBox($res("digactive"));
		checkActive.addActionListener(e -> coll.setActive(checkActive.isSelected()));
		add(checkActive, "cell 0 0");
		
		JLabel lblPeriod = new JLabel($res("wguim-energy"));
		add(lblPeriod, "cell 1 0");
		
		progressEnergy = new JProgressBar();
		progressEnergy.setStringPainted(true);
		progressEnergy.setForeground(Color.ORANGE);
		add(progressEnergy, "cell 2 0,grow");
		
		InventoryController playerInv = new InventoryController();
		playerInv.setTitle($res("player"));
		playerInv.setInv(window.getPlayer().inv);
		add(playerInv, "cell 0 1,grow");
		
		collectorInv = new InventoryController();
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
	
	public void refreshEnergy() {
		double volts = coll.battery.voltage.volts;
		double max = volts * coll.battery.capacity;
		double amt = volts * coll.battery.amt;
		checkActive.setSelected(coll.isActive());
		collectorInv.refresh();
		Electricity.formatProgress(progressEnergy, amt, max);
	}
}
