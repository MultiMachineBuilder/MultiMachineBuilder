/**
 * 
 */
package mmb.WORLD.crafting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.util.concurrent.Runnables;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.WORLD.crafting.recipes.ComplexProcessingRecipeGroup;
import mmb.WORLD.crafting.recipes.ComplexProcessingRecipeGroup.ComplexProcessingRecipe;
import mmb.WORLD.electric.Battery;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.storage.SimpleInventory;
import mmb.WORLD.items.ItemEntry;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class ComplexItemProcessHelper {
	@Nonnull private final ComplexProcessingRecipeGroup recipes;
	@Nonnull private final Inventory input;
	@Nonnull private final Inventory output;
	         private final double speed;
	@Nonnull private final Battery elec;
	@Nonnull private final VoltageTier volt;
	/** The object which is currently refreshed. It may be null */
	public Refreshable refreshable;
	/** Last known recipe. Redone immediately if required items still exist */
	public ComplexProcessingRecipe lastKnown;
	/** Energy put into item to smelt it */
	public double progress;
	/** Energy required to finish this recipe */
	public double currRequired;
	/** The item produced by a recipe*/
	public RecipeOutput rout;
	
	public ComplexItemProcessHelper(ComplexProcessingRecipeGroup recipes, Inventory input, Inventory output,
			double speed, Battery elec, VoltageTier volt) {
		super();
		this.recipes = recipes;
		this.input = input;
		this.output = output;
		this.speed = speed;
		this.elec = elec;
		this.volt = volt;
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
	
	public void cycle() {
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
				return;
			}
			//There aren't enough items to use LKRecipe, or it is not set, find a new one
			//Reset LKRecipe
			lastKnown = null;
			
			//Check ingredient count before searching recipes
			if(input.size() < recipes.minIngredients) return;
			
			//Find a new recipe(slow) and extract if found
			for(ComplexProcessingRecipe recipe: recipes.recipes) {
				if(Inventory.howManyTimesThisContainsThat(input, recipe.input) <= 0)
					//The required ingredients were not found
					continue;
				//Potential match, check voltages
				if(recipe.voltage.compareTo(volt) <= 0) {
					//Voltage satisfied, use it
					useRecipe(recipe);
					extractItems(recipe.input);
					return;
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
			}// else continue smelting
		}
		if(refreshable != null) refreshable.refreshProgress(progress, rout, currRequired);
	}
	@Nonnull private static final Debugger debug = new Debugger("COMPLEX RECIPE RPOCESSOR");
	private void useRecipe(ComplexProcessingRecipe recipe) {
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
		 * @param output item which is currently smelted
		 * @param max energy required to complete the recipe
		 */
		public void refreshProgress(double progress, @Nullable RecipeOutput output, double max);
	}
}
