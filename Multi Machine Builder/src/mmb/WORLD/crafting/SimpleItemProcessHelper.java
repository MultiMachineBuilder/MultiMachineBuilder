/**
 * 
 */
package mmb.WORLD.crafting;

import java.util.Map;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 * A class to help make simple item processors
 */
public class SimpleItemProcessHelper{
	private final Map<ItemEntry, RecipeOutput> recipes;
	private final Inventory input;
	private final Inventory output;
	private final int cycleLength;
	/** The object which is currently refreshed. It may be null */
	public Refreshable refreshable;
	/** The item which is currently smelted */
	public ItemEntry underway;
	/** Remaining ticks for output item to be released */
	public int remaining;
	/**
	 * @param recipes list of recipes to use
	 * @param input input inventory
	 * @param output output inventory
	 * @param cycleLength the time it takes to process one item
	 */
	public SimpleItemProcessHelper(Map<ItemEntry, RecipeOutput> recipes, Inventory input, Inventory output,
			int cycleLength) {
		super();
		this.recipes = recipes;
		this.input = input;
		this.output = output;
		this.cycleLength = cycleLength;
	}
	
	public void save(ObjectNode node) {
		JsonNode smeltData = ItemEntry.saveItem(underway);
		node.set("smelt", smeltData);
		node.set("remain", new IntNode(remaining));
	}
	public void load(JsonNode data) {
		JsonNode itemUnderWay = data.get("smelt");
		underway = ItemEntry.loadFromJson(itemUnderWay);
		JsonNode remainNode = data.get("remain");
		if(remainNode != null) remaining = remainNode.asInt();
	}
	
	public void cycle() {
		if(remaining == 0) {
			//Time to take a new item
			for(ItemRecord ir: input) {
				if(ir.amount() == 0) continue;
				//Item exists
				if(recipes.containsKey(ir.item())) {
					//Item is smeltable, take it
					int extracted = ir.extract(1);
					if(extracted == 1) {
						//Extracted
						remaining = cycleLength;
						underway = ir.item();
						if(refreshable != null) refreshable.refreshInputs();
						return;
					}
				}//else item is not smeltable, do not take it
			}
		}else if(remaining < 0) {
			remaining = 0;
		}else{
			//Continue smelting
			remaining--;
			if(remaining == 0) {
				//Time to eject an item
				RecipeOutput result = recipes.get(underway);
				if(result == null) {
					//Unsmeltable, return original
					if(underway != null) output.insert(underway, 1);
				}else{
					//Smeltable, eject expected item
					result.produceResults(output.createWriter());
				}
				underway = null;
				if(refreshable != null) refreshable.refreshOutputs();
			}// else continue smelting
		}
		if(refreshable != null) refreshable.refreshProgress(cycleLength-remaining, underway);
	}
	
	/**
	 * @author oskar
	 * An object which is refreshed during processing
	 */
	public static interface Refreshable{
		/** Refreshes the input list */
		public void refreshInputs();
		/** Refreshes the output list */
		public void refreshOutputs();
		/** Refreshes the progress bar 
		 * @param progress processing progress in ticks
		 * @param underway item which is currently smelted
		 */
		public void refreshProgress(int progress, @Nullable ItemEntry underway);
	}
}
