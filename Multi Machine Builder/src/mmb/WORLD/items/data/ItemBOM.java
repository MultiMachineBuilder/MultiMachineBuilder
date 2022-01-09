/**
 * 
 */
package mmb.WORLD.items.data;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.item.ItemEntity;
import mmb.WORLD.items.ContentsItems;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 * An item for Bill Of Materials, which contains a list of items.
 */
public final class ItemBOM extends ItemEntity {

	/**
	 * Creates a Bill of Materials
	 */
	public ItemBOM() {
		super(ContentsItems.BOM);
	}
	
	@Override
	public ItemEntry itemClone() {
		return this;
	}

	private RecipeOutput items;
	
	/**
	 * @return the item list for this Bill Of Materials. The returned item list is immutable
	 */
	public RecipeOutput contents() {
		return items;
	}
	
	@Override
	public void load(@Nullable JsonNode data) {
		if(data == null) return;
		JsonNode contents = data.get("items");
	}

}
