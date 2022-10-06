/**
 * 
 */
package mmb.MENU.world.machine;

import javax.swing.JPanel;

import mmb.WORLD.blocks.machine.SideConfig;
import javax.swing.BoxLayout;
import mmb.MENU.components.BoundCheckBox;

/**
 * @author oskar
 * Configures sides of given side config. Remember to close, as it holds resources on given side config
 */
public class SideConfigCtrl extends JPanel implements AutoCloseable{
	private static final long serialVersionUID = 5825304896794378584L;
	public final SideConfig cfg;
	private final BoundCheckBox cU, cD, cL, cR;
	public SideConfigCtrl(SideConfig cfg) {
		this.cfg = cfg;
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		
		cU = new BoundCheckBox("U");
		cU.setVariable(cfg.U);
		add(cU);
		
		cD = new BoundCheckBox("D");
		cD.setVariable(cfg.D);
		add(cD);
		
		cL = new BoundCheckBox("L");
		cL.setVariable(cfg.L);
		add(cL);
		
		cR = new BoundCheckBox("R");
		cR.setVariable(cfg.R);
		add(cR);
	}

	@Override
	public void close() {
		cU.setVariable(null);
		cD.setVariable(null);
		cL.setVariable(null);
		cR.setVariable(null);
	}

}
