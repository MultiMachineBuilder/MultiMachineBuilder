/**
 * 
 */
package mmb.content.ditems;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.content.ContentsItems;
import mmb.engine.item.ItemEntity;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemType;
import mmb.engine.recipe.ProcessIngredients;

/**
 * Bill of Recipe Effects, a two-sided item list with separate set of inputs and outputs.
 * @author oskar
 */
public final class ItemPIngredients extends ItemEntity {
	/** Bill of Recipe Effects with no items */
	public static final ItemPIngredients EMPTY = new ItemPIngredients(ProcessIngredients.EMPTY);
	
	//Constructors
	/**
	 * Creates a bill of Recipe Effects with items
	 * @param items recipe input/output
	 */
	public ItemPIngredients(ProcessIngredients items) {
		this.items = items;
	}
	
	@Override
	public ItemEntry itemClone() {
		return this;
	}

	//Contents
	@NN private final ProcessIngredients items;
	/** @return the item list for this Bill Of Materials. The returned item list is immutable */
	@NN public ProcessIngredients contents() {
		return items;
	}
	
	//Item methods
	@Override
	protected int hash0() {
		return items.hashCode();
	}
	@Override
	protected boolean equal0(ItemEntity other) {
		if(other instanceof ItemPIngredients)
			return ((ItemPIngredients) other).contents().equals(items);
		return false;
	}
	@Override
	public ItemType itemType() {
		return ContentsItems.pingredients;
	}
	
	//Serialization
	public static ItemPIngredients load(@Nil JsonNode data) {
		if(data == null) return EMPTY;
		ProcessIngredients list0 = ProcessIngredients.read(data);
		if(list0 == null) list0 = ProcessIngredients.EMPTY;
		return new ItemPIngredients(list0);
	}
	@Override
	public JsonNode save() {
		return ProcessIngredients.save(items);
	}
}
