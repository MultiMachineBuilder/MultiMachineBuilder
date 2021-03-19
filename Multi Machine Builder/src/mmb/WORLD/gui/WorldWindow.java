/**
 * 
 */
package mmb.WORLD.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import mmb.DATA.json.JsonTool;
import mmb.FILES.Save;
import mmb.MENU.FullScreen;
import mmb.MENU.MMBFrame;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.Blocks;
import mmb.WORLD.machine.MachineModel;
import mmb.WORLD.worlds.universe.Universe;
import mmb.debug.Debugger;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JSplitPane;
import javax.annotation.Nonnull;
import javax.swing.JButton;

import mmb.MENU.main.MainMenu;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import mmb.MENU.components.BoundCheckBoxMenuItem;

/**
 * @author oskar
 *
 */
public class WorldWindow extends MMBFrame implements WindowListener{
	private static final long serialVersionUID = -3444481558687472298L;
	private transient Save file;
	private Timer fpsCounter = new Timer();
	
	private static WorldWindow currWindow;
	@SuppressWarnings("null")
	@Nonnull public static WorldWindow currentWindow() {
		if(currWindow == null) throw new IllegalStateException("No world window open");
		return currWindow;
	}
	public static WorldWindow ncurrentWorldWindow() {
		return currWindow;
	}
	
	@Override
	public void destroy() {
		debug.printl("Exiting the world");
		fpsCounter.cancel();
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
		currWindow = null;
	}
	private transient BlockType[] blocks = Blocks.getBlocks();
	private WorldFrame worldFrame;	
	/**
	 * Creates a new world window
	 */
	public WorldWindow() {

		setTitle("Test");
		setBounds(100, 100, 451, 300);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		
		//root split pane
		splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);
		
		//right split pane
		JSplitPane rightSplitPane = new JSplitPane();
		rightSplitPane.setResizeWeight(1.0);
		rightSplitPane.setDividerLocation(0.8);
		splitPane.setRightComponent(rightSplitPane);
		
		//tool panel
		JPanel toolPanel = new JPanel();
		toolPanel.setPreferredSize(new Dimension(128, rightSplitPane.getHeight()));
		rightSplitPane.setRightComponent(toolPanel);
		toolPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		//editor split pane: placement/destruction GUI
		toolEditorSplitPane = new JSplitPane();
		toolEditorSplitPane.setResizeWeight(0.5);
		toolEditorSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		toolPanel.add(toolEditorSplitPane, "cell 0 0,grow");
		
		//Editor tabbed pane
		dialogs = new JTabbedPane(SwingConstants.TOP);
		toolEditorSplitPane.setLeftComponent(dialogs);
		
		//Scrollable Placement List
		scrollablePlacementList = new ScrollablePlacementList();
		for(BlockType t: blocks) scrollablePlacementList.add(new BlockPlacer(t));
		for(MachineModel m: MachineModel.getMachineModels().values()) scrollablePlacementList.add(m);
		
		//The world frame
		worldFrame = new WorldFrame();
		worldFrame.setBackground(Color.DARK_GRAY);
		worldFrame.addTitleListener(this::setTitle);
		worldFrame.setActive(true);
		worldFrame.setPlacer(scrollablePlacementList);
		worldFrame.setWindow(this);
		rightSplitPane.setLeftComponent(worldFrame);
		
		splitPane.setLeftComponent(scrollablePlacementList);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JButton btnGTW = new JButton("Go to world");
		btnGTW.addActionListener(e -> worldFrame.requestFocus());
		btnGTW.setFocusable(false);
		menuBar.add(btnGTW);
		
		mnNewMenu = new JMenu("New menu");
		menuBar.add(mnNewMenu);
		
		mntmNewMenuItem = new JMenuItem("Inventory");
		mnNewMenu.add(mntmNewMenuItem);
		
		mntmFullScreen = new BoundCheckBoxMenuItem();
		mntmFullScreen.setText("FullScreen");
		mntmFullScreen.setBackground(Color.YELLOW);
		mntmFullScreen.setVariable(FullScreen.isFullScreen);
		mnNewMenu.add(mntmFullScreen);
		
		mntmMMenu = new JMenuItem("To main menu");
		mntmMMenu.setBackground(Color.ORANGE);
		mntmMMenu.addActionListener(e -> dispose());
		mnNewMenu.add(mntmMMenu);
		
		mntmExitDesktop = new JMenuItem("To desktop");
		mntmExitDesktop.setBackground(Color.RED);
		mntmExitDesktop.addActionListener(e -> {
			dispose();
			System.exit(0);
		});
		mnNewMenu.add(mntmExitDesktop);
		
		bchckbxmntmDebugDisplay = new BoundCheckBoxMenuItem();
		bchckbxmntmDebugDisplay.setText("Debug display");
		bchckbxmntmDebugDisplay.setVariable(WorldFrame.DEBUG_DISPLAY);
		mnNewMenu.add(bchckbxmntmDebugDisplay);
		
		//Framerate
		fpsCounter.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				worldFrame.fps.reset();
				if(worldFrame.getMap() != null)
					worldFrame.getMap().tps.reset();
			}
		}, 0, 1000);
		currWindow = this;
	}
	private static Debugger debug = new Debugger("WORLD TEST");
	private ScrollablePlacementList scrollablePlacementList;
	public ScrollablePlacementList getPlacer() {
		return scrollablePlacementList;
	}
	private JSplitPane splitPane;
	private JMenuBar menuBar;
	//[start] mod window functions
	private JTabbedPane dialogs;
	public void openDialogWindow(Component comp, String s) {
		dialogs.add(s, comp);
	}
	public void closeDialogWindow(Component comp) {
		dialogs.remove(comp);
	}
	private JSplitPane toolEditorSplitPane;
	private JMenu mnNewMenu;
	private JMenuItem mntmNewMenuItem;
	private BoundCheckBoxMenuItem mntmFullScreen;
	private JMenuItem mntmMMenu;
	private JMenuItem mntmExitDesktop;
	private BoundCheckBoxMenuItem bchckbxmntmDebugDisplay;
	public void setPlacerGUI(Component comp) {
		toolEditorSplitPane.setLeftComponent(comp);
	}
	//[end]
	//[start] world functions
	/**
	 * @param s save file
	 * @param deserialized new world
	 */
	public void setWorld(Save s, Universe deserialized) {
		file = s;
		worldFrame.enterWorld(deserialized);
	}
	/** @return a world which is currently played*/
	public Universe getWorld() {
		return worldFrame.getWorld();
	}
	public WorldFrame getWorldFrame() {
		return worldFrame;
	}
	//[end]
	//[start] WindowListener
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
	//[end]
	
}
