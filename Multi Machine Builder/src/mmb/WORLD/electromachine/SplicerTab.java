/**
 * 
 */
package mmb.WORLD.electromachine;

import mmb.WORLD.crafting.StackedItemProcessHelper.Refreshable;
import mmb.WORLD.crafting.recipes.SimpleProcessingRecipeGroup.SimpleProcessingRecipe;
import mmb.WORLD.crafting.recipes.StackedProcessingRecipeGroup.StackedProcessingRecipe;
import mmb.WORLD.electric.Electricity;
import mmb.WORLD.gui.window.GUITab;
import mmb.WORLD.gui.window.WorldWindow;
import net.miginfocom.swing.MigLayout;
import mmb.WORLD.gui.inv.InventoryController;
import mmb.WORLD.gui.inv.MoveItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JCheckBox;

/**
 * @author oskar
 *
 */
public class SplicerTab extends GUITab implements Refreshable{
	private static final long serialVersionUID = 3998363632338624337L;
	@Nonnull private final Splicer furnace;
	@Nonnull private InventoryController invOutput;
	@Nonnull private InventoryController invInput;
	@Nonnull private JLabel lblSmelt;
	@Nonnull private JProgressBar progressSmelt;
	@Nonnull private JProgressBar progressEnergy;
	private MachineInfoTab machineInfoTab;

	/**
	 * Create the panel.
	 */
	public SplicerTab(Splicer furnace, WorldWindow window) {
		this.furnace = furnace;
		setLayout(new MigLayout("", "[grow][grow][grow]", "[][grow][][][grow]"));
		
		InventoryController invPlayer = new InventoryController(window.getPlayer().inv);
		add(invPlayer, "cell 0 0 1 5,grow");
		
		invInput = new InventoryController(furnace.in);
		invInput.setTitle("  Input  ");
		add(invInput, "cell 2 1,grow");
		
		MoveItems moveItemsInput = new MoveItems(invPlayer, invInput);
		add(moveItemsInput, "cell 1 0 1 2,grow");
		
		JButton exit = new JButton("Exit");
		exit.addActionListener(e -> window.closeWindow(this));
		exit.setBackground(Color.RED);
		add(exit, "cell 1 2 1 2,grow");
		
		JLabel lblEnergy = new JLabel("Energy:");
		lblEnergy.setBackground(Color.GREEN);
		add(lblEnergy, "flowx,cell 2 2");
		
		lblSmelt = new JLabel("Currently smelting:");
		add(lblSmelt, "flowx,cell 2 3");
		
		invOutput = new InventoryController(furnace.out);
		invOutput.setTitle("  Output  ");
		add(invOutput, "cell 2 4,grow");
		
		MoveItems moveItemsOutput = new MoveItems(invPlayer, invOutput, MoveItems.LEFT);
		add(moveItemsOutput, "cell 1 4,grow");
		
		progressEnergy = new JProgressBar();
		progressEnergy.setStringPainted(true);
		add(progressEnergy, "cell 2 2,grow");
		
		progressSmelt = new JProgressBar();
		progressSmelt.setStringPainted(true);
		add(progressSmelt, "cell 2 3,grow");
		
		machineInfoTab = new MachineInfoTab(furnace);
		add(machineInfoTab, "cell 2 0,grow");
	}

	
	@Override
	public void createTab(WorldWindow window) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroyTab(WorldWindow window) {
		furnace.tab = null;
	}

	@Override
	public void refreshInputs() {
		invInput.refresh();
	}

	@Override
	public void refreshOutputs() {
		invOutput.refresh();
	}

	@Override
	public void refreshProgress(double progress, @Nullable StackedProcessingRecipe underway) {
		if(underway == null) {
			lblSmelt.setText("Not smelting");
		}else {
			progressSmelt.setValue((int)(progress*100));
			lblSmelt.setText("Currently smelted: "+underway.input.type().title());
		}
		double volts = furnace.elec.voltage.volts;
		double max =      volts * furnace.elec.capacity;
		double amt = volts * furnace.elec.amt;
		Electricity.formatProgress(progressEnergy, amt, max);
	}

}
