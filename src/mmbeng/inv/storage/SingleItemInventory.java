/**
 * 
 */
package mmbeng.inv.storage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.data.variables.DataValue;
import mmb.data.variables.Variable;
import mmbeng.item.ItemEntry;

/**
 * @author oskar
 */
public class SingleItemInventory extends BaseSingleItemInventory {
	/** The item variable used by this inventory */
	@Nonnull public final Variable<@Nullable ItemEntry> itemvar;
	
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
	public SingleItemInventory(Variable<mmbeng.item.ItemEntry> itemvar) {
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
