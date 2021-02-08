/**
 * 
 */
package mmb.WORLD.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import mmb.DATA.json.JsonTool;
import mmb.FILES.Save;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.Blocks;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

import javax.swing.JMenuBar;

import com.google.gson.JsonObject;

import java.io.OutputStream;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JButton;

/**
 * @author oskar
 *
 */
public class WorldWindow extends JFrame implements WindowListener{
	private static final long serialVersionUID = -3444481558687472298L;
	
	private transient Save file;
	@Override
	public void dispose() {
		try(OutputStream os = file.file.getOutputStream()) {
			JsonObject object = worldFrame.getWorld().serialize();
			String text = JsonTool.gson.toJson(object);
			byte[] bin = text.getBytes();
			os.write(bin);
			os.flush();
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
		initialize();
	}
	private transient BlockType[] blocks = Blocks.getBlocks();
	private WorldFrame worldFrame;
	private void initialize() {
		setTitle("Test");
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(this);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		FlowLayout topPanelLayout = new FlowLayout();
		topPanelLayout.setHgap(0);
		topPanelLayout.setVgap(0);
		topPanelLayout.setAlignment(FlowLayout.LEADING);
		topPanel = new JPanel();
		topPanel.setLayout(topPanelLayout);
		menuBar.add(topPanel);
		
		
		btnNewButton = new JButton("Inventory");
		topPanel.add(btnNewButton);
		
		
		splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);
		worldFrame = new WorldFrame();
		splitPane.setRightComponent(worldFrame);
		worldFrame.setBackground(Color.DARK_GRAY);
		worldFrame.addTitleListener(this::setTitle);
		worldFrame.setActive(true);
		
		scrollablePlacementList = new ScrollablePlacementList();
		for(BlockType t: blocks) {
			scrollablePlacementList.add(new BlockPlacer(t));
		}
		
		splitPane.setLeftComponent(scrollablePlacementList);
		worldFrame.setPlacer(scrollablePlacementList);
	}
	private static Debugger debug = new Debugger("WORLD TEST");
	private JMenuBar menuBar;
	private ScrollablePlacementList scrollablePlacementList;
	private JSplitPane splitPane;
	private JPanel topPanel;
	private JButton btnNewButton;

	/**
	 * @param s save file
	 * @param deserialized new world
	 */
	public void setWorld(Save s, World deserialized) {
		file = s;
		worldFrame.enterWorld(deserialized);
	}
	@Override
	public void windowActivated(WindowEvent e) {
		//unused
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		//unused
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		worldFrame.setActive(false);
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		//unused
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		worldFrame.setActive(true);
	}
	@Override
	public void windowIconified(WindowEvent e) {
		worldFrame.setActive(false);
	}
	@Override
	public void windowOpened(WindowEvent e) {
		//unused
	}
}
