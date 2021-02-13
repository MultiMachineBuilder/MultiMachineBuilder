/**
 * 
 */
package mmb.WORLD.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import mmb.DATA.json.JsonTool;
import mmb.FILES.Save;
import mmb.MENU.FullScreen;
import mmb.MENU.MMBFrame;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.Blocks;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

import javax.swing.JMenuBar;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.OutputStream;
import javax.swing.JSplitPane;
import javax.swing.JButton;

import mmb.MENU.components.BoundCheckBox;
import mmb.MENU.main.MainMenu;

/**
 * @author oskar
 *
 */
public class WorldWindow extends MMBFrame implements WindowListener{
	private static final long serialVersionUID = -3444481558687472298L;
	
	private transient Save file;
	
	public void destroy() {
		if(worldFrame.getWorld() != null) {
			JsonNode object = worldFrame.getWorld().save();
			String text;
			try {
				text = JsonTool.save(object);
				if(file != null) try(OutputStream os = file.file.getOutputStream()) { //save the world
					byte[] bin = text.getBytes();
					os.write(bin);
					os.flush();
				}
			} catch (Exception e) {
				debug.pstm(e, "Failed to write the new world.");
			}
		}
		worldFrame.enterWorld(null);
		worldFrame.setActive(false);
		FullScreen.setWindow(MainMenu.INSTANCE);
	}
	public WorldWindow() {
		initialize();
	}
	private transient BlockType[] blocks = Blocks.getBlocks();
	private WorldFrame worldFrame;
		
	private void initialize() {
		setTitle("Test");
		setBounds(100, 100, 451, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(this);
		
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
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		btnNewButton = new JButton("Inventory");
		menuBar.add(btnNewButton);
		btnNewButton.setFocusable(false);
		
		fullScreenControl = new BoundCheckBox("FullScreen");
		fullScreenControl.setVariable(FullScreen.isFullScreen);
		menuBar.add(fullScreenControl);
		
		btnNewButton_1 = new JButton("<<<");
		btnNewButton_1.addActionListener(e -> dispose());
		btnNewButton_1.setBackground(Color.ORANGE);
		menuBar.add(btnNewButton_1);
	}
	private static Debugger debug = new Debugger("WORLD TEST");
	private ScrollablePlacementList scrollablePlacementList;
	private JSplitPane splitPane;
	private JButton btnNewButton;
	private JMenuBar menuBar;
	private BoundCheckBox fullScreenControl;
	private JButton btnNewButton_1;

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
