/**
 * 
 */
package mmbgame.modular.chest;

import javax.annotation.Nonnull;

import mmb.menu.world.inv.AbstractInventoryController;
import mmb.menu.world.inv.InventoryController;
import mmbeng.craft.SingleItem;
import mmbeng.inv.storage.SetInventory;
import mmbeng.inv.storage.SimpleInventory;
import mmbeng.item.ItemEntityType;
import mmbeng.item.ItemEntry;
import mmbgame.modular.part.PartEntity;
import mmbgame.modular.part.PartEntityType;
import mmbgame.modular.part.PartEntry;

/**
 * @author oskar
 *
 */
public class ChestCoreSet extends ChestCore<SetInventory<ItemEntry>> {
	@Nonnull private final PartEntityType type;
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
