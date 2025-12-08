/**
 * 
 */
package mmb.content.stn.block;

import net.miginfocom.swing.MigLayout;

import javax.swing.border.BevelBorder;

import mmb.annotations.NN;
import mmb.engine.settings.GlobalSettings;
import mmb.menu.MenuHelper;
import mmb.menu.components.ItemSelectionSlot;
import mmb.menu.world.inv.InventoryController;
import mmb.menu.world.inv.MoveItems;
import mmb.menu.world.inv.SingleInventoryController;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;

import java.awt.Color;
import javax.swing.JButton;

/**
 * Allows to configure STN importers and exporters
 * @author oskar
 */
public class STNPusherGUI extends GUITab {
	private static final long serialVersionUID = 1823899317802392491L;
	@NN private final transient STNCycler pusher;
	private ItemSelectionSlot itemSelectionSlot;
	
	STNPusherGUI(STNCycler pusher, WorldWindow window) {
		this.pusher = pusher;
		setLayout(new MigLayout("", "[][grow][]", "[40px][grow]"));
		
		InventoryController inventoryController = new InventoryController();
		window.playerInventory(inventoryController);
		add(inventoryController, "cell 0 0 1 2,grow");
		
		JButton btnNewButton = MenuHelper.exit(this, window);
		add(btnNewButton, "cell 1 0,grow");
		
		itemSelectionSlot = new ItemSelectionSlot();
		itemSelectionSlot.setBackground(new Color(64, 224, 208));
		itemSelectionSlot.setTarget(pusher.selection);
		itemSelectionSlot.setSelector(inventoryController::getSelectedItem);
		itemSelectionSlot.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		add(itemSelectionSlot, "cell 2 0,grow");
		
		SingleInventoryController singleInventoryController = new SingleInventoryController(pusher.speedupgrade);
		singleInventoryController.setTitle(GlobalSettings.$res("speedup"));
		add(singleInventoryController, "cell 2 1,grow");
		
		MoveItems moveItems = new MoveItems(inventoryController, singleInventoryController);
		add(moveItems, "cell 1 1,grow");
	}

	@Override
	public void close(WorldWindow window) {
		pusher.gui = null;
		itemSelectionSlot.close();
	}

}
