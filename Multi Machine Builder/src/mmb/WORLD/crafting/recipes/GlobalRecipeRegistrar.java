/**
 * 
 */
package mmb.WORLD.crafting.recipes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import mmb.WORLD.crafting.Recipe;
import mmb.WORLD.crafting.RecipeGroup;

/**
 * @author oskar
 * An utility class that keeps track of all known recipes.
 */
public class GlobalRecipeRegistrar {
	private GlobalRecipeRegistrar() {}
	private static final Set<Recipe<?>> recipes0 = new HashSet<>();
	public static final Set<Recipe<?>> recipes = Collections.unmodifiableSet(recipes0);
	private static final Set<RecipeGroup> rgroups0 = new HashSet<>();
	public static final Set<RecipeGroup> rgroups = Collections.unmodifiableSet(rgroups0);
	public static void addRecipe(Recipe<?> recipe) {
		recipes0.add(recipe);
	}
	public static void addRecipeGroup(RecipeGroup recipe) {
		rgroups0.add(recipe);
	}
}
