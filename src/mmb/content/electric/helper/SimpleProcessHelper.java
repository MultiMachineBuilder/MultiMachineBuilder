/**
 * 
 */
package mmb.content.electric.helper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.util.concurrent.Runnables;

import mmb.content.electric.Battery;
import mmb.content.electric.VoltageTier;
import mmb.content.electric.machines.CycleResult;
import mmb.engine.craft.Refreshable;
import mmb.engine.craft.singles.SimpleRecipe;
import mmb.engine.craft.singles.SimpleRecipeGroup;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.ItemEntry;

/**
 * @author oskar
 * A class to help make simple item processors
 */
public class SimpleProcessHelper{
	@Nonnull private final SimpleRecipeGroup<?> recipes;
	@Nonnull private final Inventory input;
	@Nonnull private final Inventory output;
	/**  The inventory to source catalysts from */
	public final SingleItemInventory catalysts;
	private final double speed;
	@Nonnull private final Battery elec;
	@Nonnull private final VoltageTier volt;
	/** The object which is currently refreshed. It may be null */
	public Refreshable refreshable;
	
	/** Energy put into item to smelt it */
	public double progress;
	
	//Info about recipe
	public double currRequired;
	
	/** The item which is currently smelted */
	public SimpleRecipe<?> underway;
	
	/**Required voltage tier */
	public VoltageTier voltRequired;
	
	public ItemEntry catalyst() {
		if(catalysts == null) return null;
		return catalysts.getContents();
	}
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
			double speed, Battery elec, @Nullable SingleItemInventory catalysts, VoltageTier volt) {
		super();
		this.recipes = recipes;
		this.input = input;
		this.output = output;
		this.speed = speed;
		this.elec = elec;
		this.volt = volt;
		this.catalysts = catalysts;
	}
	
	public void save(ObjectNode node) {
		JsonNode smeltData = null;
		if(underway != null) smeltData = ItemEntry.saveItem(underway.inputs().item());
		node.set("smelt", smeltData);
		node.set("remain", new DoubleNode(progress));
		if(catalysts != null) {
			if(underway == null) node.set("catalyst", null);
			else node.set("catalyst", ItemEntry.saveItem(underway.catalyst()));
		}
	}
	public void load(JsonNode data) {
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
	
	public CycleResult cycle() {
		CycleResult result = internals();
		if(refreshable != null) refreshable.refreshProgress(progress/currRequired, underway);
		return result;
	}
	public CycleResult internals() {
		if(underway == null || progress > underway.energy()) {
			int hasAttempted = 0;
			//Time to take a new item
			loop:
			for(ItemRecord ir: input) {
				/*
				 * The criteria are:
				 * * The item exists in the machine
				 * * The item is a supported recipe
				 * * The recipe's voltage tier is not higher than this processor's voltage tier
				 */
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
					currRequired = candidate.energy();
					if(refreshable != null) refreshable.refreshInputs();
					return CycleResult.WITHDRAW;
				}
				//else item is not smeltable, do not take it
			}
			if(hasAttempted == 2) return CycleResult.PARTIAL;
			if(hasAttempted == 1) return CycleResult.UNSUPPORTED;
			return CycleResult.EMPTY;
		}else if(progress < 0) {
			progress = 0;
		}else{
			//Continue smelting
			double amps = volt.speedMul * speed / volt.volts;
			double extract = elec.extract(amps, volt, Runnables.doNothing());
			elec.pressure -= (amps-extract)*volt.volts;
			progress += volt.volts * extract;
			if(progress > currRequired) {
				//Time to eject an item
				//Eject expected item
				InventoryWriter writer = output.createWriter();
				underway.output().produceResults(writer);
				progress -= currRequired;
				//Eject chanced outputs
				underway.luck().produceResults(writer);
				underway = null;
				if(refreshable != null) refreshable.refreshOutputs();
				return CycleResult.OUTPUT;
			}// else continue smelting
		}
		return CycleResult.RUN;
	}
	/**
	 * @param helper
	 */
	public void set(SimpleProcessHelper helper) {
		progress = helper.progress;
		currRequired = helper.currRequired;
		underway = helper.underway;
		voltRequired = helper.voltRequired;
	}
}
