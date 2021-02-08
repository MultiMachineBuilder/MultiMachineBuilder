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
import javax.swing.JPanel;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import mmb.DATA.file.AdvancedFile;
import mmb.DATA.file.LocalFile;
import mmb.FILES.Save;
import mmb.MENU.NewWorld.NewGame;
import mmb.WORLD.gui.WorldWindow;
import mmb.WORLD.worlds.world.World;
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
	private static final long serialVersionUID = -873377369477463310L;
	
	private final transient Debugger debug = new Debugger("SELECT A SAVE");
	/**The list of all saves*/
	public final transient java.util.List<Save> saves = new ArrayList<>();
	private List list;
	private JButton btnNewButton;
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
		
		
		//use the old world system
		JButton btnPlay = new JButton("Play");
		btnPlay.setToolTipText("Play the selected world");
		btnPlay.addActionListener(arg0 -> {
			int index = list.getSelectedIndex();
			if(index < 0) {
				debug.printl("No selected world!");
				return;
			}
			Save s = saves.get(list.getSelectedIndex());
			WorldWindow nww = new WorldWindow();
			nww.setVisible(true);
			try(InputStream in = s.file.getInputStream()) {
				JsonParser parser = new JsonParser();
				String loadedData = IOUtils.toString(in, Charset.defaultCharset());
				JsonElement e = parser.parse(loadedData);
				nww.setWorld(s, World.deserialize(e.getAsJsonObject(), s.name));
			}catch(Exception e) {
				nww.dispose();
				debug.pstm(e, "Failed to load the world");
			}
		});
		btnPlay.setBackground(Color.GREEN);
		subPanelSaves.add(btnPlay, "cell 0 0");
		
		
		/*
		 * Create a new world.
		 * In 0.5 this menu will significantly be enhanced
		 */
		JButton btnNewWorld = new JButton("New world");
		btnNewWorld.setToolTipText("Create a new world");
		btnNewWorld.addActionListener(arg -> new NewGame().setVisible(true));
		subPanelSaves.add(btnNewWorld, "cell 1 0");
		
		
		JButton btnReloadWorlds = new JButton("Refresh");
		btnReloadWorlds.setToolTipText("Refresh save list");
		btnReloadWorlds.addActionListener(arg -> refresh());
		subPanelSaves.add(btnReloadWorlds, "cell 2 0");
		
		btnNewButton = new JButton("Open maps directory");
		btnNewButton.setToolTipText("Open 'maps' directory in the file manager");
		btnNewButton.addActionListener(arg -> {
				try {
					Desktop.getDesktop().open(new File("maps/"));
				} catch (IOException e) {
					debug.pstm(e, "Failed to open maps/ directory");
				}
		});
		subPanelSaves.add(btnNewButton, "cell 3 0");
		EventQueue.invokeLater(() -> refresh()); //method reference does not work
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
