/**
 * 
 */
package mmb.WORLD.items.filter;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.WORLD.items.ContentsItems;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 * An item filter which filters by entry
 */
public class EntryFilter extends ItemFilter {

	public EntryFilter() {
		super(ContentsItems.ifilterEntries);
	}
	
	public EntryFilter(ItemEntry item) {
		super(ContentsItems.ifilterEntries);
	}

	@Override
	public ItemEntry itemClone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void load(@Nullable JsonNode data) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean test(ItemEntry item) {
		// TODO Auto-generated method stub
		return false;
	}

}
