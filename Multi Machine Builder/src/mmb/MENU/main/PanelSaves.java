/**
 * 
 */
package mmb.MENU.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.DATA.json.JsonTool;
import mmb.FILES.AdvancedFile;
import mmb.FILES.LocalFile;
import mmb.FILES.Save;
import mmb.MENU.FullScreen;
import mmb.MENU.NewWorld.NewGame;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.worlds.universe.Universe;
import mmb.debug.Debugger;
import net.miginfocom.swing.MigLayout;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * @author oskar
 *
 */
public class PanelSaves extends JPanel {
	
	private static final JFileChooser jfc = new JFileChooser();
	/**
	 * The singleton instance of {@code PanelSaves}
	 */
	public static final PanelSaves INSTANCE = new PanelSaves();
	private static final long serialVersionUID = -873377369477463310L;
	
	private final transient Debugger debug = new Debugger("SELECT A SAVE");
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
		
		JButton btnPlay = new JButton("Play");
		btnPlay.setToolTipText("Play the selected world");
		btnPlay.addActionListener(e -> play());
		btnPlay.setBackground(Color.GREEN);
		subPanelSaves.add(btnPlay, "cell 0 0");
		
		/*
		 * Create a new world.
		 * In 0.5 this menu will significantly be enhanced
		 */
		JButton btnNewWorld = new JButton("New world");
		btnNewWorld.setToolTipText("Create a new world");
		btnNewWorld.addActionListener(arg -> FullScreen.setWindow(new NewGame()));
		subPanelSaves.add(btnNewWorld, "cell 1 0");
		
		
		JButton btnReloadWorlds = new JButton("Refresh");
		btnReloadWorlds.setToolTipText("Refresh save list");
		btnReloadWorlds.addActionListener(arg -> refresh());
		subPanelSaves.add(btnReloadWorlds, "cell 2 0");
		
		JButton btnMapsDir = new JButton("Open maps directory");
		btnMapsDir.setToolTipText("Open 'maps' directory in the file manager");
		btnMapsDir.addActionListener(arg -> {
				try {
					Desktop.getDesktop().open(new File("maps/"));
				} catch (IOException e) {
					debug.pstm(e, "Failed to open maps/ directory");
				}
		});
		subPanelSaves.add(btnMapsDir, "cell 3 0");
		
		JButton btnOpenExternal = new JButton("External");
		btnOpenExternal.setBackground(Color.ORANGE);
		btnOpenExternal.addActionListener(e -> playExternal());
		subPanelSaves.add(btnOpenExternal, "cell 4 0");
		
		EventQueue.invokeLater(this::refresh);
	}
	
	@SuppressWarnings("null")
	private void playExternal() {
		int result = jfc.showOpenDialog(this);
		if(result == JFileChooser.APPROVE_OPTION) {
			play(new Save(new LocalFile(jfc.getSelectedFile())));
		}
	}

	private void play() {
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
		play(s);
	}
	
	private void play(Save s) {
		WorldWindow ww = new WorldWindow();
		FullScreen.setWindow(ww);
		new Thread(() -> {
			try(InputStream in = s.file.getInputStream()) {
				debug.printl("Opened a file");
				String loadedData = IOUtils.toString(in, Charset.defaultCharset());
				debug.printl("Loaded a file");
				Universe world = new Universe();
				@SuppressWarnings("null")
				JsonNode node = JsonTool.parse(loadedData);
				if(node == null) {
					debug.printl("Failed to parse JSON data");
					ww.dispose();
					return;
				}
				debug.printl("Parsed file");
				world.load(node);
				debug.printl("Loaded");
				ww.setWorld(s, world);
			}catch(Exception e) {
				debug.pstm(e, "Failed to load the world");
				ww.dispose();
			}
		}).start();
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
			debug.pstm(e, "THIS EXCEPTION SHOULD NOT HAPPEN");
		}
	}
	

}
