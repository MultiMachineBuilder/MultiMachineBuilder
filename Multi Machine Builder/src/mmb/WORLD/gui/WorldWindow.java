/**
 * 
 */
package mmb.WORLD.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import mmb.DATA.json.JsonTool;
import mmb.FILES.Save;
import mmb.MENU.FullScreen;
import mmb.MENU.MMBFrame;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.Blocks;
import mmb.WORLD.machine.MachineModel;
import mmb.WORLD.player.Player;
import mmb.WORLD.tool.Tool;
import mmb.WORLD.worlds.universe.Universe;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

import javax.swing.JMenuBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JSplitPane;
import javax.annotation.Nonnull;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;

import mmb.MENU.main.MainMenu;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import mmb.MENU.components.BoundCheckBoxMenuItem;
import javax.swing.ListSelectionModel;

/**
 * @author oskar
 *
 */
public class WorldWindow extends MMBFrame implements WindowListener{
	private static final long serialVersionUID = -3444481558687472298L;
	private transient Save file;
	private Timer fpsCounter = new Timer();
	
	//[start] static window
	private static WorldWindow currWindow;
	@SuppressWarnings("null")
	@Nonnull public static WorldWindow currentWindow() {
		if(currWindow == null) throw new IllegalStateException("No world window open");
		return currWindow;
	}
	public static WorldWindow ncurrentWorldWindow() {
		return currWindow;
	}
	//[end]	
	@Override
	public void destroy() {
		debug.printl("Exiting the world");
		fpsCounter.cancel();
		save();
		panelPlayerInv.dispose();
		worldFrame.enterWorld(null);
		worldFrame.setActive(false);
		FullScreen.setWindow(MainMenu.INSTANCE);
		currWindow = null;
	}
	/**
	 * Saves the world
	 */
	public void save() {
		if(worldFrame.getWorld() == null) return;
		if(file == null) return;
		JsonNode object = worldFrame.getWorld().save();
		try {
			String text = JsonTool.save(object);
			try(OutputStream os = file.file.getOutputStream()) { //save the world
				byte[] bin = text.getBytes();
				os.write(bin);
				os.flush();
			}
		} catch (Exception e) {
			debug.pstm(e, "Failed to write the new world.");
		}
	}
	
	private WorldFrame worldFrame;	
	/**
	 * Creates a new world window
	 */
	public WorldWindow() {

		setTitle("Test");
		setBounds(100, 100, 824, 445);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		
		//root split pane
			JSplitPane rootPane = new JSplitPane();
			rootPane.setResizeWeight(1.0);
			rootPane.setDividerLocation(0.8);
			getContentPane().add(rootPane, BorderLayout.CENTER);
			//Viewport tabbed pane
				pane = new JTabbedPane();
				//[start] World pane
					JSplitPane worldPane = new JSplitPane();
					worldPane.setDividerLocation(256);
					//[start] Scrollable Placement List Pane
						JSplitPane scrollistBipane = new JSplitPane();
						scrollistBipane.setResizeWeight(0.5);
						scrollistBipane.setOrientation(JSplitPane.VERTICAL_SPLIT);
						scrollistBipane.setDividerLocation(0.8);
						//Scrollable Placement List
							scrollablePlacementList = new ScrollablePlacementList();
							for(BlockType t: Blocks.blocks) scrollablePlacementList.add(new BlockPlacer(t));
							for(MachineModel m: MachineModel.getMachineModels().values()) scrollablePlacementList.add(m);
							scrollistPane = new JScrollPane();
							scrollistPane.setViewportView(scrollablePlacementList);
							scrollistBipane.setLeftComponent(scrollistPane);
						//Tool Pane
							toolPane = new JScrollPane();
							toolList = new JList<>();
							toolList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
							toolPane.setViewportView(toolList);
							scrollistBipane.setRightComponent(toolPane);
						worldPane.setLeftComponent(scrollistBipane);
					//[end]
					//[start] The world frame
						worldFrame = new WorldFrame();
						worldFrame.setBackground(Color.GRAY);
						worldFrame.addTitleListener(this::setTitle);
						worldFrame.setActive(true);
						worldFrame.setPlacer(scrollablePlacementList);
						worldFrame.setWindow(this);
						worldPane.setRightComponent(worldFrame);
					//[end]
					pane.add("World", worldPane);
				//[end]
				//[start] Inventory pane
					panelPlayerInv = new TabInventory();
					pane.addTab("Inventory", panelPlayerInv);
				//[end]
			rootPane.setLeftComponent(pane);
			//tool panel
				//editor split pane: placement/destruction GUI
					toolEditorSplitPane = new JSplitPane();
					toolEditorSplitPane.setDividerLocation(128);
					toolEditorSplitPane.setResizeWeight(0.5);
					toolEditorSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
					rootPane.setRightComponent(toolEditorSplitPane);
					//Editor tabbed pane
						dialogs = new JTabbedPane(JTabbedPane.LEFT);
						toolEditorSplitPane.setLeftComponent(dialogs);
		//Menu bar
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
			//Go to world
				JButton btnGTW = new JButton("Go to world");
				btnGTW.addActionListener(e -> worldFrame.requestFocus());
				btnGTW.setFocusable(false);
				menuBar.add(btnGTW);
			//Menu
				mnNewMenu = new JMenu("New menu");
				menuBar.add(mnNewMenu);
				//Full screen
					mntmFullScreen = new BoundCheckBoxMenuItem();
					mntmFullScreen.setText("FullScreen");
					mntmFullScreen.setBackground(Color.YELLOW);
					mntmFullScreen.setVariable(FullScreen.isFullScreen);
					mnNewMenu.add(mntmFullScreen);
				//To main menu
					mntmMMenu = new JMenuItem("To main menu");
					mntmMMenu.setBackground(Color.ORANGE);
					mntmMMenu.addActionListener(e -> dispose());
					mnNewMenu.add(mntmMMenu);
				//To desktop
					mntmExitDesktop = new JMenuItem("To desktop");
					mntmExitDesktop.setBackground(Color.RED);
					mntmExitDesktop.addActionListener(e -> {
						dispose();
						System.exit(0);
					});
					mnNewMenu.add(mntmExitDesktop);
				//Debug display
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
	
	private JMenuBar menuBar;
	//[start] mod window functions
	private JTabbedPane dialogs;
	
	public void openDialogWindow(Component comp, String s) {
		dialogs.add(s, comp);
	}
	public void closeDialogWindow(Component comp) {
		dialogs.remove(comp);
	}
	
	public void openWindow(Component comp, String s) {
		pane.add(s, comp);
	}
	public void openAndShowWindow(Component comp, String s) {
		pane.add(s, comp);
		pane.setSelectedComponent(comp);
	}
	public void closeWindow(Component component) {
		pane.remove(component);
	}
	//[end]
	
	private JSplitPane toolEditorSplitPane;
	private JMenu mnNewMenu;
	private BoundCheckBoxMenuItem mntmFullScreen;
	private JMenuItem mntmMMenu;
	private JMenuItem mntmExitDesktop;
	private BoundCheckBoxMenuItem bchckbxmntmDebugDisplay;
	private TabInventory panelPlayerInv;
	private JScrollPane scrollistPane;
	/**
	 * Sets the placement GUI
	 * @param comp
	 */
	public void setPlacerGUI(Component comp) {
		toolEditorSplitPane.setLeftComponent(comp);
	}
	
	//[start] world functions
	/**
	 * @param s save file
	 * @param deserialized new world
	 */
	public void setWorld(Save s, Universe deserialized) {
		file = s;
		worldFrame.enterWorld(deserialized);
		panelPlayerInv.setPlayer(worldFrame.getMap().player);
	}
	/** @return a world which is currently played*/
	public Universe getWorld() {
		return worldFrame.getWorld();
	}
	/**
	 * @return the WorldFrame associated with this WorldWindow
	 */
	public WorldFrame getWorldFrame() {
		return worldFrame;
	}
	/**
	 * @return the BlockMap associated with the WorldFrame
	 */
	public World getMap() {
		return worldFrame.getMap();
	}
	/**
	 * @return the Player associated with the world
	 */
	public Player getPlayer() {
		return worldFrame.getMap().player;
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
	//[start] Scrollable Placement List
	public ScrollablePlacementList getPlacer() {
		return scrollablePlacementList;
	}
	public void scrollScrollist(int amount) {
		JScrollBar scrollBar = scrollistPane.getVerticalScrollBar();
		scrollBar.setValue(amount+scrollBar.getValue());
		
	}
	private ScrollablePlacementList scrollablePlacementList;
	private JTabbedPane pane;
	private JScrollPane toolPane;
	private JList<Tool> toolList;
	/**
	 * @author oskar
	 * A {@code ScrollablePlacementList} is used to select a block or machine
	 */
	public class ScrollablePlacementList extends JList<Placer>{
		public final Debugger debug = new Debugger("BLOCK LIST");

		private static final long serialVersionUID = -208562764791915412L;
		/**
		 * A list of all placers that player can choose
		 */
		public final DefaultListModel<Placer> placers;
		/**
		 * @return the index, at which the placer is selected
		 * @deprecated Now implements JList. Use getSelectedIndex() instead. To be removed in 0.6
		 */
		@Deprecated
		public int getPlacerIndex() {
			return getSelectedIndex();
		}
		/**
		 * @param placerIndex the index of a placer within the ScrollablePlacementList
		 * @deprecated Now implements JList. Use getSelectedIndex() instead. To be removed in 0.6
		 */
		public void setPlacerIndex(int placerIndex) {
			setSelectedIndex(placerIndex);
		}

		/**
		 * @return the placer
		 * @deprecated Now implements JList. Use getSelectedValue() instead. To be removed in 0.6
		 */
		@Deprecated
		public Placer getPlacer() {
			return getSelectedValue();
		}
		/**
		 * @param placer the placer to set
		 * @deprecated Now implements JList. Use setSelectedValue() instead. To be removed in 0.6
		 */
		@Deprecated
		public void setPlacer(Placer placer) {
			setSelectedValue(placer, true);
		}
		
		/**
		 * Creates a new ScrollablePlacementList.
		 * @discouraged To be used only in WorldWindow code
		 */
		public ScrollablePlacementList() {
			placers = new DefaultListModel<>();
			setModel(placers);
			setFocusable(false);
			setCellRenderer(new CellRenderer());
			
		}
		/**
		 * A convienience method to add a placer
		 * @param arg0 the placer to be added
		 * @see java.util.List#add(java.lang.Object)
		 */
		public void add(Placer arg0) {
			placers.addElement(arg0);
		}

		/**
		 * A convenience method to remove a placer
		 * @param arg0 the placer to be removed
		 * @see java.util.List#remove(java.lang.Object)
		 */
		public void remove(Placer arg0) {
			placers.removeElement(arg0);
		}

		/**
		 * @return number of registered placers
		 * @see java.util.List#size()
		 */
		public int listSize() {
			return placers.size();
		}

		private final class CellRenderer extends JLabel implements ListCellRenderer<Placer>{
			public CellRenderer() {
				setOpaque(true);
			}
			@Override
			public Component getListCellRendererComponent(JList<? extends Placer> list, Placer placer, int index,
					boolean isSelected, boolean cellHasFocus) {
				setIcon(placer.getIcon());
				setText(placer.title());
				
				if (isSelected) {
				    setBackground(list.getSelectionBackground());
				    setForeground(list.getSelectionForeground());
				} else {
				    setBackground(list.getBackground());
				    setForeground(list.getForeground());
				}
				
				return this;
			}
			
		}
		
		/**
		 * @return an associated WorldWindow
		 */
		public WorldWindow getWindow() {
			return WorldWindow.this;
		}
	}
	//[end]
	
	
}
