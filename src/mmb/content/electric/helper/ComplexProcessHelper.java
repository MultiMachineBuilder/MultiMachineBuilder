/**
 * 
 */
package mmb.content.electric.helper;

import java.util.Set;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vavr.Tuple2;
import mmb.NN;
import mmb.Nil;
import mmb.content.electric.Battery;
import mmb.content.electric.VoltageTier;
import mmb.content.electric.machines.CycleResult;
import mmb.content.electric.recipes.MultiRecipeGroup;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.json.JsonTool;
import mmb.engine.recipe.ItemLists;
import mmb.engine.recipe.Recipe;
import mmb.engine.recipe.RecipeOutput;

/**
 * Implements the processing of a complex catalyzed or uncatalyzed recipes in a machine.
 * @author oskar
 * @param <Trecipe> type of recipes
 * @see mmb.content.electric.machines.ProcessorComplexCatalyzedBlock ProcessorComplexCatalyzedBlock
 * @see mmb.content.electric.machines.ProcessorComplexCatalyzedBlock ProcessorComplexBlock
 * @see SimpleProcessHelper
 */
public class ComplexProcessHelper<@NN Trecipe extends Recipe<Trecipe>> extends Helper<@NN Trecipe, mmb.content.electric.recipes.MultiRecipeGroup<Trecipe>> {
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
	public ComplexProcessHelper(MultiRecipeGroup<Trecipe> recipes, Inventory input, Inventory output,
			double speed, Battery elec, VoltageTier volt, @Nil SingleItemInventory selector) {
		super(recipes, input, output, speed, elec, volt, selector);
	}
	
	//Serialization
	@Override
	public void save(ObjectNode node) {
		node.put("remain", progress);
		node.put("active", active);
		
		Recipe<?> underway1 = underway;
		if(underway1 == null) {
			node.set("recipe", null);
		}else {
			ArrayNode recipenode = JsonTool.newArrayNode();
			recipenode.add(ItemEntry.saveItem(underway1.catalyst()));
			recipenode.add(ItemLists.save(underway1.inputs()));
			node.set("recipe", recipenode);
		}
	}

	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		
		JsonNode remainNode = data.get("remain");
		if(remainNode != null) progress = remainNode.asDouble();
		
		JsonNode activeNode = data.get("active");
		if(activeNode != null) active = activeNode.asBoolean();
		
		JsonNode recipenode = data.get("recipe");
		underway = null;
		if(recipenode != null) {
			ItemEntry catalyst = ItemEntry.loadFromJson(recipenode.get(0));
			RecipeOutput inputs = ItemLists.read(recipenode.get(1));
			if(inputs != null) underway = recipes.findExact(inputs.items(), catalyst);
		}
	}
	/**
	 * Sets this recipe helper state to the other's recipe helper state
	 * @param helper the source helper
	 */
	public void set(ComplexProcessHelper<Trecipe> helper) {
		underway = helper.underway;
		progress = helper.progress;
		active  = helper.active;
	}

	@Override
	public @NN Tuple2<@Nil Trecipe, @NN CycleResult> findRecipes() {
		CycleResult result = CycleResult.RUN;
		Recipe<?> underway1 = underway;
		if(underway1 == null) {
			//Find a new recipe
			ItemEntry catalyst = catalyst();
			result = CycleResult.EMPTY;
			loop:
			for(ItemRecord ir: input) {
				ItemEntry candidateCheck = ir.item();
				Set<@NN Trecipe> candidates = recipes.findPlausible(candidateCheck);
				if(candidates == null) continue loop;
				result = CycleResult.UNSUPPORTED;
				inner:
				for(Trecipe candidate: candidates) {
					result = CycleResult.PARTIAL;
					if(candidate.voltTier().compareTo(volt) > 0) { //candidate.voltTier() > volt
						//The voltage tier is too high
						result = CycleResult.UNSUPPORTED;
						continue inner;
					}
					if(candidate.catalyst() != catalyst) {
						//Wrong catalyst
						result = CycleResult.UNSUPPORTED;
						continue inner;
					}
					if(Inventory.howManyTimesThisContainsThat(input, candidate.inputs()) > 0) {
						//Candidate accepted
						return new Tuple2<>(candidate, CycleResult.WITHDRAW);
					}
				}
			}
		}
		return new Tuple2<>(null, result);
	}
}
