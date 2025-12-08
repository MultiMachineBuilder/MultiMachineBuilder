/**
 * 
 */
package mmb.content.modular.chest;

import mmb.annotations.NN;
import mmb.content.modular.part.PartEntityType;
import mmb.content.modular.part.PartEntry;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.recipe.SingleItem;
import mmb.menu.world.inv.AbstractInventoryController;
import mmb.menu.world.inv.InventoryController;

/**
 * @author oskar
 *
 */
public class ChestCoreSingle extends ChestCore {
	@NN private final PartEntityType type;
	/**
	 * Creates a simple chest core (many different items)
	 * @param type item entity type
	 * @param size volume in cubic meters
	 */
	public ChestCoreSingle(PartEntityType type, double size) {
		super(new SingleItemInventory().setCapacity(size));
		this.type = type;
	}

	@Override
	public PartEntry partClone() {
		ChestCoreSingle copy = new ChestCoreSingle(type, 0);
		((SingleItemInventory)copy.inventory).set((SingleItemInventory)inventory);
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
		return new ChestCoreSingle(type, inventory.capacity());
	}

	@Override
	public SingleItem getRenderItem() {
		return ((SingleItemInventory)inventory).getContents();
	}

}
