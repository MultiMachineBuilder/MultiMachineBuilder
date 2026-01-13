/**
 * 
 */
package mmb.content.electric.helper;

import java.util.Objects;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.util.concurrent.Runnables;

import io.vavr.Tuple2;
import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.content.electric.Battery;
import mmb.content.electric.VoltageTier;
import mmb.content.electric.machines.CycleResult;
import mmb.content.electric.machines.GUIMachine;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.Recipe;
import mmb.engine.recipe.RecipeGroup;
import mmb.engine.recipe.ItemList;
import mmb.engine.recipe.Refreshable;

/**
 * Handles processing logic for autocrafting machines (for all types)
 * @author oskar
 * @param <Trecipe> type of recipes
 * @param <Tgroup> type of recipe groups
 */
public abstract class Helper<@NN Trecipe extends Recipe<@NN Trecipe>, Tgroup extends RecipeGroup<? extends Trecipe>> {
	Debugger debug = new Debugger("PROCESS HELPER");
	
	//Connection with the block
	/** The inventory, from where items are consumed */
	@NN public final Inventory input;
	/** The inventory, to where items are inserted */
	@NN public final Inventory output;
	/**  The inventory to source catalysts from */
	public final SingleItemInventory catalysts;
	/** Source of electricity for this helper */
	@NN public final Battery elec;
	/** The object which is currently refreshed. It may be null */
	public Refreshable refreshable;
	/**
	 * Resets the GUI for this helper
	 * @param tab the GUI to use
	 */
	public final void setRefreshable(@Nil GUIMachine tab) {
		refreshable = tab;
	}
	
	//Process definition
	@NN public final Tgroup recipes;
	@NN public final VoltageTier volt;
	public final double speed;
	
	//Recipe info
	@Nil public Trecipe underway;
	/** @return the currently used recipe */
	public final Trecipe currentRecipe() {
		return underway;
	}
	
	//State info
	/** Energy put into item to smelt it */
	public double progress;
	/** Is the process active? Disables item extractions*/
	public boolean active;
	/** @return current catalyst, or null if none */
	public ItemEntry catalyst() {
		if(catalysts == null) return null;
		return catalysts.getContents();
	}
	
	//Constructor
	/**
	 * @param recipes list of recipes to use
	 * @param input input inventory
	 * @param output output inventory
	 * @param speed processing current in joules per tick at ULV
	 * @param elec the power source
	 * @param catalysts inventory to source catalysts from. Set to null to remove catalyst support
	 * @param volt voltage tier
	 */
	protected Helper(Tgroup recipes, Inventory input, Inventory output,
			double speed, Battery elec, VoltageTier volt, @Nil SingleItemInventory selector) {
		super();
		this.recipes = Objects.requireNonNull(recipes, "recipes is null");
		this.input = Objects.requireNonNull(input, "input is null");
		this.output = Objects.requireNonNull(output, "output is null");
		this.speed = speed;
		this.elec = Objects.requireNonNull(elec, "elec is null");
		this.volt = Objects.requireNonNull(volt, "volt is null");
		this.catalysts = selector;
	}
	
	//Serialization
	/**
	 * Saves data to the machine JSON node
	 * @param on node to save to
	 */
	public abstract void save(ObjectNode on);
	/**
	 * Reads data from JSON
	 * @param data node to load from
	 */
	public abstract void load(@Nil JsonNode data);
	
	//Template methods
	/** 
	 * @apiNote This template method is the only one implementation-specific
	 * @implSpec Find the recipe and return it, or null if not found. Respects catalysts
	 * @return a compatible recipe, or null
	 */
	public abstract @NN Tuple2<@Nil Trecipe, @NN CycleResult> findRecipes();	
	/** 
	 * Runs the crafting process
	 * @return the result of crafting processes
	 */
	public final CycleResult cycle() {
		CycleResult result = internals();
		double energy = 0;
		if(underway != null) energy = underway.energy();
		if(refreshable != null) refreshable.refreshProgress(progress/energy, underway);
		return result;
	}
	/**
	 * Internal implementation of the cycle() method
	 * @return the final outcome of the operation
	 */
	public final CycleResult internals() {
		CycleResult result = CycleResult.RUN;
		
		//Find recipes check
		if(underway == null) {
			active = false;
			@NN Tuple2<@Nil Trecipe, CycleResult> fr = findRecipes();
			underway = fr._1;
			result = fr._2;
		}
		
		//Item collection check
		Recipe<?> underway1 = underway;
		if(underway1 != null && !active){
			debug.printl("Item collection");
			//Reuse the recipe
			boolean collect = input.bulkExtract(underway1.inputs(), 1) == 1;
			if(collect) {
				active = true;
				result = CycleResult.WITHDRAW;
				if(refreshable != null) refreshable.refreshInputs();
			}else {
				underway = null;
				result = CycleResult.PARTIAL;
			}
		}
		
		//Main process
		@Nil Trecipe underway2 = underway;
		if(underway2 != null && progress < underway2.energy()) {
			//Continue smelting
			double amps = speed;
			double extract = elec.extract(amps, volt, Runnables.doNothing());
			progress += volt.volts * extract;
		}
		
		//Ejection check
		Recipe<?> underway3 = underway;
		if(underway3 != null && progress >= underway3.energy()) {
			//Time to eject an item
			//Eject expected item
			InventoryWriter writer = output.createWriter();
			ItemList iresults = underway3.output();
			boolean insert = writer.bulkInsert(iresults);
			if(insert) {
				progress -= underway3.energy();
				//Eject chanced outputs
				underway3.luck().produceResults(writer);
				if(refreshable != null) refreshable.refreshOutputs();
				result = CycleResult.OUTPUT;
				active = false;
			}
			if(refreshable != null) refreshable.refreshOutputs();
		}
		
		//Refresh
		if(refreshable != null) refreshable.refreshProgress(progress, underway);
		return result;
	}
	
	
}
