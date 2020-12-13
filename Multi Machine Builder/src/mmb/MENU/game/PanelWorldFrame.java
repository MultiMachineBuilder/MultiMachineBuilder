/**
 * 
 */
package mmb.MENU.game;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;

/**
 * @author oskar
 *
 */
public class PanelWorldFrame extends JPanel {
	private JPanel panel;
	private JButton btnNewButton;

	/**
	 * Create the panel.
	 */
	public PanelWorldFrame() {
		setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		
		btnNewButton = new JButton("Exit");
		panel.add(btnNewButton);

	}

}
