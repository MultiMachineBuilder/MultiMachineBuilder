/**
 * 
 */
package mmb.world.modulars.chest;

import javax.annotation.Nonnull;

import mmb.menu.world.inv.AbstractInventoryController;
import mmb.menu.world.inv.InventoryController;
import mmb.world.crafting.SingleItem;
import mmb.world.inventory.basic.SetInventory;
import mmb.world.inventory.storage.SimpleInventory;
import mmb.world.item.ItemEntityType;
import mmb.world.items.ItemEntry;

/**
 * @author oskar
 *
 */
public class ChestCoreSet extends ChestCore<SetInventory<ItemEntry>> {
	@Nonnull private final ItemEntityType type;
	/**
	 * Creates a simple chest core (many different items)
	 * @param type item entity type
	 * @param size volume in cubic meters
	 */
	public ChestCoreSet(ItemEntityType type, double size) {
		super(SetInventory.create().setCapacity(size));
		this.type = type;
	}

	@Override
	public ItemEntry itemClone() {
		ChestCoreSet copy = new ChestCoreSet(type, 0);
		copy.inventory.setContents(inventory);
		return copy;
	}

	@Override
	public ItemEntityType type() {
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
