/**
 * 
 */
package mmb.WORLD;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import mmb.DATA.json.JsonTool;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.Blocks;
import mmb.WORLD.gui.WorldFrame;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;
import mmb.files.saves.Save;
import javax.swing.JMenuBar;

import com.google.gson.JsonObject;

import javax.swing.JMenu;
import java.awt.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.beans.PropertyChangeEvent;

/**
 * @author oskar
 *
 */
public class WorldWindow extends JFrame implements WindowListener{
	private Save file;
	@Override
	public void dispose() {
		try(OutputStream os = file.file.getOutputStream()) {
			JsonObject object = worldFrame.getWorld().serialize();
			String text = JsonTool.gson.toJson(object);
			byte[] bin = text.getBytes();
			os.write(bin);
			os.flush();
			os.close();
		} catch (Exception e1) {
			debug.pstm(e1, "Failed to write the new world.");
			return;
		}
		worldFrame.enterWorld(null);
		worldFrame.setActive(false);
		
		//future save code
		super.dispose();
	}
	public WorldWindow() {
		//OLD TEST CODE
		/**/
		
		initialize();
	}
	private BlockType[] blocks = Blocks.getBlocks();
	private void initialize() {
		worldFrame = new WorldFrame();
		worldFrame.setBackground(Color.DARK_GRAY);
		getContentPane().add(worldFrame, BorderLayout.CENTER);
		setTitle("Test");
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(this);
		worldFrame.setActive(true);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnInventory = new JMenu("Inventory");
		menuBar.add(mnInventory);
		
		
		list = new List();
		list.setFocusable(false);
		worldFrame.setBlockSelector(list, blocks);
		for(BlockType t: blocks) {
			list.add(t.title);
		}
		getContentPane().add(list, BorderLayout.WEST);
	}
	private static Debugger debug = new Debugger("WORLD TEST");
	private WorldFrame worldFrame;
	private JMenuBar menuBar;
	private JMenu mnInventory;
	private List list;

	/**
	 * @param s
	 * @param deserialize
	 */
	public void setWorld(Save s, World deserialized) {
		file = s;
		worldFrame.enterWorld(deserialized);
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		worldFrame.setActive(false);
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
