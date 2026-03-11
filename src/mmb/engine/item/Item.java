/**
 * 
 */
package mmb.engine.item;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.PropertyExtension;
import mmb.annotations.Nil;
import mmb.engine.debug.Debugger;
import monniasza.collects.Identifiable;

/**
 * A basic item.
 * <br> To create an item, specify title, texture and register the item. Description and volume may also be specified
 * @author oskar
 */
public class Item extends ItemType implements ItemEntry {
	/**
	 * Creates a new simple item
	 * @param id unique item ID
	 * @param properties additional properties
	 */
	@SafeVarargs
	public Item(String id, PropertyExtension... properties) {
		super(id, properties);
		setItemFactory((data, type) -> this);
	}
	
	/**
	 * Get the hash code, which is always the same as ID's hash code.
	 * @return item's hash code.
	 * @see java.lang.String#hashCode()
	 */
	@Override
	public final int hashCode() {
		return id().hashCode();
	}
	@Override
	public final boolean equals(@Nil Object other) {
		if(this == other) return true;
		if(other == null) return false;
		if(!(other instanceof Identifiable)) return false;
		Object id1 = ((Identifiable<?>)other).id();
		if(id() == id1) return true;
		if(id1 == null) return false;
		return id1.equals(id());
	}

	@Override
	public ItemEntry itemClone() {
		return this;
	}
	@Override
	public ItemType itemType() {
		return this;
	}
}
