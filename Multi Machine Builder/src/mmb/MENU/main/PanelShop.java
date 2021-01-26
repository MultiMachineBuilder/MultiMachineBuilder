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
import javax.swing.JMenuItem;
import net.miginfocom.swing.MigLayout;

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
	private JMenuItem mntmSave;
	private JMenuItem mntmMod;
	private JMenuItem mntmOther;
	private JButton btnNewButton;
	private JButton btnNewButton_1;

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
		
		mntmSave = new JMenuItem("Saved game");
		mnUpload.add(mntmSave);
		
		mntmMod = new JMenuItem("Mod");
		mnUpload.add(mntmMod);
		
		mntmOther = new JMenuItem("Other...");
		mnUpload.add(mntmOther);
		
		panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new MigLayout("", "[][]", "[]"));
		
		btnNewButton = new JButton("Download");
		panel.add(btnNewButton, "cell 0 0");
		
		btnNewButton_1 = new JButton("Refresh");
		panel.add(btnNewButton_1, "cell 1 0");

	}

}
