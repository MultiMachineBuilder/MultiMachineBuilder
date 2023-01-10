/**
 * 
 */
package mmb.content.imachine.filter;

import java.util.function.Predicate;

import mmb.Nil;
import mmb.engine.item.ItemEntity;
import mmb.engine.item.ItemEntry;

/**
 * Abstraction over item filters
 * @author oskar
 *
 */
public abstract class ItemFilter extends ItemEntity implements Predicate<@Nil ItemEntry> {
	/**
	 * Tests the item type
	 * @param item item entry to test
	 * @return does item fit the filter?
	 */
	@Override public abstract boolean test(@Nil ItemEntry item); //NOSONAR the documentation is replaced
}
