/**
 * 
 */
package mmb.content.modular.chest;

import javax.annotation.Nonnull;

import mmb.content.modular.part.PartEntityType;
import mmb.content.modular.part.PartEntry;
import mmb.engine.craft.SingleItem;
import mmb.engine.inv.storage.SingleItemInventory;
import mmbbase.menu.world.inv.AbstractInventoryController;
import mmbbase.menu.world.inv.InventoryController;

/**
 * @author oskar
 *
 */
public class ChestCoreSingle extends ChestCore<SingleItemInventory> {
	@Nonnull private final PartEntityType type;
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
	public ChestCore<SingleItemInventory> makeEmpty() {
		return new ChestCoreSingle(type, inventory.capacity());
	}

	@Override
	public SingleItem getRenderItem() {
		return inventory.getContents();
	}

}
