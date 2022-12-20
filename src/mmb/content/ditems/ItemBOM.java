/**
 * 
 */
package mmb.content.ditems;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.NN;
import mmb.Nil;
import mmb.content.ContentsItems;
import mmb.content.imachine.filter.ItemFilter;
import mmb.engine.craft.ItemLists;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.craft.SimpleItemList;
import mmb.engine.item.ItemEntity;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemType;

/**
 * @author oskar
 * An item for Bill Of Materials, which contains a list of items.
 */
public final class ItemBOM extends ItemFilter {	
	/**
	 * Creates an empty Bill of Materials
	 */
	public ItemBOM() {
		//empty
	}
	/**
	 * Creates a Bill of Materials with items (optimized)
	 * @param items items to use
	 */
	public ItemBOM(SimpleItemList items) {
		this.items = items;
	}
	/**
	 * Creates a Bill of Materials with items (generic)
	 * @param items items to use
	 */
	public ItemBOM(RecipeOutput items) {
		if(items instanceof SimpleItemList) 
			this.items = (SimpleItemList) items;
		else
			this.items = new SimpleItemList(items);
	}
	
	@Override
	public ItemEntry itemClone() {
		return this;
	}

	@NN private SimpleItemList items = SimpleItemList.EMPTY;
	
	/**
	 * @return the item list for this Bill Of Materials. The returned item list is immutable
	 */
	@NN public RecipeOutput contents() {
		return items;
	}
	
	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		SimpleItemList list0 = ItemLists.read(data);
		if(list0 == null) list0 = SimpleItemList.EMPTY;
		items = list0;	
	}
	@Override
	public JsonNode save() {
		return ItemLists.save(items);
	}

	@Override
	protected int hash0() {
		return items.hashCode();
	}

	@Override
	protected boolean equal0(ItemEntity other) {
		if(other instanceof ItemBOM)
			return ((ItemBOM) other).contents().equals(items);
		return false;
	}
	@Override
	public ItemType type() {
		return ContentsItems.BOM;
	}
	
	@Override
	public boolean test(@Nil ItemEntry item) {
		return items.contains(item);
	}

	

}
