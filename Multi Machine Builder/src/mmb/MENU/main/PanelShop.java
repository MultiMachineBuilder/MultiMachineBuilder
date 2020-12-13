/**
 * 
 */
package mmb.MENU.main;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JDesktopPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JButton;
import javax.swing.JProgressBar;

/**
 * @author oskar
 *
 */
public class PanelShop extends JPanel {
	private JScrollPane scrollPane;
	private JTable table;
	private JMenuBar menuBar;
	private JMenu mnUpload;
	private JPanel panel;
	private JButton btnNewButton;
	private JProgressBar progressBar;

	/**
	 * Create the panel.
	 */
	public PanelShop() {
		setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);
		
		menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);
		
		mnUpload = new JMenu("Upload...");
		menuBar.add(mnUpload);
		
		panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		btnNewButton = new JButton("Download");
		panel.add(btnNewButton, BorderLayout.WEST);
		
		progressBar = new JProgressBar();
		panel.add(progressBar);

	}

}
