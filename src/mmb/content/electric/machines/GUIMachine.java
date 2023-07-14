/**
 * 
 */
package mmb.content.electric.machines;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;

import static mmb.engine.settings.GlobalSettings.*;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.ainslec.picocog.PicoWriter;

import io.github.parubok.text.multiline.MultilineLabel;
import mmb.NN;
import mmb.Nil;
import mmb.content.electric.Electricity;
import mmb.content.electric.MachineInfoTab;
import mmb.engine.inv.Inventories;
import mmb.engine.inv.ItemRecord;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.recipe.Recipe;
import mmb.engine.recipe.Refreshable;
import mmb.menu.world.inv.InventoryController;
import mmb.menu.world.inv.MoveItems;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;

/**
 * Allows the manual use of electric machines, and to extract and insert items to it
 * @author oskar
 */
public class GUIMachine extends GUITab implements Refreshable{
	private static final long serialVersionUID = 3998363632338624337L;
	@NN private final ProcessorAbstractBlock furnace;
	@NN private InventoryController invOutput;
	@NN private InventoryController invInput;
	@NN private MultilineLabel lblSmelt;
	@NN private JProgressBar progressSmelt;
	@NN private JProgressBar progressEnergy;
	private MultilineLabel lblChance;
	
	@NN private static final String SMELT = ($res("wguim-smelt"));
	@NN private static final String NOSMELT = ($res("wguim-nosmelt"));
	@NN private static final String CHANCE = ($res("wguim-chance"));
	/**
	 * Creates a processing machine panel.
	 * @param machine machine block
	 * @param window world window
	 */
	public GUIMachine(ProcessorAbstractBlock machine, WorldWindow window) {
		this.furnace = machine;
		setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][grow][][][][grow]"));
		
		InventoryController invPlayer = new InventoryController();
		window.playerInventory(invPlayer);
		add(invPlayer, "cell 0 0 1 6,grow");
		
		MachineInfoTab machineInfoTab = new MachineInfoTab(machine);
		add(machineInfoTab, "cell 2 0,grow");
		
		invInput = new InventoryController(machine.in);
		invInput.setTitle($res("wguim-in"));
		add(invInput, "cell 2 1,grow");
		
		MoveItems moveItemsInput = new MoveItems(invPlayer, invInput);
		add(moveItemsInput, "cell 1 0 1 2,grow");
		
		JButton exit = new JButton($res("wguic-exit"));
		exit.addActionListener(e -> window.closeWindow(this));
		exit.setBackground(Color.RED);
		add(exit, "cell 1 2,grow");
		
		JLabel lblEnergy = new JLabel(SMELT);
		lblEnergy.setBackground(Color.GREEN);
		add(lblEnergy, "flowx,cell 2 2");
		SingleItemInventory cats = furnace.catalyst();
		
		lblSmelt = new MultilineLabel($res("wguim-energy"));
		lblSmelt.setPreferredViewportLineCount(5);
		add(lblSmelt, "flowx,cell 2 3");
		
		lblChance = new MultilineLabel($res("wguim-chance"));
		lblChance.setPreferredViewportLineCount(5);
		add(lblChance, "cell 2 4,growx");
		
		if(cats != null){
			JButton removeCatalyst = new JButton("< CAT");
			removeCatalyst.addActionListener(e -> {
				Inventories.transferAll(cats, window.getPlayer().inv);
				invPlayer.refresh();
			});
		
			removeCatalyst.setBackground(new Color(204, 102, 102));
			add(removeCatalyst, "cell 1 3,growx");
			
			JButton addCatalyst = new JButton("CAT >");
			addCatalyst.setBackground(new Color(102, 204, 102));
			addCatalyst.addActionListener(e -> {
				ItemRecord irecord = invPlayer.getSelectedValue();
				if(irecord == null) return;
				Inventories.transferRecord(irecord, cats);
				invPlayer.refresh();
			});
			add(addCatalyst, "cell 1 4,growx");
		}
		
		invOutput = new InventoryController(machine.out);
		invOutput.setTitle($res("wguim-out"));
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
	public void close(WorldWindow window) {
		furnace.close(this);
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
	public void refreshProgress(double progress, @Nil Recipe<?> underway) {
		if(underway == null) {
			lblSmelt.setText(NOSMELT);
		}else {
			progressSmelt.setValue((int)(progress*100));
			PicoWriter writerA = new PicoWriter();
			underway.output().represent(writerA);
			lblSmelt.setText(SMELT+" "+writerA.toString());
			PicoWriter writerB = new PicoWriter();
			underway.luck().represent(writerB);
			lblChance.setText(CHANCE+" "+writerB.toString());
		}
		double volts = furnace.elec.voltage.volts;
		double max = volts * furnace.elec.capacity;
		double amt = volts * furnace.elec.stored;
		Electricity.formatProgress(progressEnergy, amt, max);
	}

}
