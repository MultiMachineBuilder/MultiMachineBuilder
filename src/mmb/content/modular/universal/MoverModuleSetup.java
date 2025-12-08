/**
 * 
 */
package mmb.content.modular.universal;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

import mmb.annotations.NN;
import mmb.content.modular.gui.SafeCloseable;
import mmb.engine.settings.GlobalSettings;
import mmb.menu.components.ItemSelectionSlot;
import mmb.menu.databind.IntVarSpinnerModel;
import mmb.menu.world.inv.InventoryController;

import java.awt.Color;
import javax.swing.JSpinner;
import mmb.menu.world.inv.SingleInventoryController;
import mmb.menu.world.inv.MoveItems;

/**
 *
 * @author oskar
 *
 */
public class MoverModuleSetup extends JPanel implements SafeCloseable{
	private static final long serialVersionUID = -6701441866106804925L;
	
	
	/** Configures stack count */
	@NN public final JSpinner stacker;
	private JLabel lblUpgrade;
	@NN private SingleInventoryController invctrl2;
	private MoveItems moveItems;
	
	//Resources
	/** The item selection slot */
	@NN public final ItemSelectionSlot settings;
	@NN private final transient IntVarSpinnerModel spinnerModelStack;
	@NN private final transient IntVarSpinnerModel spinnerModelPeriod;
	private JLabel lblPeriod;
	private JSpinner period;

	/**
	 * Create the panel.
	 * @param invctrl player inventory controller
	 * @param module block module
	 */
	public MoverModuleSetup(InventoryController invctrl, MoverModule module) {
		setLayout(new MigLayout("", "[grow][]", "[][][][][grow]"));
		
		JLabel lblSettings = new JLabel(GlobalSettings.$res("modblocks-settings"));
		add(lblSettings, "cell 0 0,growx");
		settings = new ItemSelectionSlot();
		settings.setSelector(invctrl::getSelectedItem);
		settings.setTarget(module.settings);
		settings.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, Color.CYAN));
		add(settings, "cell 1 0");
		
		JLabel lblStacker = new JLabel(GlobalSettings.$res("modblocks-stacker"));
		add(lblStacker, "cell 0 1,growx");
		spinnerModelStack = new IntVarSpinnerModel(module.stacking);
		stacker = new JSpinner(spinnerModelStack);
		add(stacker, "cell 1 1,growx");
		
		lblPeriod = new JLabel(GlobalSettings.$res("modblocks-period"));
		add(lblPeriod, "cell 0 2");
		spinnerModelPeriod = new IntVarSpinnerModel(module.period);
		period = new JSpinner(spinnerModelPeriod);
		add(period, "cell 1 2,growx");
		
		lblUpgrade = new JLabel(GlobalSettings.$res("modblocks-upgrade"));
		add(lblUpgrade, "cell 0 3 2 1,growx");
		invctrl2 = new SingleInventoryController(module.upgrade);
		add(invctrl2, "cell 1 4,grow");
		moveItems = new MoveItems(invctrl, invctrl2);
		add(moveItems, "cell 0 4,grow");
	}

	@Override
	public void close() {
		settings.close();
	}
}
