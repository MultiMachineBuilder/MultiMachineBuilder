/**
 * 
 */
package mmb.WORLD.recipes;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import io.vavr.Tuple2;
import mmb.WORLD.chance.Chance;
import mmb.WORLD.crafting.GlobalRecipeRegistrar;
import mmb.WORLD.crafting.RecipeGroup;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.gui.craft.ComplexCatalyzedRecipeView;
import mmb.WORLD.gui.craft.RecipeView;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.items.ItemEntry;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 * A group of recipes with a complex input and a catalyst
 */
public class ComplexCatalyzedRecipeGroup extends
AbstractRecipeGroup<ComplexCatalyzedRecipeGroup.ComplexCatalyzedRecipe>{
	/**
	 * The minimum amount of ingredients
	 */
	public final int minIngredients;
	/**
	 * Creates a list of complex catalyzed recipes
	 * @param id the title of this recipe group
	 * @param minIngredients minimum amount of ingredients, must be >= 1
	 */
	public ComplexCatalyzedRecipeGroup(String id, int minIngredients) {
		super(id);
		if(minIngredients < 1) throw new IllegalArgumentException("The minimum ingredient count must be >=1, got "+minIngredients);
		this.minIngredients = minIngredients;
	}
	
	/**
	 * @author oskar
	 * A recipe with a complex input and a catalyst
	 */
	public class ComplexCatalyzedRecipe extends BaseElectricRecipe<ComplexCatalyzedRecipe> implements Identifiable<Tuple2<Set<ItemEntry>, @Nullable ItemEntry>>{
		/** The input item list */
		@Nonnull public final RecipeOutput input;
		/** Required catalyst */
		@Nullable public final ItemEntry catalyst;
		/** Item tuple */
		@Nonnull private final Tuple2<Set<ItemEntry>, @Nullable ItemEntry> id;
		
		/**
		 * Creates a complex catalyzed recipe
		 * @param energy energy required for completion in joules
		 * @param voltage voltage tier required for this recipe
		 * @param in the input item list
		 * @param output deterministic output of this recipe
		 * @param catalyst required catalyst
		 * @param luck randomized output of this recipe
		 */
		public ComplexCatalyzedRecipe(double energy, VoltageTier voltage, RecipeOutput in, RecipeOutput output, @Nullable ItemEntry catalyst, Chance luck) {
			super(energy, voltage, output, luck);
			this.input = in;
			this.id = new Tuple2<>(input.getContents().keySet(), catalyst);
			this.catalyst = catalyst;
		}
		
		@Override
		public int maxCraftable(Inventory src, int amount) {
			return Inventory.howManyTimesThisContainsThat(src, input);
		}
		@Override
		public int craft(Inventory src, Inventory tgt, int amount) {
			return CraftingGroups.transact(input, output, tgt, src, amount);
		}
		
		@Override
		public Tuple2<Set<ItemEntry>, @Nullable ItemEntry> id() {
			return id;
		}
		@Override
		public RecipeOutput inputs() {
			return input;
		}
		@Override
		public ItemEntry catalyst() {
			return catalyst;
		}
		@Override
		public RecipeGroup<ComplexCatalyzedRecipe> group() {
			return ComplexCatalyzedRecipeGroup.this;
		}
		@Override
		public ComplexCatalyzedRecipe that() {
			return this;
		}
	}
	
	//Recipe listing
	@Nonnull private final SelfSet<Tuple2<Set<ItemEntry>, @Nullable ItemEntry>, ComplexCatalyzedRecipe> _recipes = new HashSelfSet<>();
	public final SelfSet<Tuple2<Set<ItemEntry>, @Nullable ItemEntry>, ComplexCatalyzedRecipe> recipes = Collects.unmodifiableSelfSet(_recipes);
	@Override
	public Set<? extends ItemEntry> supportedItems() {
		return supported0;
	}
	private final Set<ItemEntry> supported = new HashSet<>();
	private final Set<ItemEntry> supported0 = Collections.unmodifiableSet(supported);
	@Override
	public SelfSet<Tuple2<Set<ItemEntry>, @Nullable ItemEntry>, ComplexCatalyzedRecipe> recipes() {
		return recipes;
	}
	
	//Recipe addition
	/**
	 * Adds a recipe to this recipe group
	 * @param in input item
	 * @param out output
	 * @param catalyst catalyst required to run the recipe
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @param luck random chanced items
	 * @return a new recipe
	 */
	public ComplexCatalyzedRecipe add(RecipeOutput in, RecipeOutput out, @Nullable ItemEntry catalyst, VoltageTier voltage, double energy, Chance luck) {
		if(in.getContents().size() < minIngredients) throw new IllegalArgumentException("The recipe must have at least "+minIngredients+" inputs");
		if(in.getContents().size() == 0) throw new IllegalArgumentException("The recipe must have at least 1 input");
		ComplexCatalyzedRecipe recipe = new ComplexCatalyzedRecipe(energy, voltage, in, out, catalyst, luck);
		_recipes.add(recipe);
		supported.addAll(in.getContents().keySet());
		GlobalRecipeRegistrar.addRecipe(recipe);
		return recipe;
	}
	/**
	 * Adds a recipe to this recipe group
	 * @param in input item
	 * @param out output item
	 * @param catalyst catalyst required to run the recipe
	 * @param amount amount of output item
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @param luck random chanced items
	 * @return a new recipe
	 */
	public ComplexCatalyzedRecipe add(RecipeOutput in, ItemEntry out, @Nullable ItemEntry catalyst, int amount, VoltageTier voltage, double energy, Chance luck) {
		return add(in, out.stack(amount), catalyst, voltage, energy, luck);
	}
	/**
	 * Adds a recipe to this recipe group
	 * @param in input item
	 * @param out output
	 * @param catalyst catalyst required to run the recipe
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @return a new recipe
	 */
	public ComplexCatalyzedRecipe add(RecipeOutput in, RecipeOutput out, @Nullable ItemEntry catalyst, VoltageTier voltage, double energy) {
		return add(in, out, catalyst, voltage, energy, Chance.NONE);
	}
	/**
	 * Adds a recipe to this recipe group
	 * @param in input item
	 * @param out output item
	 * @param catalyst catalyst required to run the recipe
	 * @param amount amount of output item
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @return a new recipe
	 */
	public ComplexCatalyzedRecipe add(RecipeOutput in, ItemEntry out, @Nullable ItemEntry catalyst, int amount, VoltageTier voltage, double energy) {
		return add(in, out.stack(amount), catalyst, voltage, energy, Chance.NONE);
	}
	
	//Others
	@Override
	public RecipeView<ComplexCatalyzedRecipe> createView() {
		return new ComplexCatalyzedRecipeView();
	}
	@Override
	public boolean isCatalyzed() {
		return true;
	}
}
