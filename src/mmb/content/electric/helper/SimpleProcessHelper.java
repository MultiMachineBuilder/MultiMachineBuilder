/**
 * 
 */
package mmb.content.electric.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.util.concurrent.Runnables;

import mmb.NN;
import mmb.Nil;
import mmb.content.electric.Battery;
import mmb.content.electric.VoltageTier;
import mmb.content.electric.machines.CycleResult;
import mmb.content.electric.machines.GUIMachine;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.craft.Refreshable;
import mmb.engine.craft.singles.SimpleRecipe;
import mmb.engine.craft.singles.SimpleRecipeGroup;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.ItemEntry;

/**
 * @author oskar
 * A class to help make simple item processors
 */
public class SimpleProcessHelper implements Helper<SimpleRecipe<?>>{
	//Connection with the block
	@NN private final Inventory input;
	@NN private final Inventory output;
	/**  The inventory to source catalysts from */
	public final SingleItemInventory catalysts;
	
	@NN private final Battery elec;
	/** The object which is currently refreshed. It may be null */
	public Refreshable refreshable;
	@Override
	public void setRefreshable(@Nil GUIMachine tab) {
		refreshable = tab;
	}
	
	//Process definition
	@NN private final SimpleRecipeGroup<?> recipes;
	@NN private final VoltageTier volt;
	private final double speed;
	
	//Recipe info
	/** The item which is currently smelted */
	@Nil public SimpleRecipe<?> underway;
	@Override
	public SimpleRecipe<?> currentRecipe() {
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
	/**
	 * @param recipes list of recipes to use
	 * @param input input inventory
	 * @param output output inventory
	 * @param speed processing current in joules per tick at ULV
	 * @param elec the power source
	 * @param catalysts inventory to source catalysts from. Set to null to remove catalyst support
	 * @param volt voltage tier
	 */
	public SimpleProcessHelper(SimpleRecipeGroup<?> recipes, Inventory input, Inventory output,
			double speed, Battery elec, @Nil SingleItemInventory catalysts, VoltageTier volt) {
		super();
		this.recipes = recipes;
		this.input = input;
		this.output = output;
		this.speed = speed;
		this.elec = elec;
		this.volt = volt;
		this.catalysts = catalysts;
	}
	
	//Serialization
	@Override
	public void save(ObjectNode node) {
		JsonNode smeltData = null;
		if(underway != null) smeltData = ItemEntry.saveItem(underway.inputs().item());
		node.set("smelt", smeltData);
		node.set("remain", new DoubleNode(progress));
		if(catalysts != null) {
			SimpleRecipe<?> uway0 = underway;
			node.set("catalyst", ItemEntry.saveItem(uway0==null?null:uway0.catalyst()));
		}
	}
	@Override
	public void load(JsonNode data) {
		if(data == null) return;
		JsonNode itemUnderWay = data.get("smelt");
		ItemEntry item = ItemEntry.loadFromJson(itemUnderWay);
		JsonNode catalystUnderWay = data.get("catalyst");
		ItemEntry catalyst = ItemEntry.loadFromJson(catalystUnderWay);
		if(item == null) {
			underway = null;
		}else {
			underway = recipes.findRecipe(catalyst, item);
		}
		
		JsonNode remainNode = data.get("remain");
		if(remainNode != null) progress = remainNode.asDouble();
	}
	/**
	 * @param helper
	 */
	public void set(SimpleProcessHelper helper) {
		progress = helper.progress;
		underway = helper.underway;
	}
	
	//Recipe handling
	Debugger debug = new Debugger("SIMPLE PROCESS HELPER");
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
		
		//Item collection check
		SimpleRecipe<?> underway1 = underway;
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
				SimpleRecipe<?> candidate = recipes.findRecipe(catalyst(), ir.item());
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
				}
				//Item is smeltable, take it
				int extracted = ir.extract(candidate.inputs().amount());
				if(extracted >= 1) {
					//Extracted
					progress = 0;
					underway = candidate;
					if(refreshable != null) refreshable.refreshInputs();
					return CycleResult.WITHDRAW;
				}
				//else item is not smeltable, do not take it
			}
			if(hasAttempted == 2) result = CycleResult.PARTIAL;
			else if(hasAttempted == 1) result = CycleResult.UNSUPPORTED;
			else result = CycleResult.EMPTY;
		}else {
			boolean collect = input.bulkExtract(underway1.inputs(), 1) == 1;
			if(collect) {
				result = CycleResult.WITHDRAW;
			}else {
				underway = null;
				result = CycleResult.PARTIAL;
			}
		}
		
		//Main process
		SimpleRecipe<?> recipe2 = underway;
		if(progress < 0) {
			progress = 0;
		}
		if(recipe2 != null && progress < recipe2.energy()){
			//Continue smelting
			result = CycleResult.RUN;
			double amps = volt.speedMul * speed / volt.volts;
			double extract = elec.extract(amps, volt, Runnables.doNothing());
			elec.pressure -= (amps-extract)*volt.volts;
			progress += volt.volts * extract;
		}
		
		//Ejection check
		SimpleRecipe<?> recipe3 = underway;
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
		
		return result;
	}
}
