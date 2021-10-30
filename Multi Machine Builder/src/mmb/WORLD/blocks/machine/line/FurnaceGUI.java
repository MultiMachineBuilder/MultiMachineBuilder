/**
 * 
 */
package mmb.WORLD.blocks.machine.line;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import mmb.WORLD.blocks.machine.line.SimpleItemProcessHelper.Refreshable;
import mmb.WORLD.gui.inv.InventoryController;
import mmb.WORLD.gui.inv.InventoryOrchestrator;

import javax.swing.JLabel;
import mmb.WORLD.gui.inv.MoveItems;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.items.ItemEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JProgressBar;

/**
 * @author oskar
 *
 */
public class FurnaceGUI extends JPanel implements Refreshable{
	private static final long serialVersionUID = 82163446136100004L;
	
	@Nonnull private InventoryController invPlayer;
	@Nonnull private InventoryOrchestrator inventoryOrchestrator;
	@Nonnull private MoveItems moveItemsInput;
	@Nonnull public final InventoryController invInput;
	@Nonnull private MoveItems moveItemsOutput;
	@Nonnull public final InventoryController invOutput;
	@Nonnull private JButton exit;
	@Nonnull private JLabel label;
	@Nonnull private JProgressBar progressBar;
	/**
	 * Create the panel.
	 * @param furnace furnace connected to this GUI
	 * @param window world window, in which the furnace GUI is located
	 */
	public FurnaceGUI(Furnace furnace, WorldWindow window) {
		setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][][grow][29.00]"));
		
		invPlayer = new InventoryController(window.getPlayer().inv);
		add(invPlayer, "flowx,cell 0 0 1 3,grow");
		
		invInput = new InventoryController(furnace.incoming);
		add(invInput, "cell 2 0,grow");
		
		moveItemsInput = new MoveItems(invPlayer, invInput);
		add(moveItemsInput, "flowy,cell 1 0,grow");
		
		exit = new JButton("Exit");
		exit.addActionListener(e -> {
			window.closeWindow(this);
			furnace.closeWindow();
		});
		exit.setBackground(Color.RED);
		add(exit, "cell 1 1,growx");
		
		label = new JLabel("Currently smelted:");
		add(label, "flowx,cell 2 1");
		
		invOutput = new InventoryController(furnace.output);
		add(invOutput, "cell 2 2,grow");
		
		moveItemsOutput = new MoveItems(invPlayer, invOutput, MoveItems.LEFT);
		add(moveItemsOutput, "cell 1 2,grow");
		
		
		inventoryOrchestrator = new InventoryOrchestrator();
		add(inventoryOrchestrator, "cell 0 3 3 1,grow");
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		add(progressBar, "cell 2 1,grow");
	}
	
	@Override
	public void refreshProgress(int progress, @Nullable ItemEntry underway) {
		progressBar.setValue(progress);
		if(underway == null) {
			label.setText("Not smelting");
		}else {
			label.setText("Currently smelted: "+underway.type().title());
		}
		
	}

	@Override
	public void refreshInputs() {
		invInput.refresh();
	}

	@Override
	public void refreshOutputs() {
		invOutput.refresh();
	}


}
