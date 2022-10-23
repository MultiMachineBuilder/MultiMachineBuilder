/**
 * 
 */
package mmb.menu.world.machine;

import mmb.menu.world.inv.InventoryController;
import mmb.menu.world.inv.MoveItems;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;
import mmb.world.crafting.Recipe;
import mmb.world.crafting.Refreshable;
import mmb.world.electric.Electricity;
import mmb.world.electromachine.BlockProcessorAbstract;
import mmb.world.inventory.Inventories;
import mmb.world.inventory.ItemRecord;
import mmb.world.inventory.storage.SingleItemInventory;
import net.miginfocom.swing.MigLayout;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.ainslec.picocog.PicoWriter;

import io.github.parubok.text.multiline.MultilineLabel;
import static mmb.GlobalSettings.*;

/**
 * @author oskar
 *
 */
public class GUIMachine extends GUITab implements Refreshable{
	private static final long serialVersionUID = 3998363632338624337L;
	@Nonnull private final BlockProcessorAbstract furnace;
	@Nonnull private InventoryController invOutput;
	@Nonnull private InventoryController invInput;
	@Nonnull private MultilineLabel lblSmelt;
	@Nonnull private JProgressBar progressSmelt;
	@Nonnull private JProgressBar progressEnergy;
	private MultilineLabel lblChance;
	
	@Nonnull private static final String SMELT = ($res("wguim-smelt"));
	@Nonnull private static final String NOSMELT = ($res("wguim-nosmelt"));
	@Nonnull private static final String CHANCE = ($res("wguim-chance"));
	/**
	 * Creates a processing machine panel.
	 * @param machine machine block
	 * @param window world window
	 */
	public GUIMachine(BlockProcessorAbstract machine, WorldWindow window) {
		this.furnace = machine;
		setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][grow][][][][grow]"));
		
		InventoryController invPlayer = new InventoryController(window.getPlayer().inv);
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
				Inventories.transfer(cats, window.getPlayer().inv);
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
	public void createTab(WorldWindow window) {
		//unused
	}

	@Override
	public void destroyTab(WorldWindow window) {
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
	public void refreshProgress(double progress, @Nullable Recipe<?> underway) {
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
		double amt = volts * furnace.elec.amt;
		Electricity.formatProgress(progressEnergy, amt, max);
	}

}
