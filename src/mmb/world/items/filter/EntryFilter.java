/**
 * 
 */
package mmb.world.items.filter;

import java.util.Objects;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.world.item.ItemEntity;
import mmb.world.item.ItemType;
import mmb.world.items.ContentsItems;
import mmb.world.items.ItemEntry;

/**
 * @author oskar
 * An item filter which filters by entry
 */
public class EntryFilter extends ItemFilter {
	private ItemEntry item;
	public EntryFilter() {
		this.item = null;
	}
	
	public EntryFilter(ItemEntry item) {
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
	public JsonNode save() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public ItemType type() {
		return ContentsItems.ifilterEntries;
	}

	

}
