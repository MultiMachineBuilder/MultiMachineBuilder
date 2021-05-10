/**
 * 
 */
package mmb.WORLD.gui.inv;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 * @author oskar
 *
 */
public class Crafting extends JPanel {

	private final CraftingGrid craftingGrid;
	private InventoryController inventoryController;
	public Crafting() {
		this(1);
	}
	public Crafting(int size) {
		craftingGrid = new CraftingGrid(size);
		setLayout(new MigLayout("", "[263px]", "[155px,grow]"));
		
		inventoryController = new InventoryController();
		add(inventoryController, "cell 0 0,alignx left,growy");
		
		add(craftingGrid, "cell 1 0,alignx left,growy");
	}

}
