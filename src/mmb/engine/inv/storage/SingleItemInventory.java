/**
 * 
 */
package mmb.engine.inv.storage;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.data.variables.DataValue;
import mmb.data.variables.Variable;
import mmb.engine.item.ItemEntry;

/**
 * @author oskar
 */
public class SingleItemInventory extends BaseSingleItemInventory {
	/** The item variable used by this inventory */
	@NN public final Variable<@Nil ItemEntry> itemvar;
	
	/** Creates a single item inventory with a simple item variable and no items */
	public SingleItemInventory() {
		itemvar = new DataValue<>(null);
	}
	/**
	 * Creates a single item inventory with a simple item variable and an item
	 * @param item
	 */
	public SingleItemInventory(@Nil ItemEntry item) {
		itemvar = new DataValue<>(item);
	}
	/**
	 * Creates a single item inventory with a custom item variable
	 * @param itemvar
	 */
	public SingleItemInventory(Variable<@Nil ItemEntry> itemvar) {
		this.itemvar = itemvar;
	}
	
	/** @return contents of this inventory */
	@Override
	public ItemEntry getContents() {
		return itemvar.get();
	}
	/**  @param contents new contents of this inventory */
	@Override
	public boolean setContents(@Nil ItemEntry contents) {
		this.itemvar.set(contents);
		return true;
	}
	
	@Override
	@NN public SingleItemInventory setCapacity(double cap) {
		super.setCapacity(cap);
		return this;
	}
}
