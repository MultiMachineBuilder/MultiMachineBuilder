/**
 * 
 */
package mmb.WORLD.items.filter;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import mmb.WORLD.item.ItemEntity;
import mmb.WORLD.item.ItemEntityType;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 *
 */
public abstract class ItemFilter extends ItemEntity implements Predicate<ItemEntry> {

	/**
	 * Constructs an item filter
	 * @param type
	 */
	protected ItemFilter(@Nonnull ItemEntityType type) {
		super(type);
	}
	
	/**
	 * Tests the item type
	 * @param item item entry to test
	 * @return does item fit the filter?
	 */
	@Override public abstract boolean test(ItemEntry item); //NOSONAR the documentation is replaced

}
