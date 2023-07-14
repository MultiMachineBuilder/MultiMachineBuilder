/**
 * 
 */
package mmb.content.machinemics.line;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JButton;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Color;
import javax.swing.JProgressBar;

import org.ainslec.picocog.PicoWriter;

import mmb.NN;
import mmb.Nil;
import mmb.engine.recipe.Recipe;
import mmb.engine.recipe.Refreshable;
import mmb.menu.world.inv.InventoryController;
import mmb.menu.world.inv.MoveItems;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;

import javax.swing.Box;

/**
 * @author oskar
 *
 */
public class FurnaceGUI extends GUITab implements Refreshable{
	private static final long serialVersionUID = 82163446136100004L;
	
	@NN private InventoryController invPlayer;
	@NN private MoveItems moveItemsInput;
	@NN public final InventoryController invInput;
	@NN private MoveItems moveItemsOutput;
	@NN public final InventoryController invOutput;
	@NN private JButton exit;
	@NN private JLabel lblSmelt;
	@NN private JProgressBar smelt;

	private Furnace furnace;
	private JLabel lblFuelWarn;
	private JLabel lblRecipeWarn;
	private Box verticalBox;
	private JLabel lblFuel;
	private JProgressBar fuel;
	/**
	 * Create the panel.
	 * @param furnace furnace connected to this GUI
	 * @param window world window, in which the furnace GUI is located
	 */
	public FurnaceGUI(Furnace furnace, WorldWindow window) {
		this.furnace = furnace;
		setLayout(new MigLayout("", "[grow][grow][grow]", "[][grow][grow,center][20px,center][grow][29.00]"));
		
		verticalBox = Box.createVerticalBox();
			lblFuelWarn = new JLabel("Note: any fuel items in this place will be consumed for energy.");
			lblFuelWarn.setBackground(Color.YELLOW);
			lblFuelWarn.setOpaque(true);
			verticalBox.add(lblFuelWarn);
			
			lblRecipeWarn = new JLabel("This furnace processes only ULV recipes");
			lblRecipeWarn.setBackground(Color.ORANGE);
			lblRecipeWarn.setOpaque(true);
			verticalBox.add(lblRecipeWarn);
		add(verticalBox, "flowx,cell 2 0,growx");
		
		invPlayer = new InventoryController();
		window.playerInventory(invPlayer);
		invPlayer.setTitle($res("player"));
		add(invPlayer, "flowx,cell 0 0 1 5,grow");
		
		invInput = new InventoryController(furnace.incoming);
		invInput.setTitle($res("wguim-infuel"));
		add(invInput, "cell 2 1,grow");
		
		moveItemsInput = new MoveItems(invPlayer, invInput);
		add(moveItemsInput, "flowy,cell 1 0 1 2,grow");
		
		exit = new JButton($res("exit"));
		exit.addActionListener(e -> window.closeWindow(this));
		exit.setBackground(Color.RED);
		add(exit, "cell 1 2 1 2,grow");
		
		lblSmelt = new JLabel($res("wguim-progress"));
		add(lblSmelt, "flowx,cell 2 3");
		
		invOutput = new InventoryController(furnace.output);
		invOutput.setTitle($res("wguim-out"));
		add(invOutput, "cell 2 4,grow");
		
		moveItemsOutput = new MoveItems(invPlayer, invOutput, MoveItems.LEFT);
		add(moveItemsOutput, "cell 1 4,grow");
		
		smelt = new JProgressBar();
		smelt.setStringPainted(true);
		add(smelt, "cell 2 3,grow");
		
		lblFuel = new JLabel($res("wguim-fuel"));
		add(lblFuel, "flowx,cell 2 2,alignx left");
		
		fuel = new JProgressBar();
		fuel.setMaximum(12_000_000);
		fuel.setStringPainted(true);
		fuel.setForeground(Color.ORANGE);
		add(fuel, "cell 2 2,growx");
	}
	
	@Override
	public void refreshProgress(double progress, @Nil Recipe<?> underway) {
		smelt.setValue((int)progress);
		if(underway == null) {
			lblSmelt.setText("Not smelting");
		}else {
			smelt.setMaximum((int)underway.energy());
			PicoWriter writer = new PicoWriter();
			underway.output().represent(writer);
			lblSmelt.setText("Currently smelted: "+writer.toString());
		}
		double f = furnace.getFuelLevel();
		int f2 = (int) f;
		fuel.setValue(f2);
		fuel.setString(f+"/12'000'000");
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
	public void close(WorldWindow window) {
		furnace.closeWindow();
	}


}
