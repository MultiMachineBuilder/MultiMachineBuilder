/**
 * 
 */
package mmbbase.menu.main;

import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import mmb.engine.debug.Debugger;
import mmb.engine.mods.ModInfo;
import mmb.engine.mods.ModState;
import mmb.engine.mods.Modfile;
import mmb.engine.mods.Mods;
import net.miginfocom.swing.MigLayout;

import static mmb.engine.settings.GlobalSettings.$res;

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
	private static Debugger debug = new Debugger("MODLIST");
	public static final PanelMods INSTANCE = new PanelMods();
	
	private PanelMods() {
		setLayout(new MigLayout("", "[450px,grow 300][grow]", "[][265px,grow][35px]"));
		
		JLabel lblMods = new JLabel($res("cgui-mods"));
		add(lblMods, "cell 0 0,growx");
		
		JLabel lblModfs = new JLabel($res("cgui-modfs"));
		add(lblModfs, "cell 1 0,growx");
		
		JScrollPane scrollPaneModfs = new JScrollPane();
		add(scrollPaneModfs, "cell 1 1,grow");
		
		tablemodelModfs.addColumn($res("cguim-name"));
		tablemodelModfs.addColumn($res("cguim-state"));
		Mods.files.forEach(this::addModf);
		tableModfs = new JTable(tablemodelModfs);
		tableModfs.setFillsViewportHeight(true);
		scrollPaneModfs.setViewportView(tableModfs);
		
		JPanel subPanelMods = new JPanel();
		add(subPanelMods, "cell 0 2 2 1,growx,aligny top");
		subPanelMods.setLayout(new MigLayout("", "[][]", "[]"));
		
		JLabel lblModCounter = new JLabel(Mods.mods.size()+" mods");
		subPanelMods.add(lblModCounter, "cell 0 0");
		
		btnNewButton = new JButton($res("cguim-dir"));
		btnNewButton.addActionListener(e -> {
			try {Desktop.getDesktop().open(new File("mods/"));}
			catch (IOException e1) {
				debug.pstm(e1, "Couldn't find mods/ directory");
			}
		});
		subPanelMods.add(btnNewButton, "cell 1 0");
		
		
		tablemodelMods.addColumn($res("cguim-name"));
		tablemodelMods.addColumn($res("cguim-descr"));
		tablemodelMods.addColumn($res("cguim-state"));
		tablemodelMods.addColumn($res("cguim-update"));
		tablemodelMods.addColumn($res("cguim-version"));
		tablemodelMods.addColumn($res("cguim-author"));
		Mods.mods.forEach(this::addMod);
		tableMods = new JTable(tablemodelMods);
		tableMods.setFillsViewportHeight(true);
		JScrollPane scrollPaneMods = new JScrollPane(tableMods);
		scrollPaneMods.setViewportBorder(UIManager.getBorder("Button.border"));
		add(scrollPaneMods, "cell 0 1,grow");
	}
	
	private final DefaultTableModel tablemodelMods = new DefaultTableModel();
	private final DefaultTableModel tablemodelModfs = new DefaultTableModel();
	private JTable tableMods;
	private JButton btnNewButton;
	private static final String UNKNOWN = $res("cguim-un");
	private JTable tableModfs;
	private void addMod(ModInfo mod) {
		String release = UNKNOWN;
		String descr = "This file is corrupt.";
		String author = UNKNOWN;
		if(mod.state == ModState.ENABLE) {
			release = mod.meta.release.toString();
			descr = mod.meta.description;
			author = mod.meta.author;
		}else {
			release = new Date().toString();
			descr = "No description";
		}
		String state = mod.state.title;
		String ver = "";
		String name = mod.meta.name;
		tablemodelMods.addRow(new Object[] {name, descr, state, release, ver, author});
	}
	
	private void addModf(Modfile file) {
		String state = file.state.title;
		String name = file.path;
		tablemodelModfs.addRow(new Object[] {name, state});
	}

}
