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

import mmb.WORLD.gui.CreativeItemList;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.worlds.world.Player;

import java.awt.Color;
import mmb.MENU.components.BoundCheckBox;
import javax.swing.JSpinner;
import mmb.WORLD.gui.inv.CraftGUI;
import mmb.WORLD.gui.inv.InventoryController;

import java.util.List;
import mmb.WORLD.gui.SelectSortItemTypes;

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
	
	/**
	 * Create an inventory panel with a player pre-set
	 * @param player player represented in this tab
	 */
	public TabInventory(Player player) {
		this();
		setPlayer(player);
	}	
	/**
	 * Create an inventory panel without a player
	 */
	public TabInventory() {
		InventoryController ctrl = new InventoryController();
		craftGUI = new CraftGUI(2, null, null, ctrl);
		
		timer = new Timer(0, e -> craftGUI.inventoryController.refresh());
		setLayout(new MigLayout("", "[100px:100px:100px,grow][::250.00,grow,fill][:400.00:400.00,grow,fill]", "[20px][center][grow]"));
		
		lblSort = new JLabel("Sort ordering:");
		add(lblSort, "cell 0 0");
		
		panel = new JPanel();
		add(panel, "cell 1 0 1 2,growx,aligny top");
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
		add(selectSortItemTypes, "cell 0 1 1 2,grow");
		
		add(craftGUI, "cell 2 0 1 3,growy");
		
		creativeScrollPane = new JScrollPane();
		add(creativeScrollPane, "cell 1 2,grow");
		
		creativeItemList = new CreativeItemList();
		creativeScrollPane.setViewportView(creativeItemList);
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

	public void dispose() {
		timer.stop();
	}
}
