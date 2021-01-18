/**
 * 
 */
package mmb.WORLD_new;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mmb.WORLD.tileworld.DrawerPlainColor;
import mmb.WORLD_new.block.BlockType;
import mmb.WORLD_new.gui.WorldFrame;
import mmb.WORLD_new.worlds.MapProxy;
import mmb.WORLD_new.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class WorldWindow extends JFrame {

	private JPanel contentPane;
	private WorldFrame panel;
	private Debugger debug = new Debugger("WORLD TEST");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WorldWindow frame = new WorldWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WorldWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		panel = new WorldFrame();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.addTitleListener((t) -> {setTitle(t);});
		
		World w = World.createBlankWorld(new Dimension(11, 11));
		w.setName("Test");
		
		BlockType test = new BlockType();
		test.drawer = new DrawerPlainColor(Color.GREEN);
		
		try(MapProxy mp = w.main.createVolatileProxy()){
			mp.setImmediately(test, 3, 3);
		} catch (Exception e) {
			debug.pstm(e, "Failed to populate the map");
		}
		
		panel.enterWorld(w);
		
		if(panel.getMap() == null) debug.printl("Map not properly loaded");
	}

}
