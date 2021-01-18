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
import mmb.DATA.contents.texture.Textures;
import mmb.GRAPHICS.Patch9Image;
import mmb.GRAPHICS.Patch9Panel;
import mmb.MODS.info.AddonInfo;
import mmb.MODS.info.AddonState;
import net.miginfocom.swing.MigLayout;
import java.awt.BorderLayout;

/**
 * @author oskar
 *
 */
public class PanelMods extends Patch9Panel {

	/**
	 * Create the panel.
	 */
	public PanelMods() {
		setImage(Textures.get("UIs/orange signpost.png"));
		setLayout(new BorderLayout(0, 0));
		
		JPanel subPanelMods = new JPanel();
		add(subPanelMods, BorderLayout.SOUTH);
		subPanelMods.setLayout(new MigLayout("", "[]", "[]"));
		
		JLabel lblModCounter = new JLabel(GameContents.addons.size()+" mods");
		subPanelMods.add(lblModCounter, "cell 0 0");
		
		tablemodel.addColumn("Name");
		tablemodel.addColumn("Description");
		tablemodel.addColumn("State");
		tablemodel.addColumn("Last update");
		tablemodel.addColumn("Version");
		tablemodel.addColumn("Author");
		GameContents.addons.forEach((AddonInfo a) -> addMod(a));
		
		table = new JTable(tablemodel);
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setViewportBorder(UIManager.getBorder("Button.border"));
		add(scrollPane);
	}
	
	private final DefaultTableModel tablemodel = new DefaultTableModel();
	private JTable table;
	private void addMod(AddonInfo mod) {
		if(mod == null) return;
		String release = "Unknown", descr = "This file is corrupt.", author = "Unknown";
		if(mod.mmbmod == null) {
			release = new Date().toString();
			descr = "No description";
			author = "Unknown";
		}else if(mod.state == AddonState.ENABLE) {
			release = mod.mmbmod.release.toString();
			descr = mod.mmbmod.description;
			author = mod.mmbmod.author;
		}
		String state = mod.state.toString();
		
		String ver = "";
		String name = mod.name;
		tablemodel.addRow(new Object[] {name, descr, state, release, ver, author});
	}

}
