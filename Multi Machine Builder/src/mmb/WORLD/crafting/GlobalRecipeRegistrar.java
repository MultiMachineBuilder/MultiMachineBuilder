/**
 * 
 */
package mmb.WORLD.crafting;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 * An utility class that keeps track of all known recipes.
 */
public class GlobalRecipeRegistrar {
	private GlobalRecipeRegistrar() {}
	/** A full set of recipes */
	private static final Set<Recipe<?>> recipes0 = new HashSet<>();
	public static final Set<Recipe<?>> recipes = Collections.unmodifiableSet(recipes0);
	
	/** A full set of recipe groups */
	private static final Set<RecipeGroup<?>> rgroups0 = new HashSet<>();
	public static final Set<RecipeGroup<?>> rgroups = Collections.unmodifiableSet(rgroups0);
	/**
	 * Adds a recipe. Should be used by recipe groups
	 * @param recipe
	 */
	public static void addRecipe(Recipe<?> recipe) {
		recipes0.add(recipe);
		for(ItemEntry ent: recipe.inputs().items()) 
			byInputs0.put(ent, recipe);
		for(ItemEntry ent: recipe.output().items()) 
			byOutputs0.put(ent, recipe);
		byCatalyst0.put(recipe.catalyst(), recipe);
		for(ItemEntry ent: recipe.luck().items()) 
			byChance0.put(ent, recipe);
		VoltageTier voltage = recipe.voltTier();
		byVoltage0.put(voltage, recipe);
		for(int i = 0; i <= voltage.ordinal(); i++) {
			VoltageTier higher = VoltageTier.VOLTS.get(8 - i);
			uptoVoltage0.put(higher, recipe);
		}
		
	}
	/**
	 * Adds a recipe group. Should be used by recipe groups
	 * @param recipe
	 */
	public static void addRecipeGroup(RecipeGroup<?> recipe) {
		rgroups0.add(recipe);
	}
		
	//Indexing
	@Nonnull private static final SetMultimap<          ItemEntry, Recipe<?>> byInputs0    = HashMultimap.create();
	@Nonnull private static final SetMultimap<          ItemEntry, Recipe<?>> byOutputs0   = HashMultimap.create();
	@Nonnull private static final SetMultimap<@Nullable ItemEntry, Recipe<?>> byCatalyst0  = HashMultimap.create();
	@Nonnull private static final SetMultimap<          ItemEntry, Recipe<?>> byChance0    = HashMultimap.create();
	@Nonnull private static final SetMultimap<        VoltageTier, Recipe<?>> byVoltage0   = HashMultimap.create();
	@Nonnull private static final SetMultimap<        VoltageTier, Recipe<?>> uptoVoltage0 = HashMultimap.create();
	
	/** Recipes grouped by input items */
	@Nonnull public static final SetMultimap<          ItemEntry, Recipe<?>> byInputs    = Multimaps.unmodifiableSetMultimap(byInputs0);
	/** Recipes grouped by output items */
	@Nonnull public static final SetMultimap<          ItemEntry, Recipe<?>> byOutputs   = Multimaps.unmodifiableSetMultimap(byOutputs0);
	/** Recipes grouped by the catalyst */
	@Nonnull public static final SetMultimap<@Nullable ItemEntry, Recipe<?>> byCatalyst  = Multimaps.unmodifiableSetMultimap(byCatalyst0);
	/** Recipes grouped by randomized outputs */
	@Nonnull public static final SetMultimap<          ItemEntry, Recipe<?>> byChance    = Multimaps.unmodifiableSetMultimap(byChance0);
	/** Recipes grouped by voltage tiers, exact*/
	@Nonnull public static final SetMultimap<        VoltageTier, Recipe<?>> byVoltage   = Multimaps.unmodifiableSetMultimap(byVoltage0);
	/** Recipes grouped by voltage tiers which are equal or higher*/
	@Nonnull public static final SetMultimap<        VoltageTier, Recipe<?>> uptoVoltage = Multimaps.unmodifiableSetMultimap(uptoVoltage0);
}