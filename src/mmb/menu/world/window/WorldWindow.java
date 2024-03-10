/**
 * 
 */
package mmb.menu.world.window;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import com.fasterxml.jackson.databind.JsonNode;
import com.pploder.events.Event;

import io.github.parubok.text.multiline.MultilineLabel;
import mmb.NN;
import mmb.Nil;
import mmb.data.variables.ListenableValue;
import mmb.engine.CatchingEvent;
import mmb.engine.debug.Debugger;
import mmb.engine.files.Save;
import mmb.engine.inv.ItemRecord;
import mmb.engine.item.Items;
import mmb.engine.json.JsonTool;
import mmb.engine.recipe.Recipe;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.worlds.universe.Universe;
import mmb.engine.worlds.world.Player;
import mmb.engine.worlds.world.World;
import mmb.menu.FullScreen;
import mmb.menu.MMBFrame;
import mmb.menu.components.BoundCheckBoxMenuItem;
import mmb.menu.main.MainMenu;
import mmb.menu.world.inv.InventoryController;
import mmb.menu.wtool.ToolStandard;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JSplitPane;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JProgressBar;

/**
 * @author oskar
 *
 *
 * <h2>WINDOW TABS</h2>
 * {@link #openWindow(GUITab, String)} - opens a tab without going to it
 * {@link #openAndShowWindow(GUITab, String)} - opens a tab and shows it
 * {@link #closeWindow(GUITab)} - closes a tab
 */
public class WorldWindow extends MMBFrame{
	private static final long serialVersionUID = -3444481558687472298L;
	private transient Save file;
	private Timer fpsCounter = new Timer();
	private boolean destroyRunning = false;
	
	@Override
	public void destroy() {
		if(destroyRunning) return;
		try {
			destroyRunning = true;
			debug.printl("Exiting the world");
			fpsCounter.cancel();
			save();
			panelPlayerInv.dispose();
			worldFrame.setActive(false);
			//This gets stuck
			worldFrame.enterWorld(null);
			FullScreen.setWindow(MainMenu.INSTANCE); //this gets stuck ONLY if the world is broken
			debug.printl("Exited the world");
		}finally {
			destroyRunning = false;
		}
	}
	/**
	 * Saves the world
	 */
	public void save() {
		if(worldFrame.getWorld() == null) return;
		if(file == null) return;
		JsonNode object = worldFrame.getWorld().save();
		try {
			debug.printl("Saving the world");
			String text = JsonTool.save(object);
			try(OutputStream os = file.file.getOutputStream()) { //save the world
				byte[] bin = text.getBytes();
				os.write(bin);
				os.flush();
			}
			debug.printl("Saved the world");
		} catch (Exception e) {
			debug.stacktraceError(e, "Failed to write the new world.");
		}
	}
	
	/** The default tool */
	public final ToolStandard std;
	/** Creates a new world window */
	public WorldWindow() {
		debug.printl(Items.items.size()+" items");
		setTitle("Test");
		setBounds(100, 100, 950, 445);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setIconImage(MMBFrame.GEAR);
		addWindowListener(new WindowAdapter() {
			boolean iconified = false;
			boolean open = false;
			@Override
			public void windowClosed(@SuppressWarnings("null") WindowEvent arg0) {
				open = false;
				recalc();
			}
			@Override
			public void windowDeiconified(@SuppressWarnings("null") WindowEvent arg0) {
				iconified = false;
				recalc();
			}
			@Override
			public void windowIconified(@SuppressWarnings("null") WindowEvent arg0) {
				iconified = true;
				recalc();
			}
			@Override
			public void windowOpened(@SuppressWarnings("null") WindowEvent arg0) {
				open = true;
				recalc();
			}
			private void recalc() {
				boolean running = !iconified && open;
				worldFrame.setActive(running);
			}
		});
		
		//root split pane
				pane = new JTabbedPane();
				worldtab = new TabWorld(this);
				std = worldtab.std;
				selModel = worldtab.selModel;
				invModel = worldtab.invModel;
				this.toolEditorSplitPane = new JSplitPane();
				toolModel = worldtab.toolModel;
				worldFrame = worldtab.worldFrame;
				lblTool = worldtab.lblTool;
				lblStatus = worldtab.lblStatus;
				scrollablePlacementList = worldtab.scrollablePlacementList;
				toolList = worldtab.toolList;
				progressHP = worldtab.progressHP;
				
				toolList.setSelectedIndex(worldtab.stdIndex);
				pane.add($res("wgui-world"), worldtab);
				//[start] Inventory pane
					panelPlayerInv = new TabInventory(this);
					panelPlayerInv.craftGUI.inventoryController.setModel(invModel);
					panelPlayerInv.craftGUI.inventoryController.setSelectionModel(selModel);
					pane.addTab($res("wgui-inv"), panelPlayerInv);
				//[end]
				//[start] Recipe pane
					TabRecipes recipePane = new TabRecipes();
					pane.addTab($res("wgui-recipes"), null, recipePane, null);
				//[end]
			getContentPane().add(pane, BorderLayout.CENTER);
					
					String scale = $res("wgui-scale");
					
					debug.printl("Number of zoom levels: "+WorldFrame.zoomlevels.size()); //48
					
					//Menu bar
						//Menu
							JMenu mnGame = new JMenu($res("wgui-game"));
							menuBar.add(mnWindow);
							menuBar.add(mnGame);
							checkBindCameraPlayer = new JCheckBoxMenuItem($res("wgui-bound"));
							menuBar.add(checkBindCameraPlayer);
							
							//Debug display
								BoundCheckBoxMenuItem cDebugDisplay = new BoundCheckBoxMenuItem();
								cDebugDisplay.setText($res("wgui-debug"));
								cDebugDisplay.setVariable(WorldFrame.DEBUG_DISPLAY);
								mnGame.add(cDebugDisplay);
								
								JLabel lblBlockScale = new JLabel(scale+" 32");
								mnGame.add(lblBlockScale);
								
								JScrollBar slideBlockScale = new JScrollBar();
								slideBlockScale.setValue(27);
								slideBlockScale.setMaximum(WorldFrame.zoomlevels.size()+9); //strangely, the value goes up to only 37
								debug.printl("Scrollbar max: "+slideBlockScale.getMaximum()); //48
								slideBlockScale.addAdjustmentListener(e -> {
									worldFrame.setZoom(e.getValue());
									lblBlockScale.setText(scale+" "+worldFrame.getBlockScale());
									debug.printl("Scale: "+e.getValue());
								});
								slideBlockScale.setOrientation(Adjustable.HORIZONTAL);
								mnGame.add(slideBlockScale);					
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

	private static final String str_spd = $res("wgui-kmh");
	private static final String str_spdtrue = $res("wgui-kmhtrue");
	private static final String str_spdphys = $res("wgui-kmhphys");
	void updateTitle(String s) {
		//Update the status
		if(getPlayer() != null) {
			StringBuilder status = new StringBuilder(str_spd+" ");
			double speedMPST = getPlayer().speedTrue.length();
			status.append(speedMPST * 3.6);
			status.append(" ");
			status.append(str_spdtrue);
			lblStatus.setText(status.toString());
			
			status.append(", ");
			double speedMPSP = getPlayer().speed.length();
			status.append(speedMPSP * 3.6);
			status.append(" ");
			status.append(str_spdphys);
			
			status.append(getPlayer().physics.description());
			lblStatus.setText(status.toString());
		}
		
		StringBuilder sb = new StringBuilder(s).append(' ');
		if(worldFrame.ctrlPressed()) sb.append("[Ctrl]");
		if(worldFrame.altPressed()) sb.append("[Alt]");
		if(worldFrame.shiftPressed()) sb.append("[Shift]");
		setTitle(sb.toString());
		
		String oldDescription = lblTool.getText();
		String tool = toolModel.getTool().description();
		if(oldDescription.equals(tool)) return;
		
		lblTool.setText(tool);
	}
	@NN private static final Debugger debug = new Debugger("WORLD TEST");
	
	
	
	//World tab elements
	//tool list
	public final TabWorld worldtab;
	public final ListSelectionModel selModel;
	public final DefaultListModel<ItemRecord> invModel;
	public final WorldToolList toolList;
	public final MultilineLabel lblTool;
	public final JSplitPane toolEditorSplitPane;
	/** Player inventory panel */
	public final TabInventory panelPlayerInv;
	private JScrollPane scrollistPane;
	/**
	 * Sets the placement GUI
	 * @param comp
	 */
	public void setPlacerGUI(Component comp) {
		toolEditorSplitPane.setLeftComponent(comp);
	}
	public final JProgressBar progressHP;
	/** The tool selection. Changes to the model are reflected in the window and vice versa */
	@NN public final transient ToolSelectionModel toolModel;
	
	//tabs
	private JTabbedPane pane;
	public void openWindow(GUITab comp, String s) {
		pane.add(s, comp);
	}
	public void openAndShowWindow(GUITab comp, String s) {
		pane.add(s, comp);
		pane.setSelectedComponent(comp);
	}
	
	/**
	 * Closes a tab. Its destroyTab() method is called to dispose of resources
	 * @param component
	 */
	public void closeWindow(GUITab component) {
		try {
			component.close(this);
		} catch (Exception e) {
			debug.stacktraceError(e, "Failed to shut down the component");
		}
		pane.remove(component);
	}
	
	/**
	 * @param s save file
	 * @param deserialized new world
	 */
	public void setWorld(Save s, Universe deserialized) {
		file = s;
		worldFrame.enterWorld(deserialized);
		panelPlayerInv.setPlayer(worldFrame.getMap().player);
		progressHP.setModel(worldFrame.getMap().player.playerHP);
		scrollablePlacementList.setInv(worldFrame.getMap().player.inv);
		panelPlayerInv.lblGamemode.setText(
			worldFrame.getMap().player.creative.getValue() ? 
			GlobalSettings.$res("wgui-creamode") 
		  : GlobalSettings.$res("wgui-survival"));
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
	@NN private ScrollablePlacementList scrollablePlacementList;
	/**
	 * @author oskar
	 * A {@code ScrollablePlacementList} is used to select a block or machine
	 */
	public class ScrollablePlacementList extends InventoryController{
		private static final long serialVersionUID = -208562764791915412L;
		
		ScrollablePlacementList(ToolSelectionModel tsmodel) {
			setFocusable(false);
			addListSelectionListener(e -> {
				ItemRecord irecord = getSelectedValue();
				if(irecord == null) {
					tsmodel.toolSelectedItemList(null);
				}else {
					tsmodel.toolSelectedItemList(irecord.item().getTool());
				}
				
			});
		}
		
		/** @return an associated WorldWindow */
		public WorldWindow getWindow() {
			return WorldWindow.this;
		}
	}
	
	
	
	private JCheckBoxMenuItem checkBindCameraPlayer;
	private final JLabel lblStatus;
	
	//Recipe selection
	/** Recipe clipboard */
	@NN public final transient ListenableValue<@Nil Recipe<?>> recipesel = new ListenableValue<>(null);
	
	public void redrawUIs() {
		scrollablePlacementList.repaint();
		toolList.repaint();
	}
	
	/**
	 * Creates a new inventory controller boiund to the player
	 * @return a new inventroy controller
	 */
	@NN public InventoryController playerInventory() {
		return new InventoryController(panelPlayerInv.craftGUI.inventoryController);
	}
	public void playerInventory(InventoryController invctrl) {
		invctrl.set(panelPlayerInv.craftGUI.inventoryController);
	}
	protected JCheckBoxMenuItem getCheckBindCameraPlayer() {
		return checkBindCameraPlayer;
	}
	
	//Events
	/** Invoked when a world window is opened */
	public static final Event<@NN WorldWindow> wwindowOpen = new CatchingEvent<>(debug, "Failed to run world window opened event");
	/** Invoked when a world is loaded in this world window */
	public        final Event<@NN World> worldLoaded = new CatchingEvent<>(debug, "Failed to run world world loaded event");
	/** Invoked when a world is unloaded in this world window */
	public        final Event<@NN World> worldLeft = new CatchingEvent<>(debug, "Failed to run world world left event");
	

	/** @return an item selected by the player */
	@Nil public ItemRecord selectedItem() {
		return scrollablePlacementList.getSelectedValue();
	}
}
