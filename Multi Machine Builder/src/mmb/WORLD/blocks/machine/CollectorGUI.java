/**
 * 
 */
package mmb.WORLD.blocks.machine;

import mmb.WORLD.gui.window.GUITab;
import mmb.WORLD.gui.window.WorldWindow;
import net.miginfocom.swing.MigLayout;
import mmb.WORLD.gui.inv.InventoryController;
import mmb.WORLD.gui.inv.MoveItems;
import javax.swing.JButton;
import java.awt.Color;

/**
 * @author oskar
 *
 */
public class CollectorGUI extends GUITab {
	private static final long serialVersionUID = 2506447463267036557L;
	
	private final Collector coll;
	public CollectorGUI(Collector collector, WorldWindow window) {
		coll = collector;
		setLayout(new MigLayout("", "[][][]", "[grow][]"));
		
		InventoryController playerInv = new InventoryController();
		playerInv.setInv(window.getPlayer().inv);
		add(playerInv, "cell 0 0,grow");
		
		InventoryController collectorInv = new InventoryController();
		collectorInv.setInv(collector.getInventory());
		add(collectorInv, "cell 2 0,grow");
		
		MoveItems moveItems = new MoveItems(playerInv, collectorInv, MoveItems.LEFT);
		add(moveItems, "cell 1 0,grow");
		
		JButton btnNewButton = new JButton("Exit");
		btnNewButton.addActionListener(e -> {
			
			window.closeWindow(this);
		});
		btnNewButton.setBackground(Color.RED);
		add(btnNewButton, "cell 0 1 3 1,growx");
		
		
	}

	@Override
	public void createTab(WorldWindow window) {
		//unused
	}

	@Override
	public void destroyTab(WorldWindow window) {
		coll.gui = null;
	}
}
