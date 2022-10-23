/**
 * 
 */
package mmbmods.stn.block;

import javax.annotation.Nonnull;

import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;
import net.miginfocom.swing.MigLayout;
import mmb.menu.world.inv.InventoryController;
import mmb.menu.world.inv.MoveItems;
import mmb.GlobalSettings;
import mmb.menu.helper.MenuHelper;
import mmb.menu.world.inv.AbstractInventoryController;
import mmb.menu.world.inv.ItemSelectionSlot;
import mmb.menu.world.inv.SingleInventoryController;
import mmb.world.inventory.storage.SingleItemInventory;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import javax.swing.JButton;

/**
 * @author oskar
 *
 */
public class STNPusherGUI extends GUITab {
	private static final long serialVersionUID = 1823899317802392491L;
	@Nonnull private final transient STNCycler pusher;
	
	STNPusherGUI(STNCycler pusher, WorldWindow window) {
		this.pusher = pusher;
		setLayout(new MigLayout("", "[][grow][]", "[40px][grow]"));
		
		InventoryController inventoryController = new InventoryController();
		window.playerInventory(inventoryController);
		add(inventoryController, "cell 0 0 1 2,grow");
		
		JButton btnNewButton = MenuHelper.exit(this, window);
		add(btnNewButton, "cell 1 0,grow");
		
		ItemSelectionSlot itemSelectionSlot = new ItemSelectionSlot();
		itemSelectionSlot.setBackground(new Color(64, 224, 208));
		itemSelectionSlot.setTarget(pusher.selection);
		itemSelectionSlot.setSelectionSrc(inventoryController.itemSelection());
		itemSelectionSlot.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(itemSelectionSlot, "cell 2 0,grow");
		
		SingleInventoryController singleInventoryController = new SingleInventoryController(pusher.speedupgrade);
		singleInventoryController.setTitle(GlobalSettings.$res("speedup"));
		add(singleInventoryController, "cell 2 1,grow");
		
		MoveItems moveItems = new MoveItems(inventoryController, singleInventoryController);
		add(moveItems, "cell 1 1,grow");
	}
	

	@Override
	public void createTab(WorldWindow window) {
		//unused
	}

	@Override
	public void destroyTab(WorldWindow window) {
		pusher.gui = null;
	}

}
