/**
 * 
 */
package mmb.MENU.settings;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

/**
 * @author oskar
 *
 */
public class PanelSettings extends JPanel {
	private JTabbedPane tabbedPane;
	private JPanel panel;

	/**
	 * Create the panel.
	 */
	public PanelSettings() {
		setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);
		
		panel = new JPanel();
		tabbedPane.addTab("New tab", null, panel, null);

	}

}
