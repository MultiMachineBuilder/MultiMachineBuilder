/**
 * 
 */
package mmb.WORLD.gui.machine;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 * @author oskar
 *
 */
public class AssemblyTable extends JPanel {

	/**
	 * Create the panel.
	 */
	public AssemblyTable() {

		initialize();
	}
	private void initialize() {
		setLayout(new MigLayout("", "[]", "[]"));
	}

}
