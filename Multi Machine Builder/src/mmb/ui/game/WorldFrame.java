/**
 * 
 */
package mmb.ui.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.joml.Vector2d;
import org.junit.rules.DisableOnDebug;

import mmb.DATA.file.AdvancedFile;
import mmb.DATA.save.SaveLoad;
import mmb.WORLD.tileworld.REWORK.TileGUI;
import mmb.WORLD.tileworld.map.TileMap;
import mmb.WORLD.tileworld.map.World;
import mmb.debug.Debugger;
import mmb.files.databuffer.Savers;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

/**
 * @author oskar
 *
 */
public class WorldFrame extends JFrame {

	private final JPanel contentPane;
	private final AdvancedFile save;
	private final Debugger debug = new Debugger("WORLD");
	boolean success = false;
	private final TileGUI tileGUI;

	/**
	 * Create the frame.
	 */
	public WorldFrame(AdvancedFile file) {
		save = file;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				tileGUI.destroyTimer();
				if(success) {
					try {
						save();
						debug.printl("Successfully saved");
					} catch (Exception e) {
						debug.pstm(e, "Failed to save");
					}
				}
			}
		});
		setTitle("World - Left click to edit the block. The game saves automatically.");
		setDefaultCloseOperation(2);
		setBounds(100, 100, 685, 483);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		tileGUI = new TileGUI();
		contentPane.add(tileGUI, BorderLayout.CENTER);
		
		//tools = new ToolBar();
		//contentPane.add(tileGUI, BorderLayout.EAST);
		//Dimension size = tools.getPreferredSize();
		//size.width = 64;
		//tools.setSize(size);
		
		TileMap tm; //Load given save
		try {
			load(); //Load the world
			tileGUI.offset = new Vector2d(-5, -5);
			success = true;
			tileGUI.setActive(true);
			
			JPanel panel = new JPanel();
			contentPane.add(panel, BorderLayout.NORTH);
			
			JButton survivalInv = new JButton("Survival Inventory & Crafting");
			panel.add(survivalInv);
			
			JLabel remaining = new JLabel("x items left");
			panel.add(remaining);
		} catch (Exception e) {
			debug.pstm(e, "Failed to load world");
			dispose();
		}
	}
	
	private void load() {
		try {
			debug.printl("Retrieving the file");
			String get = readIS(save.getInputStream());
			debug.printl("Parsing world data");
			World newWorld = SaveLoad.load(get);
			tileGUI.map = newWorld;
		} catch (Exception e) {
			debug.pstm(e, "Failed to load the world");
		}
	}
	private void save() throws IOException {
		debug.printl("Stringyfiyng the world");
		String result = SaveLoad.save(tileGUI.map); //Stringify
		debug.printl("Opening data stream");
		OutputStream os = save.getOutputStream(); //Open
		debug.printl("Encoding JSON data");
		byte[] data = result.getBytes(); //Encode the world
		debug.printl("Writing byte data");
		os.write(data); //Write
		debug.printl("Closing data stream");
		os.flush();
		os.close();
	}
	
	//https://www.tutorialspoint.com/how-to-convert-inputstream-object-to-a-string-in-java
	private String readIS(InputStream in) throws IOException {
		  debug.printl("Opening data stream");
	      InputStreamReader isReader = new InputStreamReader(in);
	      //Creating a BufferedReader object
	      debug.printl("Preparing loading space");
	      BufferedReader reader = new BufferedReader(isReader);
	      StringBuilder sb = new StringBuilder();
	      String str;
	      debug.printl("Decoding text data");
	      while((str = reader.readLine())!= null){
	         sb.append(str);
	      }
	      return sb.toString();
	}

}
