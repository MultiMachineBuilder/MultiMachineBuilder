/**
 * 
 */
package mmb.WORLD.recipes;

import java.util.Objects;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.crafting.SingleItem;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.items.ItemEntry;
import mmb.debug.Debugger;

/**
 * @author oskar
 * Contains crafting utilities and recipe groups
 */
public class CraftingGroups {
	private static final Debugger debug = new Debugger("CRAFTING PROCESSOR");
	/**
	 * @param in ingredients
	 * @param out recipe output
	 * @param from source inventory
	 * @param to target inventory
	 * @return is crafting successfull?
	 */
	public static boolean transact(RecipeOutput in, RecipeOutput out, Inventory from, Inventory to) {
		//Count
		for(Entry<ItemEntry> ent: in.getContents().object2IntEntrySet()) {
			int amt = ent.getIntValue();
			int act = from.get(ent.getKey()).amount();
			if(act < amt) {
				debug.printl("no ins");
				return false;
			} //Not enough items in the source
		}
		//BULK INSERTION
		int insertions = to.bulkInsert(out, 1);
		if(insertions == 0) {
			debug.printl("failure");
			return false;
		}
		//Withdraw from input
		for(Entry<ItemEntry> ent: in.getContents().object2IntEntrySet()) {
			//somehow amount is 0
			//debug.printl("Requesting extraction on "+ent.getKey().title()+" in amount "+ent.getIntValue());
			int amt = from.extract(ent.getKey(), ent.getIntValue());
			//debug.printl("extract "+ent.getKey().title()+" in amount: "+amt);
		}
		return true;
	}
	public static boolean transact(SingleItem in, RecipeOutput out, Inventory from, Inventory to) {
		return transact(in.item(), in.amount(), out, from, to);
	}
	public static boolean transact(ItemEntry entry, int inAmount, RecipeOutput out, Inventory from, Inventory to) {
		Objects.requireNonNull(from, "from is null");
		if(from.get(entry).amount() < inAmount) return false;
		//Calculate capacity
		double volume = out.outVolume();
		double remainCapacity = to.capacity() - to.volume();
		if(volume > remainCapacity) {
			debug.printl("Required "+volume+" capacity, got "+remainCapacity);
			return false;
		} //Not enough space in the output
		//Withdraw from input
		from.extract(entry, inAmount);
		out.produceResults(to.createWriter());
		return true;
	}
	/**
	 * Crafts items according to a recipe
	 * @param input items to be consumed
	 * @param rout items to be crafted
	 * @param tgt target inventory
	 * @param src source inventory
	 * @param amount number of recipes to craft
	 * @return number of recipes crafted
	 */
	public static int transact(RecipeOutput input, RecipeOutput rout, Inventory tgt, Inventory src, int amount) {
		if(!tgt.exists()) return 0;
		if(!tgt.canInsert()) return 0;
		if(!src.exists()) return 0;
		if(!src.canExtract()) return 0;
		double volume = rout.outVolume(); //Per unit
		int fitsInOut = (int) (tgt.iremainVolume()/volume);
		int ingrsInIn = Inventory.howManyTimesThisContainsThat(tgt, input);
		int craftable = Math.min(fitsInOut, ingrsInIn);
		//Remove ingredients from input
		for(Entry<ItemEntry> irecord: input.getContents().object2IntEntrySet()) {
			int amt = irecord.getIntValue()*craftable;
			src.extract(irecord.getKey(), amt);
		}
		//Insert output
		rout.produceResults(tgt.createWriter(), craftable);
		return craftable;
	}
	
	//Recipes
	/** The list of all furnace fuels */
	@Nonnull public static final Object2DoubleMap<ItemEntry> furnaceFuels = new Object2DoubleOpenHashMap<>();
	/** The list of all nuclear reactor fuels */
	@Nonnull public static final Object2DoubleMap<ItemEntry> nukeFuels = new Object2DoubleOpenHashMap<>();
	/** Furnace recipes */
	@Nonnull public static final SingleRecipeGroup smelting = new SingleRecipeGroup("electrofurnace");
	/** Cluster mill recipes*/
 	@Nonnull public static final SingleRecipeGroup clusterMill = new SingleRecipeGroup("clustermill");
 	/** Crusher recipes */
	@Nonnull public static final SingleRecipeGroup crusher = new SingleRecipeGroup("crusher");
	/** Wiremill recipes */
	@Nonnull public static final SingleRecipeGroup wiremill = new SingleRecipeGroup("wiremill");
	/** Splitter recipes */
	@Nonnull public static final SingleRecipeGroup splitter = new SingleRecipeGroup("spllitter");
	/** Splicer recipes */
	@Nonnull public static final StackedRecipeGroup combiner = new StackedRecipeGroup("splicer");
	/** Alloy smelter recipes */
	@Nonnull public static final ComplexRecipeGroup alloyer = new ComplexRecipeGroup("alloyer", 2);
	/** Machine assembler recipes */
	@Nonnull public static final ComplexCatalyzedRecipeGroup assembler = new ComplexCatalyzedRecipeGroup("assembler", 2);
	/** Brewery recipes */
	@Nonnull public static final ComplexRecipeGroup brewery = new ComplexRecipeGroup("brewery", 2);
	/** Crafting recipes */
	@Nonnull public static final CraftingRecipeGroup crafting = new CraftingRecipeGroup("crafter");
	/** Quarry recipes */
	@Nonnull public static final SingleRecipeGroup quarry = new SingleRecipeGroup("quarry");
	/** Extruder recipes */
	@Nonnull public static final CatalyzedSingleRecipeGroup extruder = new CatalyzedSingleRecipeGroup("extruder");
	/** Crop outputs */
	@Nonnull public static final AgroRecipeGroup agro = new AgroRecipeGroup("agrorecipes");
	/** Alcoholic beverages */
	@Nonnull public static final AlcoholInfoGroup alcohol = new AlcoholInfoGroup("alcohol");
	//TODO replace with raw items
	/** Pickaxe recipes */
	@Nonnull public static final PickaxeGroup pickaxes = new PickaxeGroup("pickaxe");
	/** Sintered Carbides Factory recipes */
	@Nonnull public static final ComplexRecipeGroup sinterer = new ComplexRecipeGroup("sinterer", 2);
	/** Inscriber recipes */
	@Nonnull public static final CatalyzedSingleRecipeGroup inscriber = new CatalyzedSingleRecipeGroup("inscriber");
}
