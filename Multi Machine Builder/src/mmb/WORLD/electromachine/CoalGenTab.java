/**
 * 
 */
package mmb.WORLD.electromachine;

import mmb.WORLD.gui.window.GUITab;
import mmb.WORLD.gui.window.WorldWindow;
import net.miginfocom.swing.MigLayout;
import mmb.WORLD.electric.Electricity;
import mmb.WORLD.gui.inv.InventoryController;
import mmb.WORLD.gui.inv.MoveItems;

import javax.annotation.Nonnull;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import java.awt.Color;
import javax.swing.JButton;

/**
 * @author oskar
 *
 */
public class CoalGenTab extends GUITab {
	private static final long serialVersionUID = 1680582237436697644L;
	@Nonnull private final CoalGen gen;
	private JProgressBar progressEnergy;
	private JProgressBar progressFuel;
	public CoalGenTab(WorldWindow window, CoalGen generator) {
		gen = generator;
		setLayout(new MigLayout("", "[grow][grow][grow][]", "[grow][][]"));
		
		InventoryController invPlayer = new InventoryController(window.getPlayer().inv);
		add(invPlayer, "cell 0 0 1 3,grow");
		
		
		InventoryController invGenerator = new InventoryController(gen.inv);
		add(invGenerator, "cell 2 0 2 1,grow");
		
		MoveItems moveItems = new MoveItems(invPlayer, invGenerator);
		add(moveItems, "cell 1 0,grow");
		
		JLabel lblFuel = new JLabel("Fuel level:");
		add(lblFuel, "flowy,cell 2 1,growx");
		
		progressFuel = new JProgressBar();
		progressFuel.setStringPainted(true);
		progressFuel.setForeground(Color.ORANGE);
		add(progressFuel, "cell 3 1");
		
		JButton btnExit = new JButton("Exit");
		btnExit.setBackground(Color.RED);
		btnExit.addActionListener(e -> window.closeWindow(this));
		add(btnExit, "cell 1 1 1 2,grow");
		
		JLabel lblEnergy = new JLabel("Energy level:");
		add(lblEnergy, "cell 2 2");
		
		progressEnergy = new JProgressBar();
		progressEnergy.setStringPainted(true);
		progressEnergy.setForeground(Color.GREEN);
		add(progressEnergy, "cell 3 2");
	}

	@Override
	public void createTab(WorldWindow window) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroyTab(WorldWindow window) {
		gen.tab = null;
	}
	
	void refresh() {
		double voltf = gen.fuel.voltage.volts;
		double maxf =      voltf * gen.fuel.capacity;
		double progressf = voltf * gen.fuel.amt;
		Electricity.formatProgress(progressFuel, progressf, maxf);
		
		double volte = gen.buffer.voltage.volts;
		double maxe =      volte * gen.buffer.capacity;
		double progresse = volte * gen.buffer.amt;
		Electricity.formatProgress(progressEnergy, progresse, maxe);
	}
}
