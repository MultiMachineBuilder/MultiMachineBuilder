package mmb.ui.window;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.util.Date;

import net.miginfocom.swing.MigLayout;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import mmb.DATA.contents.GameContents;
import mmb.MODS.info.AddonInfo;
import mmb.MODS.info.AddonState;
import mmb.MODS.info.ModMetadata;

import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class ModList extends JFrame {

	private final JPanel contentPane;
	private final JTable table;
	private final JPanel settings;
	private final DefaultTableModel tablemodel = new DefaultTableModel();
	private final JButton btnViewInFile;
	private final JButton btnKillThisMod;
	private final JScrollPane scrollPane;
	private final JLabel lblModCount;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModList frame = new ModList();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public ModList() {
		setDefaultCloseOperation(2);
		setBounds(100, 100, 598, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[grow][grow][grow]"));
		tablemodel.addColumn("Name");
		tablemodel.addColumn("Description");
		tablemodel.addColumn("State");
		tablemodel.addColumn("Last update");
		tablemodel.addColumn("Version");
		tablemodel.addColumn("Author");
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "cell 0 0,grow");
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(tablemodel);
		
		settings = new JPanel();
		contentPane.add(settings, "cell 0 2,grow");
		settings.setLayout(new MigLayout("", "[][]", "[][][]"));
		
		btnViewInFile = new JButton("View in file browser");
		settings.add(btnViewInFile, "cell 0 0");
		
		lblModCount = new JLabel("Found "+GameContents.addons.size()+" mods");
		settings.add(lblModCount, "cell 1 0");
		
		btnKillThisMod = new JButton("Kill this mod");
		settings.add(btnKillThisMod, "cell 0 1");
		
		GameContents.addons.forEach((AddonInfo a) -> {addMod(a);});
		setVisible(true);
	}
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
