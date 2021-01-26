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
import mmb.DATA.json.JsonTool;
import mmb.MENU.NewWorld.NewGame;
import mmb.WORLD.WorldWindow;
import mmb.WORLD.gui.WorldFrame;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;
import mmb.files.saves.Save;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
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
		btnPlay.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				int index = list.getSelectedIndex();
				if(index < 0) {
					debug.printl("No selected world!");
					return;
				}
				Save s = saves.get(list.getSelectedIndex());
				WorldWindow nww = new WorldWindow();
				nww.setVisible(true);
				try(InputStream in = s.file.getInputStream()) {
					System.gc();
					JsonParser parser = new JsonParser();
					String loadedData = IOUtils.toString(in, Charset.defaultCharset());
					JsonElement e = parser.parse(loadedData);
					nww.setWorld(s, World.deserialize(e.getAsJsonObject(), s.name));
				}catch(Exception e) {
					nww.dispose();
					debug.pstm(e, "Failed to load the world");
				}
			}});
		btnPlay.setBackground(Color.GREEN);
		btnPlay.setForeground(Color.BLACK);
		subPanelSaves.add(btnPlay, "cell 0 0");
		
		
		/*
		 * Create a new world.
		 * In 0.5 this menu will significantly be enhanced
		 */
		JButton btnNewWorld = new JButton("New world");
		btnNewWorld.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				new NewGame().setVisible(true);
			}});
		subPanelSaves.add(btnNewWorld, "cell 1 0");
		
		
		JButton btnReloadWorlds = new JButton("Refresh");
		btnReloadWorlds.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				refresh();
			}});
		subPanelSaves.add(btnReloadWorlds, "cell 2 0");
		
		btnNewButton = new JButton("Open maps directory");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().open(new File("maps/"));
				} catch (IOException e) {
					debug.pstm(e, "Failed to open maps/ directory");
				}
			}
		});
		subPanelSaves.add(btnNewButton, "cell 3 0");
		EventQueue.invokeLater(() -> refresh());
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
			debug.printl("Found "+list.getItemCount()+" saves");
		} catch (Exception e) {
			debug.pstm(e, "THIS EXCEPTION SHOULD NOT HAPPEN");
		}
	}
	

}
