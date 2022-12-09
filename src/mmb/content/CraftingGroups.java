/**
 * 
 */
package mmb.content;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import mmb.content.agro.AgroRecipeGroup;
import mmb.content.drugs.AlcoholInfoGroup;
import mmb.engine.craft.Craftings;
import mmb.engine.craft.rgroups.CatalyzedSingleRecipeGroup;
import mmb.engine.craft.rgroups.ComplexCatalyzedRecipeGroup;
import mmb.engine.craft.rgroups.ComplexRecipeGroup;
import mmb.engine.craft.rgroups.CraftingRecipeGroup;
import mmb.engine.craft.rgroups.SingleRecipeGroup;
import mmb.engine.craft.rgroups.StackedRecipeGroup;
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
	/** Sintered Carbides Factory recipes */
	@Nonnull public static final ComplexRecipeGroup sinterer = new ComplexRecipeGroup("sinterer", 2);
	/** Inscriber recipes */
	@Nonnull public static final CatalyzedSingleRecipeGroup inscriber = new CatalyzedSingleRecipeGroup("inscriber");
}
