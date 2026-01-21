package mmb.engine.recipe3;

import java.util.*;
import it.unimi.dsi.fastutil.ints.*;
import mmb.engine.settings.GlobalSettings;

/**
 * A performant recipe group with flattened recipes
 */
public class FlattenedRecipeGroup {
	/** Are the recipes in this group crafting? */
	public final boolean isCrafting;
	/** Unique group identifier */
	public final String id;
	/** User-readable name */
	public final String name;
	/**
	 * Creates a new recipe group
	 * @param isCrafting is the group crafting?
	 * @param id unique group ID
	 * @param name visible name of the group
	 */
    public FlattenedRecipeGroup(boolean isCrafting, String id, String name) {
		super();
		this.isCrafting = isCrafting;
		this.id = id;
		this.name = GlobalSettings.$res1(name);
	}

	private final List<Recipe> internalRecipes = new ArrayList<>();
    /** Collection of all recipes */
    public final List<Recipe> recipes = Collections.unmodifiableList(internalRecipes);

    /** groupOrdinal -> recipes containing that group */
    private final Int2ObjectMap<Set<Recipe>> index = new Int2ObjectOpenHashMap<>();

    /** Adds a recipe to the group and indexes it by each input slot's groupOrdinal */
    public Recipe addRecipe(RecipeSpec recipespec) {
    	Recipe recipe = new Recipe(this, recipespec);
        internalRecipes.add(recipe);
        for (FlattenedSlot slot : recipe.flattened.slots()) {
            index.computeIfAbsent(slot.groupOrdinal(), k -> new HashSet<>()).add(recipe);
        }
        return recipe;
    }

    /** Returns all recipes that could plausibly match any given item/fluid groupOrdinal */
    public Set<Recipe> findPlausible(int groupOrdinal) {
        return index.getOrDefault(groupOrdinal, Collections.emptySet());
    }

    public List<Recipe> allRecipes() {
        return internalRecipes;
    }
    
    //TODO LOOKUP functions
    
}
