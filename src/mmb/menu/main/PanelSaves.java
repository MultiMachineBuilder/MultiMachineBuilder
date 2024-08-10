/**
 * 
 */
package mmb.menu.main;

import static mmb.engine.settings.GlobalSettings.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import mmb.engine.debug.Debugger;
import mmb.engine.files.AdvancedFile;
import mmb.engine.files.LocalFile;
import mmb.engine.files.Save;
import mmb.menu.FullScreen;
import mmb.menu.newgame.NewGame;
import net.miginfocom.swing.MigLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The saves panel in the main menu
 * @author oskar
 */
public class PanelSaves extends JPanel {
	private static final long serialVersionUID = -873377369477463310L;
	private static final Debugger debug = new Debugger("SELECT A SAVE");	
	private static final JFileChooser jfc = new JFileChooser();
	/**
	 * The singleton instance of {@code PanelSaves}
	 */
	public static final PanelSaves INSTANCE = new PanelSaves();
	
	/**The list of all saves*/
	public final transient java.util.List<Save> saves = new ArrayList<>();
	private List list;
	/**
	 * Create the panel.
	 */
	private PanelSaves() {
		setLayout(new BorderLayout(0, 0));
		list = new List();
		add(list);
		
		JPanel subPanelSaves = new JPanel();
		add(subPanelSaves, BorderLayout.SOUTH);
		subPanelSaves.setLayout(new MigLayout("", "[][][][][]", "[]"));
		
		JButton btnPlay = new JButton($res("cguiw-play"));
		btnPlay.setToolTipText("Play the selected world");
		btnPlay.addActionListener(e -> play());
		btnPlay.setBackground(Color.GREEN);
		subPanelSaves.add(btnPlay, "cell 0 0");
		
		/*
		 * Create a new world.
		 * In 0.5 this menu will significantly be enhanced
		 */
		JButton btnNewWorld = new JButton($res("cguiw-new"));
		btnNewWorld.setToolTipText("Create a new world");
		btnNewWorld.addActionListener(arg -> FullScreen.setWindow(new NewGame()));
		subPanelSaves.add(btnNewWorld, "cell 1 0");
		
		
		JButton btnReloadWorlds = new JButton($res("cguiw-refresh"));
		btnReloadWorlds.setToolTipText("Refresh save list");
		btnReloadWorlds.addActionListener(arg -> refresh());
		subPanelSaves.add(btnReloadWorlds, "cell 2 0");
		
		JButton btnMapsDir = new JButton($res("cguiw-dir"));
		btnMapsDir.setToolTipText("Open 'maps' directory in the file manager");
		btnMapsDir.addActionListener(arg -> {
				try {
					Desktop.getDesktop().open(new File("maps/"));
				} catch (IOException e) {
					debug.stacktraceError(e, "Failed to open maps/ directory");
				}
		});
		subPanelSaves.add(btnMapsDir, "cell 3 0");
		
		JButton btnOpenExternal = new JButton($res("cguiw-ext"));
		btnOpenExternal.setBackground(Color.ORANGE);
		btnOpenExternal.addActionListener(e -> playExternal());
		subPanelSaves.add(btnOpenExternal, "cell 4 0");
		
		EventQueue.invokeLater(this::refresh);
	}
	
	/** Plays an external world */
	@SuppressWarnings("null")
	public void playExternal() {
		int result = jfc.showOpenDialog(this);
		if(result == JFileChooser.APPROVE_OPTION) {
			new PlayAWorld().play(new Save(new LocalFile(jfc.getSelectedFile())));
		}
	}

	/** Plays the selected world */
	public void play() {
		int index = list.getSelectedIndex();
		if(index < 0) {
			debug.printl("No selected world!");
			return;
		}
		Save s = saves.get(index);
		if(!s.file.exists()) {
			debug.printl("File does not exist");
			refresh();
			return;
		}
		new PlayAWorld().play(s);
	}
	
	/**
	 * Refresh the save list
	 */
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
			debug.printl("Found "+list.getItemCount()+" saves");
		} catch (Exception e) {
			debug.stacktraceError(e, "THIS EXCEPTION SHOULD NOT HAPPEN");
		}
	}
	

}
