/**
 * 
 */
package mmb.content.ditems;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.NN;
import mmb.Nil;
import mmb.content.ContentsItems;
import mmb.engine.craft.ProcessIngredients;
import mmb.engine.item.ItemEntity;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemType;

/**
 * Bill of Recipe Effects, a two-sided item list with separate set of inputs and outputs.
 * @author oskar
 */
public final class ItemPIngredients extends ItemEntity {
	//Constructors
	/** Creates an empty Bill of Recipe Effects*/
	public ItemPIngredients() {
		//empty
	}
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
	@NN private ProcessIngredients items = ProcessIngredients.EMPTY;
	/**
	 * @return the item list for this Bill Of Materials. The returned item list is immutable
	 */
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
	public ItemType type() {
		return ContentsItems.pingredients;
	}
	
	//Serialization
	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		ProcessIngredients list0 = ProcessIngredients.read(data);
		if(list0 == null) list0 = ProcessIngredients.EMPTY;
		items = list0;	
	}
	@Override
	public JsonNode save() {
		return ProcessIngredients.save(items);
	}
}
