/**
 * 
 */
package mmb.world.modulars.chest;

import javax.annotation.Nonnull;

import mmb.menu.world.inv.AbstractInventoryController;
import mmb.menu.world.inv.InventoryController;
import mmb.world.crafting.SingleItem;
import mmb.world.inventory.storage.SimpleInventory;
import mmb.world.item.ItemEntityType;
import mmb.world.item.ItemEntry;
import mmb.world.part.PartEntityType;
import mmb.world.part.PartEntry;

/**
 * @author oskar
 *
 */
public class SimpleChestCore extends ChestCore<SimpleInventory> {
	@Nonnull private final PartEntityType type;
	/**
	 * Creates a simple chest core (many different items)
	 * @param type item entity type
	 * @param size volume in cubic meters
	 */
	public SimpleChestCore(PartEntityType type, double size) {
		super(new SimpleInventory().setCapacity(size));
		this.type = type;
	}

	@Override
	public PartEntry partClone() {
		SimpleChestCore copy = new SimpleChestCore(type, 0);
		copy.inventory.set(inventory);
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
	public ChestCore<SimpleInventory> makeEmpty() {
		return new SimpleChestCore(type, inventory.capacity());
	}

	@Override
	public SingleItem getRenderItem() {
		return null;
	}

}
