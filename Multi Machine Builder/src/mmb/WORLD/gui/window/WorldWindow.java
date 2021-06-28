/**
 * 
 */
package mmb.WORLD.gui.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import mmb.DATA.json.JsonTool;
import mmb.FILES.Save;
import mmb.MENU.FullScreen;
import mmb.MENU.MMBFrame;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.Blocks;
import mmb.WORLD.gui.BlockPlacer;
import mmb.WORLD.gui.Placer;
import mmb.WORLD.gui.WorldToolList;
import mmb.WORLD.machine.MachineModel;
import mmb.WORLD.player.Player;
import mmb.WORLD.tool.ToolSelectionModel;
import mmb.WORLD.tool.ToolStandard;
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
import javax.swing.JLabel;
import javax.swing.JList;

import mmb.MENU.main.MainMenu;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import mmb.MENU.components.BoundCheckBoxMenuItem;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

/**
 * @author oskar
 *
 */
public class WorldWindow extends MMBFrame{
	private static final long serialVersionUID = -3444481558687472298L;
	private transient Save file;
	private Timer fpsCounter = new Timer();
	
	@Override
	public void destroy() {
		debug.printl("Exiting the world");
		fpsCounter.cancel();
		save();
		panelPlayerInv.dispose();
		worldFrame.enterWorld(null);
		worldFrame.setActive(false);
		FullScreen.setWindow(MainMenu.INSTANCE);
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
	
	/**
	 * Creates a new world window
	 */
	public WorldWindow() {
		setTitle("Test");
		setBounds(100, 100, 824, 445);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			boolean iconified = false;
			boolean open = false;
			@Override
			public void windowClosed(WindowEvent arg0) {
				open = false;
				recalc();
			}
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				iconified = false;
				recalc();
			}
			@Override
			public void windowIconified(WindowEvent arg0) {
				iconified = true;
				recalc();
			}
			@Override
			public void windowOpened(WindowEvent arg0) {
				open = true;
				recalc();
			}
			private void recalc() {
				boolean running = !iconified && open;
				worldFrame.setActive(running);
			}
			
		});
		
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
					//[start] The world frame
						worldFrame = new WorldFrame(this);
						worldFrame.setBackground(Color.GRAY);
						worldFrame.titleChange.addListener(this::updateTitle);
						worldPane.setRightComponent(worldFrame);
					//[end]
					//[start] Scrollable Placement List Pane
						JSplitPane scrollistBipane = new JSplitPane();
						scrollistBipane.setResizeWeight(0.5);
						scrollistBipane.setOrientation(JSplitPane.VERTICAL_SPLIT);
						scrollistBipane.setDividerLocation(0.8);
						//Scrollable Placement List
							scrollablePlacementList = new ScrollablePlacementList(toolModel);
							for(BlockType t: Blocks.blocks) scrollablePlacementList.add(new BlockPlacer(t));
							for(MachineModel m: MachineModel.getMachineModels().values()) scrollablePlacementList.add(m);
							scrollistPane = new JScrollPane();
							scrollistPane.setViewportView(scrollablePlacementList);
							scrollistBipane.setLeftComponent(scrollistPane);
						//Tool Pane
							JScrollPane toolPane = new JScrollPane();
							toolList = new WorldToolList(toolModel, this);
							toolList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
							toolPane.setViewportView(toolList);
							for(int i = 0; i < toolList.model.getSize(); i++) {
								if(toolList.model.elementAt(i) instanceof ToolStandard) {
									toolList.setSelectedIndex(i);
									break;
								}
							}
							scrollistBipane.setRightComponent(toolPane);
						worldPane.setLeftComponent(scrollistBipane);
					//[end]
					worldFrame.setActive(true);
					worldFrame.setPlacer(scrollablePlacementList);
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
						dialogs = new JTabbedPane(SwingConstants.LEFT);
						toolEditorSplitPane.setLeftComponent(dialogs);
		//Menu bar
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
			//Menu
				JMenu mnNewMenu = new JMenu("New menu");
				menuBar.add(mnNewMenu);
				//Full screen
					BoundCheckBoxMenuItem mntmFullScreen = new BoundCheckBoxMenuItem();
					mntmFullScreen.setText("FullScreen");
					mntmFullScreen.setBackground(Color.YELLOW);
					mntmFullScreen.setVariable(FullScreen.isFullScreen);
					mnNewMenu.add(mntmFullScreen);
				//To main menu
					JMenuItem mntmMMenu = new JMenuItem("To main menu");
					mntmMMenu.setBackground(Color.ORANGE);
					mntmMMenu.addActionListener(e -> dispose());
					mnNewMenu.add(mntmMMenu);
				//To desktop
					JMenuItem mntmExitDesktop = new JMenuItem("To desktop");
					mntmExitDesktop.setBackground(Color.RED);
					mntmExitDesktop.addActionListener(e -> {
						dispose();
						System.exit(0);
					});
					mnNewMenu.add(mntmExitDesktop);
				//Debug display
					BoundCheckBoxMenuItem bchckbxmntmDebugDisplay = new BoundCheckBoxMenuItem();
					bchckbxmntmDebugDisplay.setText("Debug display");
					bchckbxmntmDebugDisplay.setVariable(WorldFrame.DEBUG_DISPLAY);
					mnNewMenu.add(bchckbxmntmDebugDisplay);
		//Framerate
		fpsCounter.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				worldFrame.fps.reset();
				if(worldFrame.getMap() != null) {
					worldFrame.getMap().tps.reset();
				}
					
			}
		}, 0, 1000);
	}
	private void updateTitle(String s) {
		StringBuilder sb = new StringBuilder(s).append(' ');
		if(worldFrame.ctrlPressed()) sb.append("[Ctrl]");
		if(worldFrame.altPressed()) sb.append("[Alt]");
		if(worldFrame.shiftPressed()) sb.append("[Shift]");
		setTitle(sb.toString());
	}
	private static Debugger debug = new Debugger("WORLD TEST");
	
	//menu
	private JMenuBar menuBar;
	public void addMenu(Component comp) {
		menuBar.add(comp);
	}
	public void removeMenu(Component comp) {
		menuBar.remove(comp);
	}
	
	//dialogs [BROKEN]
	private JTabbedPane dialogs;
	public void openDialogWindow(Component comp, String s) {
		dialogs.add(s, comp);
	}
	public void closeDialogWindow(Component comp) {
		dialogs.remove(comp);
	}
	
	//tabs
	private JTabbedPane pane;
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

	//tool list
	private WorldToolList toolList;
	
	private JSplitPane toolEditorSplitPane;
	private TabInventory panelPlayerInv;
	private JScrollPane scrollistPane;
	/**
	 * Sets the placement GUI
	 * @param comp
	 */
	public void setPlacerGUI(Component comp) {
		toolEditorSplitPane.setLeftComponent(comp);
	}
	
	/**
	 * @param s save file
	 * @param deserialized new world
	 */
	public void setWorld(Save s, Universe deserialized) {
		file = s;
		worldFrame.enterWorld(deserialized);
		panelPlayerInv.setPlayer(worldFrame.getMap().player);
	}
	/** @return a world which is currently played */
	public Universe getWorld() {
		return worldFrame.getWorld();
	}
	private WorldFrame worldFrame;
	/** @return the WorldFrame associated with this WorldWindow */
	public WorldFrame getWorldFrame() {
		return worldFrame;
	}

	/** @return the BlockMap associated with the WorldFrame */
	public World getMap() {
		return worldFrame.getMap();
	}
	/** @return the Player associated with the world */
	public Player getPlayer() {
		if(worldFrame == null) return null;
		return worldFrame.getPlayer();
	}
	
	//Scrollable Placement List
	public ScrollablePlacementList getPlacer() {
		return scrollablePlacementList;
	}
	public void scrollScrollist(int amount) {
		JScrollBar scrollBar = scrollistPane.getVerticalScrollBar();
		scrollBar.setValue(amount+scrollBar.getValue());
		
	}
	private ScrollablePlacementList scrollablePlacementList;
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
		public ScrollablePlacementList(ToolSelectionModel tsmodel) {
			placers = new DefaultListModel<>();
			setModel(placers);
			setFocusable(false);
			setCellRenderer(new CellRenderer());
			addListSelectionListener(e -> {
				tsmodel.toolSelectedItemList(null);
			});
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
			private static final long serialVersionUID = 5070252011413398383L;
			
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
	
	
	/**
	 * The tool selection. Changes to the model are reflected in the window and vice versa
	 */
	@Nonnull public final ToolSelectionModel toolModel = new ToolSelectionModel(this);
	
	public void redrawUIs() {
		scrollablePlacementList.repaint();
		toolList.repaint();
	}
}
