/**
 * 
 */
package mmb.WORLD.gui.machine;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import mmb.WORLD.blocks.machine.Furnace;
import mmb.WORLD.gui.inv.InventoryController;
import mmb.WORLD.gui.inv.InventoryOrchestrator;

import javax.swing.JLabel;
import mmb.WORLD.gui.inv.MoveItems;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.ItemEntry;

import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JProgressBar;

/**
 * @author oskar
 *
 */
public class Smelting extends JPanel {
	private static final long serialVersionUID = 82163446136100004L;
	
	private InventoryController invPlayer;
	private InventoryOrchestrator inventoryOrchestrator;
	private MoveItems moveItemsInput;
	public final InventoryController invInput;
	private MoveItems moveItemsOutput;
	public final InventoryController invOutput;
	private JButton exit;
	private JLabel label;
	private JProgressBar progressBar;
	private final Furnace f;
	/**
	 * Create the panel.
	 */
	public Smelting(Furnace furnace, WorldWindow window) {
		f = furnace;
		setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][][grow][29.00]"));
		
		invPlayer = new InventoryController(window.getPlayer().inv);
		add(invPlayer, "flowx,cell 0 0 1 3,alignx left,growy");
		
		invInput = new InventoryController(furnace.in);
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
		
		invOutput = new InventoryController(furnace.out);
		add(invOutput, "cell 2 2,grow");
		
		moveItemsOutput = new MoveItems(invPlayer, invOutput, MoveItems.LEFT);
		add(moveItemsOutput, "cell 1 2,grow");
		
		
		inventoryOrchestrator = new InventoryOrchestrator();
		add(inventoryOrchestrator, "cell 0 3 3 1,grow");
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		add(progressBar, "cell 2 1,grow");
	}
	
	public void refresh() {
		progressBar.setValue(100 - f.getRemaining());
		ItemEntry smelt = f.getSmeltingUnderway();
		if(smelt == null) {
			label.setText("Not smelting");
		}else {
			label.setText("Currently smelted: "+smelt.type().title());
		}
		
	}

}
