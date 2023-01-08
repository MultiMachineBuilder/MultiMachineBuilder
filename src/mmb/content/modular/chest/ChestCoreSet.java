/**
 * 
 */
package mmb.content.modular.chest;

import mmb.NN;
import mmb.content.modular.part.PartEntity;
import mmb.content.modular.part.PartEntityType;
import mmb.content.modular.part.PartEntry;
import mmb.engine.craft.SingleItem;
import mmb.engine.inv.storage.SetInventory;
import mmb.engine.inv.storage.SimpleInventory;
import mmb.engine.item.ItemEntityType;
import mmb.engine.item.ItemEntry;
import mmb.menu.world.inv.AbstractInventoryController;
import mmb.menu.world.inv.InventoryController;

/**
 * @author oskar
 *
 */
public class ChestCoreSet extends ChestCore<SetInventory<ItemEntry>> {
	@NN private final PartEntityType type;
	/**
	 * Creates a simple chest core (many different items)
	 * @param type item entity type
	 * @param size volume in cubic meters
	 */
	public ChestCoreSet(PartEntityType type, double size) {
		super(SetInventory.create().setCapacity(size));
		this.type = type;
	}

	@Override
	public PartEntry partClone() {
		ChestCoreSet copy = new ChestCoreSet(type, 0);
		copy.inventory.setContents(inventory);
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
	public ChestCore<SetInventory<ItemEntry>> makeEmpty() {
		return new ChestCoreSet(type, inventory.capacity());
	}

	@Override
	public SingleItem getRenderItem() {
		return null;
	}

	
}
