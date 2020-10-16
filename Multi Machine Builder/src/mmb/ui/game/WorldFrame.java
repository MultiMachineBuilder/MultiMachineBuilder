/**
 * 
 */
package mmb.ui.game;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileSystemException;
import org.joml.Vector2d;
import org.junit.rules.DisableOnDebug;

import mmb.debug.Debugger;
import mmb.files.databuffer.Savers;
import mmb.tileworld.TileGUI;
import mmb.tileworld.TileMap;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author oskar
 *
 */
public class WorldFrame extends JFrame {

	private JPanel contentPane;
	private FileContent save;
	private Debugger debug = new Debugger("WORLD");
	boolean success = false;
	private TileGUI tileGUI;

	/**
	 * Create the frame.
	 */
	public WorldFrame(FileContent file) {
		save = file;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(success) {
					try {
						DataOutputStream dos = new DataOutputStream(save.getOutputStream());
						Savers.mapSaver.save(dos, tileGUI.map);
						dos.close();
						debug.printl("Successfully saved");
					} catch (IOException e) {
						debug.pstm(e, "Failed to save");
					}
				}
			}
		});
		setTitle("World");
		setDefaultCloseOperation(2);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		tileGUI = new TileGUI();
		contentPane.add(tileGUI, BorderLayout.CENTER);
		
		TileMap tm; //Load given save
		try {
			tm = Savers.mapSaver.read(new DataInputStream(file.getInputStream()));
			tileGUI.map = tm;
			tileGUI.offset = new Vector2d(-5, -5);
			success = true;
		} catch (Exception e) {
			debug.pstm(e, "Failed to load world");
			dispose();
		}
	}

}
