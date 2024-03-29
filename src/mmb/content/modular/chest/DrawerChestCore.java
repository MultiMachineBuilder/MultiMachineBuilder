/**
 * 
 */
package mmb.content.modular.chest;

import mmb.NN;
import mmb.content.modular.part.PartEntityType;
import mmb.content.modular.part.PartEntry;
import mmb.engine.inv.storage.SingleStackedInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.SingleItem;
import mmb.menu.world.inv.AbstractInventoryController;
import mmb.menu.world.inv.InventoryController;

/**
 * @author oskar
 *
 */
public class DrawerChestCore extends ChestCore {
	@NN private final PartEntityType type;
	/**
	 * Creates a simple chest core (many different items)
	 * @param type item entity type
	 * @param size volume in cubic meters
	 */
	public DrawerChestCore(PartEntityType type, double size) {
		super(new SingleStackedInventory().setCapacity(size));
		this.type = type;
	}

	@Override
	public PartEntry partClone() {
		DrawerChestCore copy = new DrawerChestCore(type, 0);
		((SingleStackedInventory)copy.inventory).set((SingleStackedInventory)inventory);
		return copy;
	}

	@Override
	public PartEntityType type() {
		return type;
	}

	@Override
	public AbstractInventoryController invctrl() {
		return new InventoryController(inventory);
	}

	@Override
	public ChestCore makeEmpty() {
		return new DrawerChestCore(type, inventory.capacity());
	}

	@Override
	public SingleItem getRenderItem() {
		return ((SingleStackedInventory)inventory).getStack();
	}

}
