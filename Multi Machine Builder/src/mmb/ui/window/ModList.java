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

import mmb.addon.data.AddonInfo;
import mmb.addon.data.AddonState;
import mmb.addon.data.ModMetadata;
import mmb.data.contents.GameContents;

public class ModList extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JPanel settings;
	private DefaultTableModel tablemodel = new DefaultTableModel();
	private JButton btnViewInFile;
	private JButton btnKillThisMod;

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
		contentPane.setLayout(new MigLayout("", "[grow]", "[grow][grow]"));
		
		table = new JTable();
		tablemodel.addColumn("Name");
		tablemodel.addColumn("Description");
		tablemodel.addColumn("State");
		tablemodel.addColumn("Last update");
		tablemodel.addColumn("Version");
		tablemodel.addColumn("Author");
		table.setModel(tablemodel);
		contentPane.add(table, "cell 0 0,grow");
		
		settings = new JPanel();
		contentPane.add(settings, "cell 0 1,grow");
		settings.setLayout(new MigLayout("", "[]", "[][][]"));
		
		btnViewInFile = new JButton("View in file browser");
		settings.add(btnViewInFile, "cell 0 0");
		
		btnKillThisMod = new JButton("Kill this mod");
		settings.add(btnKillThisMod, "cell 0 1");
		
		GameContents.addons.forEach((AddonInfo a) -> {addMod(a);});
		setVisible(true);
	}
	private void addMod(AddonInfo mod) {
		String release = "Unknown", descr = "This file is corrupt.", author = "Unknown";
		if(mod == null){
			
		}else if(mod.mmbmod == null) {
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
