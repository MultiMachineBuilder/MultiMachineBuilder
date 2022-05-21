/**
 * 
 */
package mmb.WORLD.electromachine;

import mmb.WORLD.crafting.Recipe;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.crafting.Refreshable;
import mmb.WORLD.electric.Electricity;
import mmb.WORLD.gui.window.GUITab;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.Inventories;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.inventory.storage.SingleItemInventory;
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

import io.github.parubok.text.multiline.MultilineLabel;
import javax.swing.Box;

/**
 * @author oskar
 *
 */
public class MachineTab extends GUITab implements Refreshable{
	private static final long serialVersionUID = 3998363632338624337L;
	@Nonnull private final CommonMachine furnace;
	@Nonnull private InventoryController invOutput;
	@Nonnull private InventoryController invInput;
	@Nonnull private MultilineLabel lblSmelt;
	@Nonnull private JProgressBar progressSmelt;
	@Nonnull private JProgressBar progressEnergy;
	private MultilineLabel lblChance;

	/**
	 * Create the panel.
	 */
	public MachineTab(CommonMachine alloySmelter, WorldWindow window) {
		this.furnace = alloySmelter;
		setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][grow][][][][grow]"));
		
		InventoryController invPlayer = new InventoryController(window.getPlayer().inv);
		add(invPlayer, "cell 0 0 1 6,grow");
		
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
		SingleItemInventory cats = furnace.catalyst();
		
		lblSmelt = new MultilineLabel("Currently smelting:");
		lblSmelt.setPreferredViewportLineCount(5);
		add(lblSmelt, "flowx,cell 2 3");
		
		lblChance = new MultilineLabel("CHANCE TO GET");
		lblChance.setPreferredViewportLineCount(5);
		add(lblChance, "cell 2 4");
		
		if(cats != null){
			JButton removeCatalyst = new JButton("< CAT");
			removeCatalyst.addActionListener(e -> {
				Inventories.transfer(cats, window.getPlayer().inv);
				invPlayer.refresh();
			});
		
			removeCatalyst.setBackground(new Color(204, 102, 102));
			add(removeCatalyst, "cell 1 3,growx");
			
			JButton addCatalyst = new JButton("CAT >");
			addCatalyst.setBackground(new Color(102, 204, 102));
			addCatalyst.addActionListener(e -> {
				ItemRecord record = invPlayer.getSelectedValue();
				if(record == null) return;
				Inventories.transfer(record, cats);
				invPlayer.refresh();
			});
			add(addCatalyst, "cell 1 4,growx");
		}
		
		invOutput = new InventoryController(alloySmelter.out);
		invOutput.setTitle("  Output  ");
		add(invOutput, "cell 2 5,grow");
		
		MoveItems moveItemsOutput = new MoveItems(invPlayer, invOutput, MoveItems.LEFT);
		add(moveItemsOutput, "cell 1 5,grow");
		
		progressEnergy = new JProgressBar();
		progressEnergy.setStringPainted(true);
		add(progressEnergy, "cell 2 2,grow");
		
		progressSmelt = new JProgressBar();
		progressSmelt.setStringPainted(true);
		add(progressSmelt, "cell 2 3,grow");
		
		
	}
	
	@Override
	public void createTab(WorldWindow window) {
		//unused
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
	public void refreshProgress(double progress, @Nullable Recipe underway) {
		if(underway == null) {
			lblSmelt.setText("Not smelting");
		}else {
			progressSmelt.setValue((int)(progress*100));
			PicoWriter writerA = new PicoWriter();
			underway.output().represent(writerA);
			lblSmelt.setText("Currently smelted: "+writerA.toString());
			PicoWriter writerB = new PicoWriter();
			underway.luck().represent(writerB);
			lblChance.setText("CHANCE TO GET: "+writerB.toString());
		}
		double volts = furnace.elec.voltage.volts;
		double max = volts * furnace.elec.capacity;
		double amt = volts * furnace.elec.amt;
		Electricity.formatProgress(progressEnergy, amt, max);
	}

}
