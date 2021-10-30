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
		setLayout(new MigLayout("", "[::250.00,grow,fill][:400.00:400.00,grow,fill][grow]", "[20px][grow]"));
		
		lblNewLabel = new JLabel("Creative items");
		add(lblNewLabel, "flowx,cell 0 0");
		
		creativeScrollPane = new JScrollPane();
		add(creativeScrollPane, "cell 0 1,grow");
		
		creativeItemList = new CreativeItemList();
		creativeScrollPane.setViewportView(creativeItemList);
		
		add(craftGUI, "cell 1 0 1 2,growy");
		
		panel = new JPanel();
		add(panel, "cell 2 0 1 2,grow");
		panel.setLayout(new MigLayout("", "[][]", "[][][][]"));
		
		checkSurvival = new BoundCheckBox();
		panel.add(checkSurvival, "cell 0 0");
		checkSurvival.setText("Creative");
		
		lblNewLabel_1 = new JLabel("Items to remove/add");
		panel.add(lblNewLabel_1, "cell 0 1");
		
		itemAmt = new JSpinner();
		panel.add(itemAmt, "cell 1 1,growx");
		
		btnRemove = new JButton("Remove one");
		panel.add(btnRemove, "cell 0 2,growx");
		btnRemove.addActionListener(e -> removeItems(1));
		
		btnRemove.setBackground(new Color(170, 0, 0));
		
		
		btnNewButton = new JButton("Remove #");
		btnNewButton.addActionListener(e -> {
			@SuppressWarnings("boxing")
			int amt = (Integer)(itemAmt.getValue());
			removeItems(amt);
		});
		btnNewButton.setBackground(new Color(204, 0, 0));
		panel.add(btnNewButton, "cell 1 2");
		
		btnNewButton_2 = new JButton("Empty this slot");
		btnNewButton_2.addActionListener(e -> removeItems(Integer.MAX_VALUE));
		btnNewButton_2.setBackground(new Color(255, 0, 0));
		panel.add(btnNewButton_2, "cell 0 3 2 1,growx");
		
		btnAdd = new JButton("Add one");
		btnAdd.addActionListener(e -> addItems(1));
		btnAdd.setBackground(new Color(0, 170, 0));
		add(btnAdd, "cell 0 0");
		
		btnNewButton_1 = new JButton("Add #");
		btnNewButton_1.addActionListener(e -> {
			@SuppressWarnings("boxing")
			int amt = (Integer)(itemAmt.getValue());
			addItems(amt);
		});
		btnNewButton_1.setBackground(new Color(0, 204, 0));
		add(btnNewButton_1, "cell 0 0");
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
		ItemType item = creativeItemList.getSelectedValue();
		if(item == null) {
			debug.printl("Got null item");
			return;
		}
		inv.insert(item.create(), amount);
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

	public void dispose() {
		timer.stop();
	}
}
