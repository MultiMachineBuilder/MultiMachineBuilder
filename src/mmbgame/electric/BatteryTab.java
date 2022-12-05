/**
 * 
 */
package mmbgame.electric;

import javax.annotation.Nonnull;

import net.miginfocom.swing.MigLayout;
import mmb.menu.helper.MenuHelper;
import mmb.menu.world.inv.InventoryController;
import mmb.menu.world.inv.MoveItems;
import mmb.menu.world.inv.SingleInventoryController;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;
import mmbgame.electric.machines.BlockBattery;

import javax.swing.JButton;
import javax.swing.JProgressBar;
import java.awt.Color;

/**
 * @author oskar
 *
 */
public class BatteryTab extends GUITab {
	private static final long serialVersionUID = -6777331260258929072L;
	@Nonnull private final transient BlockBattery battery;
	@Nonnull private final JProgressBar progressBar;
	/**
	 * Creates a batter GUI
	 * @param battery block to use
	 * @param window world window
	 */
	public BatteryTab(BlockBattery battery, WorldWindow window) {
		this.battery = battery;
		setLayout(new MigLayout("", "[grow][][center]", "[grow][grow][][]"));
		
		InventoryController inventoryController = new InventoryController();
		add(inventoryController, "cell 0 0 1 4,grow");
		
		SingleInventoryController sicCharger = new SingleInventoryController(battery.charger);
		add(sicCharger, "cell 2 0,growy");
		
		MoveItems moveCharger = new MoveItems(inventoryController, sicCharger);
		add(moveCharger, "cell 1 0,grow");
		
		SingleInventoryController sicDischarger = new SingleInventoryController(battery.discharger);
		add(sicDischarger, "cell 2 1,growy");
		
		MoveItems moveDischarger = new MoveItems(inventoryController, sicDischarger);
		add(moveDischarger, "cell 1 1,grow");
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setForeground(Color.ORANGE);
		add(progressBar, "cell 1 2 2 1,growx");
		
		JButton exit = MenuHelper.newButton("#exit", Color.RED, MenuHelper.closeGUI(window, this));
		add(exit, "cell 1 3 2 3,growx");
	}
	
	/**
	 * Refreshes this GUI
	 */
	public void refresh() {
		Electricity.formatProgress(progressBar, battery.battery.stored, battery.battery.capacity);
	}
	
	@Override
	public void close(WorldWindow window) {
		battery.close(this);
	}

}
