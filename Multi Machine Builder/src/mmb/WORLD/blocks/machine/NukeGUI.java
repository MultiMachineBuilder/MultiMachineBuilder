/**
 * 
 */
package mmb.WORLD.blocks.machine;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import mmb.WORLD.blocks.machine.SkeletalBlockMachine.Update;

/**
 * @author oskar
 *
 */
class NukeGUI extends JPanel implements AutoCloseable, Update{
	JLabel fuellevel;
	private final Nuker nuker;
	public NukeGUI(Nuker nuker) {
		this.nuker = nuker;
		setLayout(new MigLayout("", "[][]", "[]"));
		
		fuellevel = new JLabel("Fuel level:");
		add(fuellevel, "cell 0 0");
	}
	private static final long serialVersionUID = 4601578870693782502L;
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update() {
		fuellevel.setText("Fuel level:" + nuker.getFuelRemain());
	}

}
