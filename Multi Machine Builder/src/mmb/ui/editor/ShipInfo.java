package mmb.ui.editor;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

public class ShipInfo extends JPanel {

	/**
	 * Create the panel.
	 */
	public ShipInfo() {
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane);
		
		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);
		panel.setLayout(new MigLayout("", "[][]", "[][]"));
		
		JLabel lblShipProperties = new JLabel("Ship properties");
		panel.add(lblShipProperties, "cell 0 0");
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setRightComponent(tabbedPane);

	}

}
