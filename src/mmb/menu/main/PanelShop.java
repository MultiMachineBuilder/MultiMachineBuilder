/**
 * 
 */
package mmb.menu.main;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import net.miginfocom.swing.MigLayout;

/**
 * The shop panel in the main menu (currently unused)
 * @author oskar
 */
public class PanelShop extends JPanel {
	private static final long serialVersionUID = 5613201675920230934L;
	
	private JScrollPane scrollPane;
	private JTable table;
	private JMenuBar menuBar;
	private JMenu mnUpload;
	private JPanel panel;
	private JMenuItem mntmSave;
	private JMenuItem mntmMod;
	private JMenuItem mntmOther;
	private JButton btnDownload;
	private JButton btnRefresh;
	private JMenu reportAbuse;
	private JMenuItem reportMods;
	private JMenuItem preparePolice;

	/** Create the panel. */
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
		
		reportAbuse = new JMenu("Report abuse...");
		menuBar.add(reportAbuse);
		
		reportMods = new JMenuItem("Report to the moderators");
		reportAbuse.add(reportMods);
		
		preparePolice = new JMenuItem("Prepare police report");
		reportAbuse.add(preparePolice);
		
		panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new MigLayout("", "[][]", "[]"));
		
		btnDownload = new JButton("Download");
		panel.add(btnDownload, "cell 0 0");
		
		btnRefresh = new JButton("Refresh");
		panel.add(btnRefresh, "cell 1 0");

	}

}
