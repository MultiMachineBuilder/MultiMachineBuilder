/**
 * 
 */
package mmbbase.menu.wtool;

import javax.swing.JPanel;

import mmb.NN;
import net.miginfocom.swing.MigLayout;

import javax.swing.JCheckBox;

/**
 * @author oskar
 *
 */
public class ToolVisualsPanel extends JPanel {
	private static final long serialVersionUID = 4745533816341127056L;
	@NN public final JCheckBox autoRefresh;

	/**
	 * Create the panel.
	 */
	public ToolVisualsPanel() {
		setLayout(new MigLayout("", "[]", "[]"));
		
		autoRefresh = new JCheckBox("Autorefresh mouse position");
		autoRefresh.setSelected(true);
		add(autoRefresh, "cell 0 0");

	}

}
