/**
 * 
 */
package mmb.world.blocks.chest;

import net.miginfocom.swing.MigLayout;
import mmb.menu.world.ColorGUI;
import mmb.menu.world.inv.InventoryController;
import mmb.menu.world.inv.InventoryOrchestrator;
import mmb.menu.world.inv.MoveItems;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;
import mmb.world.worlds.world.Player;

import javax.swing.JButton;
import javax.annotation.Nonnull;

import static mmb.GlobalSettings.$res;

import java.awt.Color;

/**
 * @author oskar
 *
 */
public class ChestGui extends GUITab{
	/**
	 * Creates a chest GUI
	 * @param hopper chest to use
	 * @param window world window
	 */
	public ChestGui(ArbitraryChest hopper, WorldWindow window) {
		setLayout(new MigLayout("", "[300.00,grow][132.00,grow][300.00,grow]", "[grow][30.00]"));
		
		Player p = window.getPlayer();
		
		color = new JButton("Change color");
		color.addActionListener(e -> {
			ColorGUI gui = new ColorGUI(hopper.getColor(), hopper::setColor, window);
			window.openAndShowWindow(gui, "Chest color");
		});
		
		inventoryOrchestrator = new InventoryOrchestrator();
		add(inventoryOrchestrator, "cell 0 1 3 1,growx");
		
		chestCtrl = new InventoryController();
		chestCtrl.setInv(hopper.inv());
		chestCtrl.setOrchestrator(inventoryOrchestrator);
		chestCtrl.setTitle($res("chest"));
		add(chestCtrl, "cell 2 0,grow");
		
		playerCtrl = new InventoryController();
		playerCtrl.setTitle($res("player"));
		playerCtrl.setInv(p.inv);
		playerCtrl.setOrchestrator(inventoryOrchestrator);
		add(playerCtrl, "cell 0 0,grow");
		
		close = new JButton($res("exit"));
		close.addActionListener(e -> window.closeWindow(this));
		close.setBackground(Color.RED);
		
		moveItems = new MoveItems(playerCtrl, chestCtrl);
		moveItems.addAdditionalComponent(color);
		moveItems.addAdditionalComponent(close);
		add(moveItems, "cell 1 0,growx,aligny center");
		
		refresh();
	}
	private static final long serialVersionUID = -3527290050616724746L;
	@Nonnull private InventoryController playerCtrl;
	@Nonnull private InventoryOrchestrator inventoryOrchestrator;
	@Nonnull private InventoryController chestCtrl;
	@Nonnull private JButton close;
	@Nonnull private JButton color;
	@Nonnull private MoveItems moveItems;
	
	private void refresh() {
		chestCtrl.refresh();
		playerCtrl.refresh();
	}

	@Override
	public void close(WorldWindow window) {
		//unused
	}

}
