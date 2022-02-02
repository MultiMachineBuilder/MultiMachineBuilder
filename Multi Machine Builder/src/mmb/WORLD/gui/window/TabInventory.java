/**
 * 
 */
package mmb.WORLD.gui.window;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import com.pploder.events.CatchingEvent;
import com.pploder.events.Event;

import mmb.debug.Debugger;

import javax.swing.JButton;
import javax.swing.JLabel;

import mmb.WORLD.crafting.Recipe;
import mmb.WORLD.gui.CreativeItemList;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.world.Player;

import java.awt.Color;
import mmb.MENU.components.BoundCheckBox;
import javax.swing.JSpinner;
import mmb.WORLD.gui.inv.CraftGUI;
import mmb.WORLD.gui.inv.InventoryController;

import java.util.List;
import java.util.Objects;

import mmb.WORLD.gui.SelectSortItemTypes;

import javax.annotation.Nonnull;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

/**
 * @author oskar
 *
 */
public class TabInventory extends JPanel {
	private static final long serialVersionUID = 4210914960590758120L;
	
	private JScrollPane creativeScrollPane;
	private CreativeItemList creativeItemList;
	
	private static final Debugger debug = new Debugger("PLAYER INVENTORY");
	private Player player;
	public final Event<Player> playerChanged = new CatchingEvent<>(debug, "Failed to process player changed event");
	public final WorldWindow window;
	
	/**
	 * Create an inventory panel with a player pre-set
	 * @param player player represented in this tab
	 */
	public TabInventory(WorldWindow window, Player player) {
		this(window);
		setPlayer(player);
	}	
	/**
	 * Create an inventory panel without a player
	 * @wbp.parser.constructor
	 */
	public TabInventory(WorldWindow window) {
		this.window = window;
		setLayout(new MigLayout("", "[:400.00:400.00,grow,fill][][grow]", "[20px,grow]"));
		InventoryController ctrl = new InventoryController();
		craftGUI = new CraftGUI(2, null, null, ctrl);
		timer = new Timer(0, e -> craftGUI.inventoryController.refresh());
		add(craftGUI, "cell 1 0,growy");
		
		creativePanel = new JPanel();
		add(creativePanel, "cell 0 0,grow");
		creativePanel.setLayout(new MigLayout("", "[][]", "[][grow]"));
		
		lblSort = new JLabel("Sort ordering:");
		creativePanel.add(lblSort, "cell 0 0");
		
		panel = new JPanel();
		creativePanel.add(panel, "cell 1 0,growx");
		panel.setLayout(new MigLayout("", "[][]", "[][][][][]"));
		
		lblNewLabel = new JLabel("Creative items");
		panel.add(lblNewLabel, "cell 0 0");
		
		checkSurvival = new BoundCheckBox();
		panel.add(checkSurvival, "cell 1 0");
		checkSurvival.setText("Creative");
		
		lblNewLabel_1 = new JLabel("Items to remove/add");
		panel.add(lblNewLabel_1, "cell 0 1");
		
		itemAmt = new JSpinner();
		panel.add(itemAmt, "cell 1 1,growx");
		
		btnAdd = new JButton("Add one");
		panel.add(btnAdd, "cell 0 2,growx");
		btnAdd.addActionListener(e -> addItems(1));
		btnAdd.setBackground(new Color(0, 170, 0));
		
		btnNewButton_1 = new JButton("Add #");
		panel.add(btnNewButton_1, "cell 1 2,growx");
		btnNewButton_1.addActionListener(e -> {
			@SuppressWarnings("boxing")
			int amt = (Integer)(itemAmt.getValue());
			addItems(amt);
		});
		btnNewButton_1.setBackground(new Color(0, 204, 0));
		
		btnRemove = new JButton("Remove one");
		panel.add(btnRemove, "cell 0 3,growx");
		btnRemove.addActionListener(e -> removeItems(1));
		
		btnRemove.setBackground(new Color(170, 0, 0));
		
		btnNewButton = new JButton("Remove #");
		btnNewButton.addActionListener(e -> {
			@SuppressWarnings("boxing")
			int amt = (Integer)(itemAmt.getValue());
			removeItems(amt);
		});
		btnNewButton.setBackground(new Color(204, 0, 0));
		panel.add(btnNewButton, "cell 1 3");
		
		btnNewButton_2 = new JButton("Empty this slot");
		btnNewButton_2.addActionListener(e -> removeItems(Integer.MAX_VALUE));
		btnNewButton_2.setBackground(new Color(255, 0, 0));
		panel.add(btnNewButton_2, "cell 0 4 2 1,growx");
		
		selectSortItemTypes = new SelectSortItemTypes();
		creativePanel.add(selectSortItemTypes, "cell 0 1,growy");
		
		creativeScrollPane = new JScrollPane();
		creativePanel.add(creativeScrollPane, "cell 1 1,growy");
		
		creativeItemList = new CreativeItemList();
		creativeScrollPane.setViewportView(creativeItemList);
		
		craftingsPanel = new JPanel();
		add(craftingsPanel, "cell 2 0,grow");
		craftingsPanel.setLayout(new BoxLayout(craftingsPanel, BoxLayout.Y_AXIS));
		
		lblRecipes = new JLabel("Find recipes which:");
		craftingsPanel.add(lblRecipes);
		
		btnCraftTo = new JButton("PRODUCE given item");
		btnCraftTo.addActionListener(e -> {
			ItemEntry item = findSourceItem();
			if(item != null) {
				queryRecipes(producing(item));
			}else {
				queryRecipes(all());
			}
		});
		btnCraftTo.setBackground(Color.GREEN);
		craftingsPanel.add(btnCraftTo);
		
		btnCraftFrom = new JButton("CONSUME given item");
		btnCraftFrom.setBackground(Color.RED);
		btnCraftFrom.addActionListener(e -> {
			ItemEntry item = findSourceItem();
			if(item != null) {
				queryRecipes(consuming(item));
			}else {
				queryRecipes(all());
			}
		});
		craftingsPanel.add(btnCraftFrom);
		
		btnCraftBy = new JButton("use given MACHINE(s)");
		btnCraftBy.setEnabled(false);
		btnCraftBy.setBackground(Color.BLUE);
		craftingsPanel.add(btnCraftBy);
		
		btnCraftWith = new JButton("use given CATALYST");
		btnCraftWith.setBackground(new Color(255, 255, 0));
		btnCraftWith.addActionListener(e -> {
			ItemEntry item = findSourceItem();
			if(item != null) {
				queryRecipes(catalysing(item));
			}else {
				queryRecipes(all());
			}
		});
		craftingsPanel.add(btnCraftWith);
		
		btnCraftAll = new JButton("ALL recipes");
		btnCraftAll.setBackground(Color.GRAY);
		btnCraftAll.addActionListener(e -> queryRecipes(all()));
		craftingsPanel.add(btnCraftAll);
		
		checkUseCIL = new JCheckBox("Use the creative item list");
		checkUseCIL.setSelected(true);
		craftingsPanel.add(checkUseCIL);
		
	}
	private void removeItems(int amount) {
		int remain = amount;
		List<ItemRecord> records = craftGUI.inventoryController.getSelectedValuesList();
		for(ItemRecord record: records) {
			if(remain == 0) return;
			remain -= record.extract(amount);
		}
		craftGUI.inventoryController.refresh();
	}
	private void addItems(int amount) {
		Inventory inv = craftGUI.inventoryController.getInv();
		if(inv == null) {
			debug.printl("Got null inventory");
			return;
		}
		List<ItemType> items = creativeItemList.getSelectedValuesList();
		if(items == null) {
			debug.printl("Got null item list");
			return;
		}
		for(ItemType item: items) {
			if(item == null) {
				debug.printl("Got null item");
				continue;
			}
			inv.insert(item.create(), amount);
		}
		craftGUI.inventoryController.refresh();
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		if(this.player == player) return;
		this.player = player;
		playerChanged.trigger(player);
		craftGUI.inventoryController.setInv(player.inv);
		checkSurvival.setVariable(player.creative);
	}
	
	private final Timer timer;
	private JLabel lblNewLabel;
	private JButton btnAdd;
	private JButton btnRemove;
	private BoundCheckBox checkSurvival;
	private JLabel lblNewLabel_1;
	private JPanel panel;
	private JButton btnNewButton;
	private JSpinner itemAmt;
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;
	public final CraftGUI craftGUI;
	private SelectSortItemTypes selectSortItemTypes;
	private JLabel lblSort;
	private JPanel creativePanel;
	private JPanel craftingsPanel;
	private JLabel lblRecipes;
	private JButton btnCraftTo;
	private JButton btnCraftFrom;
	private JButton btnCraftBy;
	private JButton btnCraftWith;
	private JButton btnCraftAll;
	private JCheckBox checkUseCIL;

	/**
	 * @author oskar
	 * Describes a rule or set of rules to filter recipes
	 */
	public static interface RecipeQuery{
		/**
		 * @return the displayed query string
		 */
		public String name();
		/**
		 * @param recipe the recipe to test
		 * @return does recipe match?
		 */
		public boolean filter(Recipe<?> recipe);
	}
	private void queryRecipes(RecipeQuery query) {
		debug.printl("Querying recipes");
		window.openAndShowWindow(new QueriedRecipes(query, window), "Recipe search: "+query.name());
		debug.printl("Query finished");
	}
	
	@Nonnull public static RecipeQuery all() {
		return new RecipeQuery() {
			@Override
			public String name() {
				return "All recipes";
			}

			@Override
			public boolean filter(Recipe<?> recipe) {
				return true;
			}
		};
	}
	@Nonnull public static RecipeQuery consuming(ItemEntry item) {
		return new RecipeQuery() {
			@Override
			public String name() {
				return "Recipes which consume: "+item.title();
			}

			@Override
			public boolean filter(Recipe<?> recipe) {
				return recipe.inputs().contains(item);
			}
		};
	}
	@Nonnull public static RecipeQuery producing(ItemEntry item) {
		return new RecipeQuery() {
			@Override
			public String name() {
				return "Recipes which produce: "+item.title();
			}

			@Override
			public boolean filter(Recipe<?> recipe) {
				return recipe.output().contains(item);
			}
		};
	}
	@Nonnull public static RecipeQuery catalysing(ItemEntry item) {
		return new RecipeQuery() {
			@Override
			public String name() {
				return "Recipes which produce: "+item.title();
			}

			@Override
			public boolean filter(Recipe<?> recipe) {
				return Objects.equals(item, recipe.catalyst());
			}
		};
	}
	
	private ItemEntry findSourceItem() {
		ItemRecord record = craftGUI.inventoryController.getSelectedValue();
		if(record != null) return record.item();
		ItemType type = creativeItemList.getSelectedValue();
		if(type == null) return null;
		return type.create();
	}
	
	public void dispose() {
		timer.stop();
	}
}
