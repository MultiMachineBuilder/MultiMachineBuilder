/**
 * 
 */
package mmb.content.electric.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vavr.Tuple2;
import mmb.NN;
import mmb.Nil;
import mmb.content.electric.Battery;
import mmb.content.electric.VoltageTier;
import mmb.content.electric.machines.CycleResult;
import mmb.content.electric.recipes.SimpleRecipe;
import mmb.content.electric.recipes.SimpleRecipeGroup;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.ItemEntry;

/**
 * Implements the processing of a stacked or single catalyzed or uncatalyzed recipes in a machine.
 * @author oskar
 * @param <Trecipe> type of recipes
 * @see mmb.content.electric.machines.ProcessorSimpleCatalyzedBlock ProcessorSimpleCatalyzedBlock
 * @see mmb.content.electric.machines.ProcessorSimpleBlock ProcessorSimpleBlock
 * @see ComplexProcessHelper
 */
public class SimpleProcessHelper<@NN Trecipe extends SimpleRecipe<@NN Trecipe>> extends Helper<@NN Trecipe, mmb.content.electric.recipes.SimpleRecipeGroup<@NN Trecipe>>{
	//Constructor
	/**
	 * @param recipes list of recipes to use
	 * @param input input inventory
	 * @param output output inventory
	 * @param speed processing current in joules per tick at ULV
	 * @param elec the power source
	 * @param selector inventory to source catalysts from. Set to null to remove catalyst support
	 * @param volt voltage tier
	 */
	public SimpleProcessHelper(SimpleRecipeGroup<Trecipe> recipes, Inventory input, Inventory output,
			double speed, Battery elec, VoltageTier volt, @Nil SingleItemInventory selector) {
		super(recipes, input, output, speed, elec, volt, selector);
	}
	
	//Serialization
	@Override
	public void save(ObjectNode node) {
		JsonNode smeltData = null;
		if(underway != null) smeltData = ItemEntry.saveItem(underway.inputs().item());
		node.set("smelt", smeltData);
		
		node.put("active", active);
		node.set("remain", new DoubleNode(progress));
		
		if(catalysts != null) {
			SimpleRecipe uway0 = underway;
			node.set("catalyst", ItemEntry.saveItem(uway0==null?null:uway0.catalyst()));
		}
	}
	
	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		JsonNode itemUnderWay = data.get("smelt");
		ItemEntry item = ItemEntry.loadFromJson(itemUnderWay);
		
		JsonNode activeNode = data.get("active");
		if(activeNode != null) active = activeNode.asBoolean();
		JsonNode remainNode = data.get("remain");
		if(remainNode != null) progress = remainNode.asDouble();
		
		JsonNode catalystUnderWay = data.get("catalyst");
		ItemEntry catalyst = ItemEntry.loadFromJson(catalystUnderWay);
		if(item == null) {
			underway = null;
		}else {
			underway = recipes.findRecipe(catalyst, item);
		}
		
		
	}
	/**
	 * Sets this recipe helper state to the other's recipe helper state
	 * @param helper the source helper
	 */
	public void set(SimpleProcessHelper<Trecipe> helper) {
		progress = helper.progress;
		underway = helper.underway;
		active  = helper.active;
	}

	@Override
	public @NN Tuple2<@Nil Trecipe, @NN CycleResult> findRecipes() {
		CycleResult result = CycleResult.RUN;
		@Nil Trecipe underway1 = underway;
		if(underway1 == null) {
			int hasAttempted = 0;
			//Time to take a new item
			loop:
			for(ItemRecord ir: input) {
				/* The criteria are:
				 * The item exists in the machine
				 * The item is a supported recipe
				 * The recipe's voltage tier is not higher than this processor's voltage tier */
				if(ir.amount() == 0) {
					//Item does not exist
					continue loop;
				}
				if(hasAttempted == 0) hasAttempted = 1;
				@Nil Trecipe candidate = recipes.findRecipe(catalyst(), ir.item());
				if(candidate == null) {
					//Recipe does not exist
					continue loop;
				}
				if(ir.amount() < candidate.inputs().amount()) {
					//There is not enough of specified item
					hasAttempted = 2;
					continue loop;
				}
				if(candidate.voltTier().compareTo(volt) > 0) {
					//The voltage tier is too high
					continue loop;
				}//Item is smeltable, get a recipe
				return new Tuple2<>(candidate, CycleResult.WITHDRAW);
				//else item is not smeltable, do not take it
			}
			if(hasAttempted == 2) result = CycleResult.PARTIAL;
			else if(hasAttempted == 1) result = CycleResult.UNSUPPORTED;
			else result = CycleResult.EMPTY;
		}
		return new Tuple2<>(null, result);
	}
}
