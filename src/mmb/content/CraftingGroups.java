/**
 * 
 */
package mmb.content;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import mmb.NN;
import mmb.content.electric.recipes.CatSingleRecipeGroup;
import mmb.content.electric.recipes.ComplexCatRecipeGroup;
import mmb.content.electric.recipes.ComplexRecipeGroup;
import mmb.content.electric.recipes.SingleRecipeGroup;
import mmb.content.electric.recipes.StackedRecipeGroup;
import mmb.engine.craft.CraftingRecipeGroup;
import mmb.engine.craft.Craftings;
import mmb.engine.inv.Inventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemRaw;

/**
 * @author oskar
 * Contains crafting utilities and recipe groups
 */
public class CraftingGroups {
	/**
	 * Activates an item
	 * (replaces the selected raw item with a new item entity,
	 * whose type is the same as base type for the raw item)
	 * @param item item to activate
	 * @param from source inventory
	 * @param to target inventory
	 * @return was the item activated?
	 */
	public static boolean activateItem(ItemRaw item, Inventory from, Inventory to) {
		ItemEntry ient = item.iet.create();
		return Craftings.transact(item, ient, from, to);
	}
	
	//Recipes
	/** The list of all furnace fuels */
	@NN public static final Object2DoubleMap<ItemEntry> furnaceFuels = new Object2DoubleOpenHashMap<>();
	/** The list of all nuclear reactor fuels */
	@NN public static final Object2DoubleMap<ItemEntry> nukeFuels = new Object2DoubleOpenHashMap<>();
	/** Furnace recipes */
	@NN public static final SingleRecipeGroup smelting = new SingleRecipeGroup("electrofurnace");
	/** Cluster mill recipes*/
 	@NN public static final SingleRecipeGroup clusterMill = new SingleRecipeGroup("clustermill");
 	/** Crusher recipes */
	@NN public static final SingleRecipeGroup crusher = new SingleRecipeGroup("crusher");
	/** Wiremill recipes */
	@NN public static final SingleRecipeGroup wiremill = new SingleRecipeGroup("wiremill");
	/** Splitter recipes */
	@NN public static final SingleRecipeGroup splitter = new SingleRecipeGroup("spllitter");
	/** Splicer recipes */
	@NN public static final StackedRecipeGroup combiner = new StackedRecipeGroup("splicer");
	/** Alloy smelter recipes */
	@NN public static final ComplexRecipeGroup alloyer = new ComplexRecipeGroup("alloyer", 2);
	/** Machine assembler recipes */
	@NN public static final ComplexCatRecipeGroup assembler = new ComplexCatRecipeGroup("assembler", 2);
	/** Brewery recipes */
	@NN public static final ComplexRecipeGroup brewery = new ComplexRecipeGroup("brewery", 2);
	/** Crafting recipes */
	@NN public static final CraftingRecipeGroup crafting = new CraftingRecipeGroup("crafter");
	/** Quarry recipes */
	@NN public static final SingleRecipeGroup quarry = new SingleRecipeGroup("quarry");
	/** Extruder recipes */
	@NN public static final CatSingleRecipeGroup extruder = new CatSingleRecipeGroup("extruder");
	/** Sintered Carbides Factory recipes */
	@NN public static final ComplexRecipeGroup sinterer = new ComplexRecipeGroup("sinterer", 2);
	/** Inscriber recipes */
	@NN public static final CatSingleRecipeGroup inscriber = new CatSingleRecipeGroup("inscriber");
}
