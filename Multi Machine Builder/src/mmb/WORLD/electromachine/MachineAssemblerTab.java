/**
 * 
 */
package mmb.WORLD.electromachine;

import mmb.WORLD.crafting.ComplexCatalyzedItemProcessHelper.Refreshable;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.electric.Electricity;
import mmb.WORLD.gui.window.GUITab;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.Inventories;
import mmb.WORLD.inventory.ItemRecord;
import net.miginfocom.swing.MigLayout;
import mmb.WORLD.gui.inv.InventoryController;
import mmb.WORLD.gui.inv.MoveItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.ainslec.picocog.PicoWriter;

/**
 * @author oskar
 *
 */
public class MachineAssemblerTab extends GUITab implements Refreshable{
	private static final long serialVersionUID = 3998363632338624337L;
	@Nonnull private final MachineAssembler furnace;
	@Nonnull private InventoryController invOutput;
	@Nonnull private InventoryController invInput;
	@Nonnull private JLabel lblSmelt;
	@Nonnull private JProgressBar progressSmelt;
	@Nonnull private JProgressBar progressEnergy;

	/**
	 * Create the panel.
	 */
	public MachineAssemblerTab(MachineAssembler alloySmelter, WorldWindow window) {
		this.furnace = alloySmelter;
		setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][grow][][][grow]"));
		
		InventoryController invPlayer = new InventoryController(window.getPlayer().inv);
		add(invPlayer, "cell 0 0 1 5,grow");
		
		MachineInfoTab machineInfoTab = new MachineInfoTab(alloySmelter);
		add(machineInfoTab, "cell 2 0,grow");
		
		invInput = new InventoryController(alloySmelter.in);
		invInput.setTitle("  Input  ");
		add(invInput, "cell 2 1,grow");
		
		MoveItems moveItemsInput = new MoveItems(invPlayer, invInput);
		add(moveItemsInput, "cell 1 0 1 2,grow");
		
		JButton exit = new JButton("Exit");
		exit.addActionListener(e -> window.closeWindow(this));
		exit.setBackground(Color.RED);
		add(exit, "cell 1 2,grow");
		
		JLabel lblEnergy = new JLabel("Energy:");
		lblEnergy.setBackground(Color.GREEN);
		add(lblEnergy, "flowx,cell 2 2");
		
		JButton removeCatalyst = new JButton("< CAT");
		removeCatalyst.addActionListener(e -> {
			Inventories.transfer(furnace.catalyst, window.getPlayer().inv);
			invPlayer.refresh();
		});
		removeCatalyst.setBackground(new Color(204, 102, 102));
		add(removeCatalyst, "flowx,cell 1 3,growx");
		
		lblSmelt = new JLabel("Currently smelting:");
		add(lblSmelt, "flowx,cell 2 3");
		
		invOutput = new InventoryController(alloySmelter.out);
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
		
		JButton addCatalyst = new JButton("CAT >");
		addCatalyst.setBackground(new Color(102, 204, 102));
		addCatalyst.addActionListener(e -> {
			ItemRecord record = invPlayer.getSelectedValue();
			if(record == null) return;
			Inventories.transfer(record, furnace.catalyst);
			invPlayer.refresh();
		});
		add(addCatalyst, "cell 1 3,growx");
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
	public void refreshProgress(double progress, @Nullable RecipeOutput underway, double energy) {
		if(underway == null) {
			lblSmelt.setText("Not smelting");
		}else {
			progressSmelt.setMaximum((int)energy);
			progressSmelt.setValue((int)(progress*100));
			PicoWriter writer = new PicoWriter();
			underway.represent(writer);
			lblSmelt.setText("Currently smelted: "+writer.toString());
		}
		double volts = furnace.elec.voltage.volts;
		double max = volts * furnace.elec.capacity;
		double amt = volts * furnace.elec.amt;
		Electricity.formatProgress(progressEnergy, amt, max);
	}

}
