/**
 * 
 */
package mmb.world.items.filter;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import mmb.world.item.ItemEntity;
import mmb.world.item.ItemEntityType;
import mmb.world.items.ItemEntry;

/**
 * @author oskar
 *
 */
public abstract class ItemFilter extends ItemEntity implements Predicate<@Nonnull ItemEntry> {

	/**
	 * Constructs an item filter
	 * @param type
	 */
	protected ItemFilter(ItemEntityType type) {
		super(type);
	}
	
	/**
	 * Tests the item type
	 * @param item item entry to test
	 * @return does item fit the filter?
	 */
	@Override public abstract boolean test(ItemEntry item); //NOSONAR the documentation is replaced

}
