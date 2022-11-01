/**
 * 
 */
package mmb.world.modulars.chest;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.menu.world.inv.AbstractInventoryController;
import mmb.world.chance.Chance;
import mmb.world.inventory.Inventory;
import mmb.world.item.ItemEntityMutable;
import mmb.world.item.ItemType;
import mmb.world.items.ItemEntry;
import mmb.world.modulars.BlockCore;
import mmb.world.modulars.ModularBlock;

/**
 * A chest core
 * @author oskar
 * @param <Tinv> type of the inventory
 */
public abstract class ChestCore<Tinv extends Inventory> extends ItemEntityMutable implements BlockCore<ChestCore<Tinv>>{
	/** Base constructor for chest cores */
	protected ChestCore(Tinv inventory) {
		this.inventory = inventory;
	}
	
	@Override
	public Chance dropItems() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/** The inventory for the core*/
	public final Tinv inventory;

	/**
	 * Creates a new inventory controller
	 * @return a new inventory controller
	 */
	public abstract AbstractInventoryController invctrl();

	@Override
	public ItemEntry returnToPlayer() {
		// TODO Auto-generated method stub
		return BlockCore.super.returnToPlayer();
	}
}
