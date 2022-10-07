/**
 * 
 */
package mmb.world.crafting.helper;

import java.util.Objects;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.util.concurrent.Runnables;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.debug.Debugger;
import mmb.world.crafting.RecipeOutput;
import mmb.world.crafting.Refreshable;
import mmb.world.crafting.SimpleItemList;
import mmb.world.electric.Battery;
import mmb.world.electric.VoltageTier;
import mmb.world.electromachine.CycleResult;
import mmb.world.inventory.Inventory;
import mmb.world.inventory.storage.SimpleInventory;
import mmb.world.items.ItemEntry;
import mmb.world.recipes.ComplexCatalyzedRecipeGroup;
import mmb.world.recipes.ComplexRecipeGroup;
import mmb.world.recipes.ComplexCatalyzedRecipeGroup.ComplexCatalyzedRecipe;
import mmb.world.recipes.ComplexRecipeGroup.ComplexRecipe;

/**
 * @author oskar
 *
 */
public class ComplexCatalyzedItemProcessHelper {
	@Nonnull private final ComplexCatalyzedRecipeGroup recipes;
	@Nonnull private final Inventory input;
	@Nonnull private final Inventory output;
	         private final double speed;
	@Nonnull private final Battery elec;
	@Nonnull private final VoltageTier volt;
	/** The object which is currently refreshed. It may be null */
	public Refreshable refreshable;
	/** Last known recipe. Redone immediately if required items still exist */
	public ComplexCatalyzedRecipe lastKnown;
	/** Energy put into item to smelt it */
	public double progress;
	/** Energy required to finish this recipe */
	public double currRequired;
	/** The item produced by a recipe*/
	public RecipeOutput rout;
	
	public final Supplier<@Nullable ItemEntry> selector;
	
	public ComplexCatalyzedItemProcessHelper(ComplexCatalyzedRecipeGroup recipes, Inventory input, Inventory output,
			double speed, Battery elec, VoltageTier volt, Supplier<@Nullable ItemEntry> selector) {
		super();
		this.recipes = recipes;
		this.input = input;
		this.output = output;
		this.speed = speed;
		this.elec = elec;
		this.volt = volt;
		this.selector = selector;
	}
	public void save(ObjectNode node) {
		SimpleInventory intermediateInv = new SimpleInventory();
		intermediateInv.setCapacity(999);
		JsonNode smeltData = null;
		if(rout != null) {
			rout.produceResults(intermediateInv.createWriter());
			smeltData = intermediateInv.save();
		}
		node.set("smelt", smeltData);
		node.put("remain", progress);
		node.put("energy", currRequired);
	}
	public void load(JsonNode data) {
		//Load the recipe
		JsonNode itemUnderWay = data.get("smelt");
		debug.printl("IUW data:"+itemUnderWay);
		SimpleInventory intermediateInv = new SimpleInventory();
		rout = null;
		if(itemUnderWay != null && !itemUnderWay.isNull()) {
			intermediateInv.load(itemUnderWay);
			debug.printl("IUW after load:"+intermediateInv);
			rout = new SimpleItemList(intermediateInv);
		}
		JsonNode nodeReqEnergy = data.get("energy");
		if(nodeReqEnergy != null) currRequired = nodeReqEnergy.asDouble();
		if(!Double.isFinite(currRequired)) currRequired = 0;
		
		JsonNode remainNode = data.get("remain");
		if(remainNode != null) progress = remainNode.asDouble();
	}
	
	public CycleResult cycle() {
		CycleResult result = internals();
		if(refreshable != null) refreshable.refreshProgress(progress/currRequired, lastKnown);
		return result;
	}
	public CycleResult internals() {
		
		if(progress < 0 || !Double.isFinite(currRequired)) {
			//Invalid progress, it is negative, infinite or NaN
			progress = 0;
		} else if(rout == null || progress > currRequired) {
			//Time to take a new item
			//TODO find a recipe
			if(lastKnown != null && Inventory.howManyTimesThisContainsThat(input, lastKnown.input) > 0) {
				//There are enough items to use LKRecipe, reuse it
				//Extract required items
				debug.printl("Finding a recipe(reuse)");
				useRecipe(lastKnown);
				extractItems(lastKnown.input);
				return CycleResult.WITHDRAW;
			}
			//There aren't enough items to use LKRecipe, or it is not set, find a new one
			//Reset LKRecipe
			lastKnown = null;
			
			//Check ingredient count before searching recipes
			if(input.size() < recipes.minIngredients) return CycleResult.UNSUPPORTED;
			
			//Find a new recipe(slow) and extract if found
			for(ComplexCatalyzedRecipe recipe: recipes.recipes) {
				if(Inventory.howManyTimesThisContainsThat(input, recipe.input) <= 0)
					//The required ingredients were not found
					continue;
				//Check catalyst
				if(!Objects.equals(selector.get(), recipe.catalyst))
					//The catalyst does not match
					continue;
				//Potential match, check voltages
				if(recipe.voltage.compareTo(volt) <= 0) {
					//Voltage satisfied, use it
					useRecipe(recipe);
					extractItems(recipe.input);
					return CycleResult.WITHDRAW;
				}
			}
		}else{
			//Continue smelting
			double amps = volt.speedMul * speed / volt.volts;
			double extract = elec.extract(amps, volt, Runnables.doNothing());
			elec.pressure -= (amps-extract)*volt.volts;
			progress += volt.volts * extract;
			if(progress > currRequired) {
				//Time to eject an item
				//Eject expected item
				progress -= currRequired;
				rout.produceResults(output.createWriter());
				if(refreshable != null) refreshable.refreshOutputs();
				rout = null;
				return CycleResult.OUTPUT;
			}// else continue smelting
		}
		if(refreshable != null) refreshable.refreshProgress(progress, lastKnown);
		return CycleResult.RUN;
	}
	@Nonnull private static final Debugger debug = new Debugger("COMPLEX RECIPE RPOCESSOR");
	private void useRecipe(ComplexCatalyzedRecipe recipe) {
		lastKnown = recipe;
		rout = recipe.output;
		currRequired = recipe.energy;
	}
	private void extractItems(RecipeOutput out) {
		//this does not extract items
		for(Entry<ItemEntry> ent: out.getContents().object2IntEntrySet()) {
			input.extract(ent.getKey(), ent.getIntValue());
		}
		if(refreshable != null) refreshable.refreshInputs();
	}
	
	/**
	 * @param helper
	 */
	public void set(ComplexCatalyzedItemProcessHelper helper) {
		lastKnown = helper.lastKnown;
		progress = helper.progress;
		currRequired = helper.currRequired;
		rout = helper.rout;
	}
}
