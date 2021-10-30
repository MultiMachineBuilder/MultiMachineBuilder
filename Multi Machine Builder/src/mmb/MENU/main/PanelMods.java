/**
 * 
 */
package mmb.MENU.main;

import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import mmb.DATA.contents.GameContents;
import mmb.MODS.info.AddonState;
import mmb.MODS.loader.AddonInfo;
import mmb.debug.Debugger;
import net.miginfocom.swing.MigLayout;
import java.awt.BorderLayout;
import java.awt.Desktop;

import javax.swing.JButton;
import java.io.File;
import java.io.IOException;

/**
 * @author oskar
 *
 */
public class PanelMods extends JPanel {
	private static final long serialVersionUID = -971992923441938268L;
	private Debugger debug = new Debugger("MODLIST");
	/**
	 * Create the panel.
	 */
	public PanelMods() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel subPanelMods = new JPanel();
		add(subPanelMods, BorderLayout.SOUTH);
		subPanelMods.setLayout(new MigLayout("", "[][]", "[]"));
		
		JLabel lblModCounter = new JLabel(GameContents.addons.size()+" mods");
		subPanelMods.add(lblModCounter, "cell 0 0");
		
		btnNewButton = new JButton("Open mods directory");
		btnNewButton.addActionListener((e) -> {
			try {Desktop.getDesktop().open(new File("mods/"));}
			catch (IOException e1) {
				debug.pstm(e1, "Couldn't find mods/ directory");
			}
		});
		subPanelMods.add(btnNewButton, "cell 1 0");
		
		tablemodel.addColumn("Name");
		tablemodel.addColumn("Description");
		tablemodel.addColumn("State");
		tablemodel.addColumn("Last update");
		tablemodel.addColumn("Version");
		tablemodel.addColumn("Author");
		GameContents.addons.forEach(this::addMod);
		
		table = new JTable(tablemodel);
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setViewportBorder(UIManager.getBorder("Button.border"));
		add(scrollPane);
	}
	
	private final DefaultTableModel tablemodel = new DefaultTableModel();
	private JTable table;
	private JButton btnNewButton;
	private static final String UNKNOWN = "Unknown";
	private void addMod(AddonInfo mod) {
		String release = UNKNOWN;
		String descr = "This file is corrupt.";
		String author = UNKNOWN;
		if(mod.mmbmod == null) {
			release = new Date().toString();
			descr = "No description";
		}else if(mod.state == AddonState.ENABLE) {
			release = mod.mmbmod.release.toString();
			descr = mod.mmbmod.description;
			author = mod.mmbmod.author;
		}
		String state = mod.state.title;
		String ver = "";
		String name = mod.name;
		tablemodel.addRow(new Object[] {name, descr, state, release, ver, author});
	}

}
