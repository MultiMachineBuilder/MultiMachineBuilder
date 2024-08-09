/**
 * 
 */
package mmb.menu.world.window;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import com.pploder.events.Event;

import monniasza.collects.Collects;

import javax.swing.JButton;
import javax.swing.JLabel;

import static mmb.engine.settings.GlobalSettings.*;

import java.awt.Color;

import javax.swing.JSpinner;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

import javax.swing.JCheckBox;
import javax.swing.JList;
import io.github.parubok.text.multiline.MultilineLabel;
import mmb.NN;
import mmb.Nil;
import mmb.content.craft.CraftGUI;
import mmb.content.electric.VoltageTier;
import mmb.engine.CatchingEvent;
import mmb.engine.block.Block;
import mmb.engine.block.BlockEntityType;
import mmb.engine.block.BlockType;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.item.Item;
import mmb.engine.item.ItemEntityType;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemType;
import mmb.engine.item.Items;
import mmb.engine.recipe.GlobalRecipeRegistrar;
import mmb.engine.recipe.Recipe;
import mmb.engine.worlds.world.Player;
import mmb.menu.components.BoundCheckBox;
import mmb.menu.world.CreativeItemList;
import mmb.menu.world.inv.InventoryController;

/**
 * @author oskar
 *
 */
public class TabInventory extends JPanel {
	private static final long serialVersionUID = 4210914960590758120L;
	
	private JScrollPane creativeScrollPane;
	private CreativeItemList creativeItemList;
	
	@NN private static final Debugger debug = new Debugger("PLAYER INVENTORY");
	private Player player;
	public final Event<Player> playerChanged = new CatchingEvent<>(debug, "Failed to process player changed event");
	public final WorldWindow window;
	
	//Tag selectors (tagsels)
	/**
	 * Filters items by tags or properties
	 * @author oskar
	 */
	public static interface Tagsel{
		/** @return all matching items */
		@NN public DefaultListModel<ItemType> eligible();
		/** @return Display title in tag selection list */
		@NN public String title();
	}
	
	/**
	 * Selects all items
	 * @author oskar
	 */
	public static class AllTagsel implements Tagsel{
		@NN private static final String title = "1 "+$res("wguit-all");
		@Override
		public DefaultListModel<ItemType> eligible() {
			return CreativeItemList.model;
		}

		@Override
		public String title() {
			return title;
		}

		@Override
		public String toString() {
			return title;
		}
	}
	/**
	 * Selects items which match criteria
	 * @author oskar
	 */
	public static class FilterTagsel implements Tagsel{
		@NN public final String tag;
		@NN public final DefaultListModel<ItemType> set;
		public FilterTagsel(String s, Predicate<ItemType> filter) {
			tag = "2 "+s;
			set = new DefaultListModel<>();
			for(ItemType item: Items.items) {
				if(filter.test(item)) set.addElement(item);
			}
		}
		@Override
		public DefaultListModel<ItemType> eligible() {
			return set;
		}
		@Override
		public String title() {
			return tag;
		}
		@Override
		public String toString() {
			return tag;
		}
	}
	/**
	 * Selects items by tag
	 * @author oskar
	 */
	public static class TaggedSel implements Tagsel{
		/** The tag selection */
		@NN public final String tag;
		@NN public final DefaultListModel<ItemType> set;
		public TaggedSel(String s, Set<ItemType> set2) {
			tag = "3 "+s;
			set = new DefaultListModel<>();
			for(ItemType item: set2) {
				set.addElement(item);
			}
		}
		@Override
		public DefaultListModel<ItemType> eligible() {
			return set;
		}
		@Override
		public String title() {
			return tag;
		}
		@Override
		public String toString() {
			return tag;
		}
	}
	
	/**
	 * Create an inventory panel with a player pre-set
	 * @param window world window, for which the tab is created
	 * @param player player represented in this tab
	 */
	public TabInventory(WorldWindow window, Player player) {
		this(window);
		setPlayer(player);
	}	
	/**
	 * Create an inventory panel without a player
	 * @param window world window, for which the tab is created
	 * @wbp.parser.constructor
	 */
	public TabInventory(WorldWindow window) {
		this.window = window;
		setLayout(new MigLayout("", "[400.00,fill][]", "[20px,grow]"));
		craftGUI = new CraftGUI(2, null, null, null);
		timer = new Timer(0, e -> craftGUI.inventoryController.refresh());
		add(craftGUI, "cell 1 0,growy");
		
		creativePanel = new JPanel();
		add(creativePanel, "cell 0 0,grow");
		creativePanel.setLayout(new MigLayout("", "[grow][grow]", "[][grow][grow][]"));
		
		lblSort = new JLabel($res("wgui-sort"));
		creativePanel.add(lblSort, "cell 0 0");
		
		panel = new JPanel();
		creativePanel.add(panel, "cell 1 0,growx");
		panel.setLayout(new MigLayout("", "[][]", "[][][][][]"));
		
		lblCreativeItems = new JLabel($res("wgui-creati"));
		panel.add(lblCreativeItems, "cell 0 0,growx");
		
		lblGamemode = new JLabel("TEXT NOT SET! THIS IS A BUG!");
		lblGamemode.setBackground(new Color(65, 105, 225));
		lblGamemode.setOpaque(true);
		panel.add(lblGamemode, "cell 1 0");
		
		lbAddRemoveCount = new JLabel($res("wgui-icount"));
		panel.add(lbAddRemoveCount, "cell 0 1");
		
		itemAmt = new JSpinner();
		panel.add(itemAmt, "cell 1 1,growx");
		
		JButton btnAdd = new JButton($res("wgui-a1"));
		panel.add(btnAdd, "cell 0 2,growx");
		btnAdd.addActionListener(e -> addItems(1));
		btnAdd.setBackground(new Color(0, 170, 0));
		
		JButton btnAddN = new JButton($res("wgui-an"));
		panel.add(btnAddN, "cell 1 2,growx");
		btnAddN.addActionListener(e -> {
			@SuppressWarnings({"boxing", "🥊"})
			int amt = (Integer)(itemAmt.getValue());
			addItems(amt);
		});
		btnAddN.setBackground(new Color(0, 204, 0));
		
		JButton btnRemove = new JButton($res("wgui-r1"));
		panel.add(btnRemove, "cell 0 3,growx");
		btnRemove.addActionListener(e -> removeItems(1));
		
		btnRemove.setBackground(new Color(170, 0, 0));
		
		JButton btnRemoveN = new JButton($res("wgui-rn"));
		btnRemoveN.addActionListener(e -> {
			@SuppressWarnings("boxing")
			int amt = (Integer)(itemAmt.getValue());
			removeItems(amt);
		});
		btnRemoveN.setBackground(new Color(204, 0, 0));
		panel.add(btnRemoveN, "cell 1 3,growx");
		
		JButton btnREmoveAll = new JButton($res("wgui-ra"));
		btnREmoveAll.addActionListener(e -> removeItems(Integer.MAX_VALUE));
		btnREmoveAll.setBackground(new Color(255, 0, 0));
		panel.add(btnREmoveAll, "cell 0 4 2 1,growx");
		
		selectSortItemTypes = new SelectSortItemTypes(() -> tags);
		creativePanel.add(selectSortItemTypes, "cell 0 1,grow");
		
		//Creative Item List
		creativeScrollPane = new JScrollPane();
		creativePanel.add(creativeScrollPane, "cell 1 1 1 2,grow");
		creativeItemList = new CreativeItemList();
		creativeItemList.addListSelectionListener(e -> {
			ItemType item = creativeItemList.getSelectedValue();
			String s = (item==null)?null:item.description();
			multilineLabel.setText(s==null?"":s);
		});
		creativeScrollPane.setViewportView(creativeItemList);
		
		lblResults = new JLabel($res("wgui-results"));
		creativeScrollPane.setColumnHeaderView(lblResults);
		
		//Tags
		DefaultListModel<Tagsel> model = new DefaultListModel<>();
		model.addElement(new AllTagsel());
		model.addElement(new FilterTagsel($res("wguit-block"), i -> i instanceof BlockType));
		model.addElement(new FilterTagsel($res("wguit-items"), i -> !(i instanceof BlockType)));
		model.addElement(new FilterTagsel($res("wguit-bents"), i -> i instanceof BlockEntityType));
		model.addElement(new FilterTagsel($res("wguit-ients"), i -> i instanceof ItemEntityType));
		model.addElement(new FilterTagsel($res("wguit-sblk"),  i -> i instanceof Block));
		model.addElement(new FilterTagsel($res("wguit-sitem"), i -> i instanceof Item && !(i instanceof BlockType)));
		model.addElement(new FilterTagsel($res("wguit-notags"), i -> !Items.tags.containsValue(i)));
		model.addElement(new FilterTagsel($res("wguit-tags"), i -> Items.tags.containsValue(i)));
		model.addElement(new FilterTagsel($res("wguit-norecipes"), i -> !GlobalRecipeRegistrar.craftable.contains(i)));
		model.addElement(new FilterTagsel($res("wguit-recipes"), i -> GlobalRecipeRegistrar.craftable.contains(i)));
		model.addElement(new FilterTagsel($res("wguit-noway"), i -> !GlobalRecipeRegistrar.obtainable.contains(i)));
		model.addElement(new FilterTagsel($res("wguit-way"), i -> GlobalRecipeRegistrar.obtainable.contains(i)));
		model.addElement(new FilterTagsel($res("wguit-noins"), i -> !GlobalRecipeRegistrar.consumable.contains(i)));
		model.addElement(new FilterTagsel($res("wguit-ins"), i -> GlobalRecipeRegistrar.consumable.contains(i)));
		model.addElement(new FilterTagsel($res("wguit-nochance"), i -> !GlobalRecipeRegistrar.chanceable.contains(i)));
		model.addElement(new FilterTagsel($res("wguit-chance"), i -> GlobalRecipeRegistrar.chanceable.contains(i)));
		for(Entry<String, Collection<ItemType>> data : Items.tags.asMap().entrySet()) {
			String s = data.getKey();
			Set<ItemType> set = (Set<ItemType>) data.getValue();
			model.addElement(new TaggedSel(s, set));
		}
		
		scrollPane = new JScrollPane();
		creativePanel.add(scrollPane, "cell 0 2 1 2,grow");
		tags = new JList<>();
		scrollPane.setViewportView(tags);
		tags.setModel(model);
		
		multilineLabel = new MultilineLabel();
		multilineLabel.setBackground(new Color(0, 191, 255));
		multilineLabel.setOpaque(true);
		creativePanel.add(multilineLabel, "cell 1 3,grow");
		tags.addListSelectionListener(e -> {
			Tagsel sel = tags.getSelectedValue();
			Comparator<ItemType> cmp = selectSortItemTypes.getSelectedValue();
			if(cmp == null) return;
			DefaultListModel<ItemType> model0 = sel.eligible();
			lblResults.setText(model0.getSize()+" "+$res("wgui-results"));
			CreativeItemList.resort(cmp, model0);
			creativeItemList.setModel(model0);
		});
		//Sort the tags
		Collections.sort(Collects.toWritableList(model), (a, b) -> a.title().compareTo(b.title()));
		
		//Crafting		
		craftingsPanel = new JPanel();
		craftGUI.box.add(craftingsPanel);
		craftingsPanel.setLayout(new BoxLayout(craftingsPanel, BoxLayout.Y_AXIS));
		
		//Find recipes
		JLabel lblRecipes = new JLabel($res("wguicf-head"));
		craftingsPanel.add(lblRecipes);
		
		JButton btnCraftTo = new JButton($res("wguicf-prod"));
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
		
		JButton btnCraftRandom = new JButton($res("wguicf-chan"));
		btnCraftRandom.setBackground(new Color(204, 255, 0));
		btnCraftRandom.addActionListener(e -> {
			ItemEntry item = findSourceItem();
			if(item != null) {
				queryRecipes(gambling(item));
			}else {
				queryRecipes(all());
			}
		});
		craftingsPanel.add(btnCraftRandom);
		
		JButton btnCraftFrom = new JButton($res("wguicf-cons"));
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
		
		JButton btnCraftBy = new JButton($res("wguicf-mac"));
		btnCraftBy.setEnabled(false);
		btnCraftBy.setBackground(Color.BLUE);
		craftingsPanel.add(btnCraftBy);
		
		JButton btnCraftWith = new JButton($res("wguicf-cat"));
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
		
		JButton btnCraftAll = new JButton($res("wguicf-all"));
		btnCraftAll.setBackground(Color.GRAY);
		btnCraftAll.addActionListener(e -> queryRecipes(all()));
		craftingsPanel.add(btnCraftAll);
		
		checkUseCIL = new JCheckBox($res("wguicf-cil"));
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
	}
	
	private final Timer timer;
	private JLabel lblCreativeItems;
	private JLabel lbAddRemoveCount;
	private JPanel panel;
	private JSpinner itemAmt;
	public final CraftGUI craftGUI;
	private SelectSortItemTypes selectSortItemTypes;
	private JLabel lblSort;
	private JPanel creativePanel;
	private JPanel craftingsPanel;
	private JCheckBox checkUseCIL;
	private JList<Tagsel> tags;
	private JScrollPane scrollPane;
	private MultilineLabel multilineLabel;
	private JLabel lblResults;
	JLabel lblGamemode;

	/**
	 * @author oskar
	 * Describes a rule or set of rules to filter recipes
	 */
	public static interface RecipeQuery{
		/**
		 * Returns a large set of eligible items
		 * The recipes which pass both phases 1 and 2 must be exactly the same as all recipes which pass this query's filter
		 * @return potentially eligible items
		 */
		@Nil public Set<@NN Recipe> phase1();
		/**
		 * Narrows down the list of items to produce an exact list
		 * The recipes which pass both phases 1 and 2 must be exactly the same as all recipes which pass this query's filter
		 * @param recipe the recipe to test
		 * @return does recipe match?
		 */
		public boolean phase2(Recipe<?> recipe);
		
		/** @return all eligible items*/		
		@NN public default Set<Recipe<?>> eligible(){
			Set<@NN Recipe> recipes = phase1();
			Set<Recipe<?>> model = new HashSet<>();
			if(recipes != null) for(Recipe<?> item: recipes) {
				if(phase2(item)) model.add(item);
			}
			return model;
		}
		/**
		 * @return the displayed query string
		 */
		public String name();
		/**
		 * @param recipe the recipe to test
		 * @return does recipe match?
		 */public boolean filter(Recipe<?> recipe);
	}
	private void queryRecipes(RecipeQuery query) {
		debug.printl("Querying recipes");
		window.openAndShowWindow(new QueriedRecipes(query, window), $res("wguicf-search")+" "+query.name());
		debug.printl("Query finished");
	}
	
	/**
	 * @return a recipe query, which accepts all recipes
	 */
	@NN public static RecipeQuery all() {
		return new RecipeQuery() {
			@Override
			public String name() {
				return $res("wguicq-all");
			}

			@Override
			public boolean filter(Recipe<?> recipe) {
				return true;
			}
			@Override
			public @Nil Set<@NN Recipe> phase1() {
				return GlobalRecipeRegistrar.recipes;
			}

			@Override
			public boolean phase2(Recipe<?> recipe) {
				return true;
			}
		};
	}
	/**
	 * @param item
	 * @return a recipe query, which accepts recipes
	 */
	@NN public static RecipeQuery consuming(ItemEntry item) {
		return new RecipeQuery() {
			@Override
			public String name() {
				return $res("wguicq-consuming")+" "+item.title();
			}

			@Override
			public boolean filter(Recipe<?> recipe) {
				return recipe.inputs().contains(item);
			}

			@Override
			public @Nil Set<@NN Recipe> phase1() {
				return GlobalRecipeRegistrar.inputs.multimap().get(item);
			}

			@Override
			public boolean phase2(Recipe<?> recipe) {
				return true;
			}
		};
	}
	@NN public static RecipeQuery producing(ItemEntry item) {
		return new RecipeQuery() {
			@Override
			public String name() {
				return $res("wguicq-producing")+" "+item.title();
			}

			@Override
			public boolean filter(Recipe<?> recipe) {
				return recipe.output().contains(item);
			}
			
			@Override
			public @Nil Set<@NN Recipe> phase1() {
				return GlobalRecipeRegistrar.output.multimap().get(item);
			}

			@Override
			public boolean phase2(Recipe<?> recipe) {
				return true;
			}
		};
	}
	@NN public static RecipeQuery gambling(ItemEntry item) {
		return new RecipeQuery() {
			@Override
			public String name() {
				return $res("wguicq-gambling")+" "+item.title();
			}

			@Override
			public boolean filter(Recipe<?> recipe) {
				return recipe.luck().contains(item);
			}
			
			@Override
			public @Nil Set<@NN Recipe> phase1() {
				return GlobalRecipeRegistrar.chance.multimap().get(item);
			}

			@Override
			public boolean phase2(Recipe<?> recipe) {
				return true;
			}
		};
	}
	@NN public static RecipeQuery catalysing(ItemEntry item) {
		return new RecipeQuery() {
			@Override
			public String name() {
				return $res("wguicq-catalysing")+" "+item.title();
			}

			@Override
			public boolean filter(Recipe<?> recipe) {
				return Objects.equals(item, recipe.catalyst());
			}
			@Override
			public Set<@NN Recipe> phase1() {
				return GlobalRecipeRegistrar.catalyst.multimap().get(item);
			}

			@Override
			public boolean phase2(Recipe<?> recipe) {
				return true;
			}
		};
	}
	@NN public static RecipeQuery byVolt(VoltageTier item) {
		return new RecipeQuery() {
			@Override
			public String name() {
				return "Recipes at: "+item.name;
			}

			@Override
			public boolean filter(Recipe<?> recipe) {
				return recipe.voltTier() == item;
			}
			@Override
			public @Nil Set<@NN Recipe> phase1() {
				return GlobalRecipeRegistrar.volt.multimap().get(item);
			}

			@Override
			public boolean phase2(Recipe<?> recipe) {
				return true;
			}
		};
	}
	@NN public static RecipeQuery alone(Set<@NN Recipe> recipes) {
		return new RecipeQuery() {
			@Override
			public String name() {
				return $res("wguicq-alone");
			}

			@Override
			public boolean filter(Recipe<?> recipe) {
				return true;
			}
			@Override
			public @Nil Set<@NN Recipe> phase1() {
				return recipes;
			}

			@Override
			public boolean phase2(Recipe<?> recipe) {
				return true;
			}
		};
	}
	
	private ItemEntry findSourceItem() {
		boolean check = checkUseCIL.isSelected();
		if(check) {
			ItemType type = creativeItemList.getSelectedValue();
			if(type == null) return null;
			return type.create();
		}
		ItemRecord irecord = craftGUI.inventoryController.getSelectedValue();
		if(irecord == null) return null;
		return irecord.item();
	}
	
	public void dispose() {
		timer.stop();
	}
}
