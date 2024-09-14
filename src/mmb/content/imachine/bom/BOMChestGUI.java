package mmb.content.imachine.bom;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Color;

import javax.swing.JButton;

import mmb.NN;
import mmb.Nil;
import mmb.content.ContentsItems;
import mmb.content.ditems.ItemBOM;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.RecipeOutput;
import mmb.engine.recipe.RecipeUtil;
import mmb.engine.recipe.SimpleItemList;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;
import mmb.menu.world.inv.InventoryController;
import net.miginfocom.swing.MigLayout;
import mmb.menu.world.inv.MoveItems;
import mmb.menu.world.inv.AbstractInventoryController;

/** UI for the BOM machine */
public class BOMChestGUI extends GUITab {
	private final BOMFactory factory;
	private final WorldWindow window;
	private static final long serialVersionUID = -7412021565574535128L;
	private InventoryController playerInv;
	private static final Debugger debug = new Debugger("BOM MAKER");

	/**
	 * Creates a GUI for the BOM machine
	 * @param bomFactory the current BOM machine
	 * @param window the current window
	 */
	public BOMChestGUI(BOMFactory bomFactory, WorldWindow window) {
		factory = bomFactory;
		this.window = window;
		
		setLayout(new MigLayout("", "[259px,grow][100px][grow]", "[158px,grow]"));
		
		playerInv = new InventoryController();
		add(playerInv, "cell 0 0,grow");
		playerInv.setInv(window.getPlayer().inv);
		
		InventoryController innerInv = new InventoryController();
		add(innerInv, "cell 2 0,grow");
		innerInv.setInv(factory.inv);
		
		MoveItems moveItems = new MoveItems(playerInv, innerInv);
		add(moveItems, "cell 1 0,growy");
		
		JButton close = new JButton($res("exit"));
		close.addActionListener(e -> window.closeWindow(this));
		close.setBackground(Color.RED);
		
		JButton make = new JButton($res("makebom"));
		make.addActionListener(e -> make());
		make.setBackground(Color.GREEN);
		
		moveItems.addAdditionalComponent(make);
		moveItems.addAdditionalComponent(close);
	}

	private void make() {
		RecipeOutput items = new SimpleItemList(factory.inv);
		ItemEntry bom = new ItemBOM(items);
		Inventory inv = window.playerInventory().getInv();
		ItemRecord irecord = playerInv.getSelectedValue(); //this returns null
		if(irecord == null) {
			debug.printl("No item selected");
			return;
		}
		ItemEntry item = irecord.item();
		debug.printl("Type: "+item.type().id());		
		if(irecord.amount() < 1 || item.type() != ContentsItems.BOM) return;
		RecipeUtil.transact(item, bom, inv, inv);
	}

	@Override
	public void close(WorldWindow window) {
		//nothing
	}

}
