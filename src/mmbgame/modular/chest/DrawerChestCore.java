/**
 * 
 */
package mmbgame.modular.chest;

import javax.annotation.Nonnull;

import mmb.menu.world.inv.AbstractInventoryController;
import mmb.menu.world.inv.InventoryController;
import mmbeng.craft.SingleItem;
import mmbeng.inv.storage.SingleStackedInventory;
import mmbeng.item.ItemEntry;
import mmbgame.modular.part.PartEntityType;
import mmbgame.modular.part.PartEntry;

/**
 * @author oskar
 *
 */
public class DrawerChestCore extends ChestCore<SingleStackedInventory> {
	@Nonnull private final PartEntityType type;
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
	public ChestCore<SingleStackedInventory> makeEmpty() {
		return new DrawerChestCore(type, inventory.capacity());
	}

	@Override
	public SingleItem getRenderItem() {
		return inventory.getStack();
	}

}
