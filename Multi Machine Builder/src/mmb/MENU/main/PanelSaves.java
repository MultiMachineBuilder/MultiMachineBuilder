/**
 * 
 */
package mmb.MENU.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import mmb.DATA.file.AdvancedFile;
import mmb.DATA.file.LocalFile;
import mmb.debug.Debugger;
import mmb.files.saves.Save;
import mmb.ui.game.NewGame;
import mmb.ui.game.WorldFrame;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

/**
 * @author oskar
 *
 */
public class PanelSaves extends JPanel {
	private final Debugger debug = new Debugger("SELECT A SAVE");
	public java.util.List<Save> saves = new ArrayList<Save>();
	private List list;
	/**
	 * Create the panel.
	 */
	public PanelSaves() {
		setLayout(new BorderLayout(0, 0));
		list = new List();
		add(list);
		
		JPanel subPanelSaves = new JPanel();
		add(subPanelSaves, BorderLayout.SOUTH);
		subPanelSaves.setLayout(new MigLayout("", "[][][][]", "[]"));
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Save s = saves.get(list.getSelectedIndex());
				new WorldFrame(s.file).setVisible(true);
				debug.printl("Successfully opened "+s.name);
			}
		});
		btnPlay.setBackground(Color.GREEN);
		btnPlay.setForeground(Color.BLACK);
		subPanelSaves.add(btnPlay, "cell 0 0");
		
		JButton btnNewWorld = new JButton("New world");
		btnNewWorld.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new NewGame().setVisible(true);
			}
		});
		subPanelSaves.add(btnNewWorld, "cell 1 0");
		
		JButton btnReloadWorlds = new JButton("Refresh");
		btnReloadWorlds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refresh();
			}
		});
		subPanelSaves.add(btnReloadWorlds, "cell 2 0");
		refresh();
	}
	public void refresh() {
		saves.clear();
		list.removeAll();
		LocalFile saves2 = new LocalFile("maps/");
		AdvancedFile[] files;
		try {
			files = saves2.children();
			for(int i = 0; i < files.length; i++) {
				if(files[i].name().endsWith(".mworld")){
					Save save = new Save(files[i]);
					saves.add(save);
					list.add(save.name);
					debug.printl("Found save: "+save.name);
				}
			}
			debug.printl("Found "+files.length+" saves");
		} catch (Exception e) {
			debug.pstm(e, "THIS EXCEPTION SHOULD NOT HAPPEN");
		}
	}

}
