/**
 * 
 */
package mmb.content.modular.chest;

import mmb.NN;
import mmb.content.modular.part.PartEntityType;
import mmb.content.modular.part.PartEntry;
import mmb.engine.craft.SingleItem;
import mmb.engine.inv.storage.SimpleInventory;
import mmb.engine.item.ItemEntityType;
import mmb.engine.item.ItemEntry;
import mmbbase.menu.world.inv.AbstractInventoryController;
import mmbbase.menu.world.inv.InventoryController;

/**
 * @author oskar
 *
 */
public class SimpleChestCore extends ChestCore<SimpleInventory> {
	@NN private final PartEntityType type;
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
