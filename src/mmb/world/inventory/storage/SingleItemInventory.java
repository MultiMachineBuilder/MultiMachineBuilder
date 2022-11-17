/**
 * 
 */
package mmb.world.inventory.storage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import mmb.data.json.JsonTool;
import mmb.data.variables.DataValue;
import mmb.data.variables.Variable;
import mmb.world.item.ItemEntry;

/**
 * @author oskar
 *
 */
public class SingleItemInventory extends BaseSingleItemInventory {
	/** The item variable used by this inventory */
	@Nonnull public final Variable<mmb.world.item.ItemEntry> itemvar;
	
	/** Creates a single item inventory with a simple item variable and no items */
	public SingleItemInventory() {
		itemvar = new DataValue<>(null);
	}
	/**
	 * Creates a single item inventory with a simple item variable and an item
	 * @param item
	 */
	public SingleItemInventory(@Nullable ItemEntry item) {
		itemvar = new DataValue<>(item);
	}
	/**
	 * Creates a single item inventory with a custom item variable
	 * @param itemvar
	 */
	public SingleItemInventory(Variable<mmb.world.item.ItemEntry> itemvar) {
		this.itemvar = itemvar;
	}
	
	/** @return contents of this inventory */
	@Override
	public ItemEntry getContents() {
		return itemvar.get();
	}
	/**  @param contents new contents of this inventory */
	@Override
	public boolean setContents(@Nullable ItemEntry contents) {
		this.itemvar.set(contents);
		return true;
	}
	
	@Override
	@Nonnull public SingleItemInventory setCapacity(double cap) {
		super.setCapacity(cap);
		return this;
	}
}
