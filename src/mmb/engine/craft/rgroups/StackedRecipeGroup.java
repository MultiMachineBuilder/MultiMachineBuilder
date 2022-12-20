/**
 * 
 */
package mmb.engine.craft.rgroups;

import java.util.Set;

import mmb.NN;
import mmb.Nil;
import mmb.content.CraftingGroups;
import mmb.content.agro.AgroRecipeGroup.AgroProcessingRecipe;
import mmb.content.electric.VoltageTier;
import mmb.engine.chance.Chance;
import mmb.engine.craft.GlobalRecipeRegistrar;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.craft.SingleItem;
import mmb.engine.craft.singles.SimpleRecipe;
import mmb.engine.craft.singles.SimpleRecipeGroup;
import mmb.engine.inv.Inventory;
import mmb.engine.item.ItemEntry;
import mmbbase.menu.world.craft.StackedRecipeView;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * A list of recipes with a stacked input
 * @author oskar
 */
public class StackedRecipeGroup extends AbstractRecipeGroup<StackedRecipeGroup.StackedRecipe>
implements SimpleRecipeGroup<StackedRecipeGroup.StackedRecipe>{
	/**
	 * Creates a list of stacked recipes
	 * @param id group ID
	 */
	public StackedRecipeGroup(String id) {
		super(id);
	}
	/**
	 * A recipe with a stacked input
	 * @author oskar
	 */
	public class StackedRecipe extends BaseElectricRecipe<StackedRecipe> implements Identifiable<ItemEntry>, SimpleRecipe<StackedRecipe>{
		/** The input item stack */
		@NN public final SingleItem input;
		/**
		 * Creates a complex recipe
		 * @param energy energy required for completion in joules
		 * @param voltage voltage tier required for this recipe
		 * @param input the input item stack
		 * @param output deterministic output of this recipe
		 * @param luck randomized output of this recipe
		 */
		public StackedRecipe(double energy, VoltageTier voltage, SingleItem input, RecipeOutput output, Chance luck) {
			super(energy, voltage, output, luck);
			this.input = input;
		}
		@Override
		public ItemEntry id() {
			return input.item();
		}
		@Override
		public int maxCraftable(Inventory src, int amount) {
			return Inventory.howManyTimesThisContainsThat(src, input);
		}
		@Override
		public SingleItem inputs() {
			return input;
		}
		@Override
		public ItemEntry catalyst() {
			return null;
		}
		@Override
		public StackedRecipeGroup group() {
			return StackedRecipeGroup.this;
		}
		@Override
		public StackedRecipe that() {
			return this;
		}
		/** @return amount of input items */
		public int amount() {
			return input.amount();
		}
		/** @return input item entry */
		public ItemEntry item() {
			return input.item();
		}
	}
	
	//Recipe listing
	@NN private final SelfSet<ItemEntry, StackedRecipe> _recipes = HashSelfSet.createNonnull(StackedRecipe.class);
	@NN public final SelfSet<ItemEntry, StackedRecipe> recipes = Collects.unmodifiableSelfSet(_recipes);
	@Override
	public Set<? extends ItemEntry> supportedItems() {
		return recipes.keys();
	}
	@Override
	public SelfSet<ItemEntry, StackedRecipe> recipes() {
		return recipes;
	}
	@Override
	public StackedRecipe findRecipe(@Nil ItemEntry catalyst, ItemEntry in) {
		return recipes.get(in);
	}
	
	//Recipe addition
	/**
	 * Adds a recipe to this recipe group
	 * @param in input item
	 * @param out output
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @param luck random chanced items
	 * @return new recipe
	 */
	public StackedRecipe add(SingleItem in, RecipeOutput out, VoltageTier voltage, double energy, Chance luck) {
		StackedRecipe recipe = new StackedRecipe(energy, voltage, in, out, luck);
		_recipes.add(recipe);
		GlobalRecipeRegistrar.addRecipe(recipe);
		return recipe;
	}
	/**
	 * Adds a recipe to this recipe group
	 * @param in input item
	 * @param out output item
	 * @param amounto amount of output item
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @param luck random chanced items
	 * @return new recipe
	 */
	public StackedRecipe add(SingleItem in, ItemEntry out, int amounto, VoltageTier voltage, double energy, Chance luck) {
		return add(in, out.stack(amounto), voltage, energy, luck);
	}
	/**
	 * Adds a recipe to this recipe group
	 * @param in input item
	 * @param out output
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @return new recipe
	 */
	public StackedRecipe add(SingleItem in, RecipeOutput out, VoltageTier voltage, double energy) {
		return add(in, out, voltage, energy, Chance.NONE);
	}
	/**
	 * Adds a recipe to this recipe group
	 * @param in input item
	 * @param out output item
	 * @param amounto amount of output item
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @return new recipe
	 */
	public StackedRecipe add(SingleItem in, ItemEntry out, int amounto, VoltageTier voltage, double energy) {
		return add(in, out.stack(amounto), voltage, energy, Chance.NONE);
	}
	
	//Others
	@Override public StackedRecipeView createView() {
		return new StackedRecipeView();
	}
	@Override
	public boolean isCatalyzed() {
		return false;
	}

}
