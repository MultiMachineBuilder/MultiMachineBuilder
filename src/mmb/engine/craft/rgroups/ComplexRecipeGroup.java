/**
 * 
 */
package mmb.engine.craft.rgroups;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import mmb.NN;
import mmb.content.electric.VoltageTier;
import mmb.engine.chance.Chance;
import mmb.engine.craft.GlobalRecipeRegistrar;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.inv.Inventory;
import mmb.engine.item.ItemEntry;
import mmb.menu.world.craft.ComplexRecipeView;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 * A group of recipes with a complex input
 */
public class ComplexRecipeGroup extends AbstractRecipeGroup<ComplexRecipeGroup.ComplexRecipe>{
	/**
	 * The minimum amount of ingredients
	 */
	public final int minIngredients;
	/**
	 * Creates a list of complex recipes
	 * @param id group ID
	 * @param minIngredients minimum amount of ingredients, must be >= 1
	 */
	public ComplexRecipeGroup(String id, int minIngredients) {
		super(id);
		if(minIngredients < 1) throw new IllegalArgumentException("The minimum ingredient count must be >=1, got "+minIngredients);
		this.minIngredients = minIngredients;
	}
	
	/**
	 * A recipe with a complex input
	 * @author oskar
	 */
	public class ComplexRecipe extends BaseElectricRecipe<ComplexRecipe> implements Identifiable<Set<ItemEntry>>{
		/** The input item list */
		@NN public final RecipeOutput input;
		@NN private final Set<ItemEntry> id;
		
		/**
		 * Creates a complex recipe
		 * @param energy energy required for completion in joules
		 * @param voltage voltage tier required for this recipe
		 * @param in the input item list
		 * @param output deterministic output of this recipe
		 * @param luck randomized output of this recipe
		 */
		public ComplexRecipe(double energy, VoltageTier voltage, RecipeOutput in, RecipeOutput output, Chance luck) {
			super(energy, voltage, output, luck);
			this.input = in;
			this.id = input.getContents().keySet();
		}
		@Override
		public Set<ItemEntry> id() {
			return id;
		}
		@Override
		public int maxCraftable(Inventory src, int amount) {
			return Inventory.howManyTimesThisContainsThat(src, input);
		}

		@Override
		public RecipeOutput inputs() {
			return input;
		}
		@Override
		public ItemEntry catalyst() {
			return null;
		}
		@Override
		public ComplexRecipeGroup group() {
			return ComplexRecipeGroup.this;
		}
		@Override
		public ComplexRecipe that() {
			return this;
		}
	}
	
	//Recipe listing
	@NN private final SelfSet<Set<ItemEntry>, ComplexRecipe> _recipes = HashSelfSet.createNonnull(ComplexRecipe.class);
	public final SelfSet<Set<ItemEntry>, ComplexRecipe> recipes = Collects.unmodifiableSelfSet(_recipes);
	@Override
	public Set<? extends ItemEntry> supportedItems() {
		return supported0;
	}
	private final Set<ItemEntry> supported = new HashSet<>();
	private final Set<ItemEntry> supported0 = Collections.unmodifiableSet(supported);
	
	//Recipe addition	
	/**
	 * Adds a recipes to this recipe group
	 * @param in input item
	 * @param out output
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @param luck random chanced items
	 * @return new recipe
	 */
	public ComplexRecipe add(RecipeOutput in, RecipeOutput out, VoltageTier voltage, double energy, Chance luck) {
		if(in.getContents().size() < minIngredients) throw new IllegalArgumentException("The recipe must have at least "+minIngredients+" inputs");
		if(in.getContents().size() <= 0) throw new IllegalArgumentException("The recipe must have at least 1 input");
		ComplexRecipe recipe = new ComplexRecipe(energy, voltage, in, out, luck);
		_recipes.add(recipe);
		supported.addAll(in.getContents().keySet());
		GlobalRecipeRegistrar.addRecipe(recipe);
		return recipe;
	}
	/**
	 * @param in input item
	 * @param out output item
	 * @param amount amount of output item
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @param luck random chanced items
	 * @return new recipe
	 */
	public ComplexRecipe add(RecipeOutput in, ItemEntry out, int amount, VoltageTier voltage, double energy, Chance luck) {
		return add(in, out.stack(amount), voltage, energy, luck);
	}
	/**
	 * Adds a recipes to this recipe group
	 * @param in input item
	 * @param out output
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @return new recipe
	 */
	public ComplexRecipe add(RecipeOutput in, RecipeOutput out, VoltageTier voltage, double energy) {
		return add(in, out, voltage, energy, Chance.NONE);
	}
	/**
	 * @param in input item
	 * @param out output item
	 * @param amount amount of output item
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @return new recipe
	 */
	public ComplexRecipe add(RecipeOutput in, ItemEntry out, int amount, VoltageTier voltage, double energy) {
		return add(in, out.stack(amount), voltage, energy, Chance.NONE);
	}
	
	@Override
	public SelfSet<Set<ItemEntry>, ComplexRecipe> recipes() {
		return recipes;
	}

	//Others
	@Override
	public ComplexRecipeView createView() {
		return new ComplexRecipeView();
	}
	@Override
	public boolean isCatalyzed() {
		return false;
	}
}
