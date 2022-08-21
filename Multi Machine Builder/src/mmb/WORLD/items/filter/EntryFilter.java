/**
 * 
 */
package mmb.WORLD.items.filter;

import java.util.Objects;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.davidmoten.rtree.internal.Comparators;

import mmb.WORLD.item.ItemEntity;
import mmb.WORLD.items.ContentsItems;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 * An item filter which filters by entry
 */
public class EntryFilter extends ItemFilter {
	private ItemEntry item;
	public EntryFilter() {
		super(ContentsItems.ifilterEntries);
		this.item = null;
	}
	
	public EntryFilter(ItemEntry item) {
		super(ContentsItems.ifilterEntries);
		this.item = item;
	}

	@Override
	public ItemEntry itemClone() {
		return this;
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

	@Override
	protected int hash0() {
		return Objects.hashCode(item);
	}

	@Override
	protected boolean equal0(ItemEntity other) {
		return Objects.equals(((EntryFilter) other).item, item);
	}

}
