/**
 * 
 */
package mmb.content.electric.helper;

import java.util.Set;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.util.concurrent.Runnables;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.NN;
import mmb.Nil;
import mmb.content.electric.Battery;
import mmb.content.electric.VoltageTier;
import mmb.content.electric.machines.CycleResult;
import mmb.content.electric.machines.GUIMachine;
import mmb.engine.craft.ItemLists;
import mmb.engine.craft.Recipe;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.craft.Refreshable;
import mmb.engine.craft.singles.MultiRecipeGroup;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.json.JsonTool;

/**
 * @author oskar
 *
 */
public class ComplexProcessHelper implements Helper<Recipe<?>> {
	//Connection with the block
	@NN private final Inventory input;
	@NN private final Inventory output;
	public final SingleItemInventory catalysts;
	@NN private final Battery elec;
	/** The object which is currently refreshed. It may be null */
	public Refreshable refreshable;
	@Override
	public void setRefreshable(@Nil GUIMachine tab) {
		refreshable = tab;
	}
	
	//Process definition
	@NN private final MultiRecipeGroup<?> recipes;
    private final double speed;
    @NN private final VoltageTier volt;
	
	//Recipe info
	/** Last known recipe. Redone immediately if required items still exist */
	public Recipe<?> underway;
	@Override
	public Recipe<?> currentRecipe() {
		return underway;
	}
	
	//State info
	/** Energy put into item to smelt it */
	public double progress;
	public ItemEntry catalyst() {
		if(catalysts == null) return null;
		return catalysts.getContents();
	}
	
	//Constructor
	public ComplexProcessHelper(MultiRecipeGroup<?> recipes, Inventory input, Inventory output,
			double speed, Battery elec, VoltageTier volt, @Nil SingleItemInventory selector) {
		super();
		this.recipes = recipes;
		this.input = input;
		this.output = output;
		this.speed = speed;
		this.elec = elec;
		this.volt = volt;
		this.catalysts = selector;
	}
	
	//Serialization
	@Override
	public void save(ObjectNode node) {
		node.put("remain", progress);
		
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
		
		JsonNode recipenode = data.get("recipe");
		underway = null;
		if(recipenode != null) {
			ItemEntry catalyst = ItemEntry.loadFromJson(recipenode.get(0));
			RecipeOutput inputs = ItemLists.read(recipenode.get(1));
			if(inputs != null) underway = recipes.findExact(inputs.items(), catalyst);
		}
	}
	public void set(ComplexProcessHelper helper) {
		underway = helper.underway;
		progress = helper.progress;
	}
	
	//Recipe handling
	Debugger debug = new Debugger("COMPLEX PROCESS HELPER");
	@Override
	public CycleResult cycle() {
		CycleResult result = internals();
		double energy = 0;
		if(underway != null) energy = underway.energy();
		if(refreshable != null) refreshable.refreshProgress(progress/energy, underway);
		return result;
	}
	public CycleResult internals() {
		CycleResult result = CycleResult.RUN;
		
		//Find recipes check
		Recipe<?> underway1 = underway;
		if(underway1 == null) {
			//Find a new recipe
			ItemEntry catalyst = catalyst();
			result = CycleResult.EMPTY;
			loop:
			for(ItemRecord ir: input) {
				ItemEntry candidateCheck = ir.item();
				@SuppressWarnings("unchecked")
				Set<Recipe<?>> candidates = (Set<Recipe<?>>) recipes.findPlausible(candidateCheck);
				if(candidates == null) continue loop;
				result = CycleResult.UNSUPPORTED;
				inner:
				for(Recipe<?> candidate: candidates) {
					result = CycleResult.PARTIAL;
					if(candidate.voltTier().compareTo(volt) > 0) { //candidate.voltTier() > volt
						//The voltage tier is too high
						result = CycleResult.UNSUPPORTED;
						continue inner;
					}
					if(Inventory.howManyTimesThisContainsThat(input, candidate.inputs()) > 0) {
						//Candidate accepted
						useRecipe(candidate);
						break loop;
					}
				}
			}
		}
		
		//Item collection check
		Recipe<?> underway2 = underway;
		if(underway2 != null){
			//Reuse the recipe
			boolean collect = input.bulkExtract(underway2.inputs(), 1) == 1;
			if(collect) {
				result = CycleResult.WITHDRAW;
			}else {
				underway = null;
				result = CycleResult.PARTIAL;
			}
		}
		
		//Main process
		if(underway != null && progress < underway.energy()) {
			//Continue smelting
			double amps = volt.speedMul * speed / volt.volts;
			double extract = elec.extract(amps, volt, Runnables.doNothing());
			elec.pressure -= (amps-extract)*volt.volts;
			progress += volt.volts * extract;
			debug.printl("Progress");
		}
		
		//FIXME this eats produced items
		//Ejection check
		Recipe<?> recipe3 = underway;
		if(recipe3 != null && progress >= recipe3.energy()) {
			//Time to eject an item
			//Eject expected item
			InventoryWriter writer = output.createWriter();
			RecipeOutput iresults = recipe3.output();
			boolean insert = writer.bulkInsert(iresults);
			if(insert) {
				progress -= recipe3.energy();
				//Eject chanced outputs
				recipe3.luck().produceResults(writer);
				if(refreshable != null) refreshable.refreshOutputs();
				result = CycleResult.OUTPUT;
			}else {
				debug.printl("Item insertion failed");
			}
		}
		
		if(refreshable != null) refreshable.refreshProgress(progress, underway);
		return result;
	}
	
	private void useRecipe(Recipe<?> candidate) {
		underway = candidate;
	}
	private void extractItems(RecipeOutput out) {
		//this does not extract items
		for(Entry<@NN ItemEntry> ent: out.getContents().object2IntEntrySet()) {
			input.extract(ent.getKey(), ent.getIntValue());
		}
		if(refreshable != null) refreshable.refreshInputs();
	}	
}
