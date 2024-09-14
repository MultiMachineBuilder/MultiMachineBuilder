/**
 * 
 */
package mmb.content.ditems;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.NN;
import mmb.Nil;
import mmb.content.ContentsItems;
import mmb.content.imachine.filter.ItemFilter;
import mmb.engine.debug.Debugger;
import mmb.engine.item.ItemEntity;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemType;
import mmb.engine.recipe.ItemLists;
import mmb.engine.recipe.RecipeOutput;
import mmb.engine.recipe.SimpleItemList;

/**
 * An item for Bill Of Materials, which contains a list of items.
 * The Bill of Materials can also be used as an item filter
 * @author oskar
 */
public final class ItemBOM extends ItemFilter {
	private static final Debugger debug = new Debugger("BOM");
	//Constructors
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
	
	//Contents
	@NN private SimpleItemList items = SimpleItemList.EMPTY;
	/**
	 * @return the item list for this Bill Of Materials. The returned item list is immutable
	 */
	@NN public RecipeOutput contents() {
		return items;
	}
	@Override
	public String description() {
		return items.toString();
	}
	@Override
	public String title() {
		String superTitle = super.title();
		return items.getContents().isEmpty() ? superTitle : superTitle + "\n"+description();
		
	}
	//Item methods
	@Override
	public ItemEntry itemClone() {
		return this;
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
	
	//Serialization
	@Override
	public void load(@Nil JsonNode data) {
		debug.printl("Loading: " + (data == null ? "null" : data.toPrettyString()));
		if(data == null) return;
		SimpleItemList list0 = ItemLists.read(data);
		if(list0 == null) {
			debug.printl("Items are null");
			list0 = SimpleItemList.EMPTY;
		}
		items = list0;	
	}
	@Override
	public JsonNode save() {
		JsonNode data = ItemLists.save(items);
		debug.printl("Saving: " + data.toPrettyString());
		return data;
	}

	//Item filter
	@Override
	public boolean test(@Nil ItemEntry item) {
		return items.contains(item);
	}
}
