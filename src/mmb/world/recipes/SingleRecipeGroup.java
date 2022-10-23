/**
 * 
 */
package mmb.world.recipes;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.menu.world.craft.SingleRecipeView;
import mmb.world.chance.Chance;
import mmb.world.crafting.GlobalRecipeRegistrar;
import mmb.world.crafting.RecipeOutput;
import mmb.world.crafting.singles.SimpleRecipe;
import mmb.world.crafting.singles.SimpleRecipeGroup;
import mmb.world.electric.VoltageTier;
import mmb.world.inventory.Inventory;
import mmb.world.items.ItemEntry;
import mmb.world.recipes.AgroRecipeGroup.AgroProcessingRecipe;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 *
 */
public class SingleRecipeGroup extends AbstractRecipeGroup<SingleRecipeGroup.SingleRecipe>
implements SimpleRecipeGroup<SingleRecipeGroup.SingleRecipe>{
	/**
	 * Creates a list of single-item recipes
	 * @param id group ID
	 */
	public SingleRecipeGroup(String id) {
		super(id);
	}
	/**
	 * A recipe with a single input item
	 * @author oskar
	 */
	public class SingleRecipe extends BaseElectricRecipe<SingleRecipe> implements Identifiable<ItemEntry>, SimpleRecipe<SingleRecipe>{
		/** The input item */
		@Nonnull public final ItemEntry input;
		
		/**
		 * Creates a single recipe
		 * @param energy energy required for completion in joules
		 * @param voltage voltage tier required for this recipe
		 * @param input the input item
		 * @param output deterministic output of this recipe
		 * @param luck randomized output of this recipe
		 */
		public SingleRecipe(double energy, VoltageTier voltage, ItemEntry input, RecipeOutput output, Chance luck) {
			super(energy, voltage, output, luck);
			this.input = input;
		}
		@Override
		public ItemEntry id() {
			return input;
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
		public ItemEntry inputs() {
			return input;
		}
		@Override
		public ItemEntry catalyst() {
			return null;
		}
		@Override
		public SingleRecipeGroup group() {
			return SingleRecipeGroup.this;
		}
		@Override
		public SingleRecipe that() {
			return this;
		}
	}
	
	//Recipe listing
	@Nonnull private final SelfSet<ItemEntry, SingleRecipe> _recipes = HashSelfSet.createNonnull(SingleRecipe.class);
	@Nonnull public final SelfSet<ItemEntry, SingleRecipe> recipes = Collects.unmodifiableSelfSet(_recipes);
	@Override
	public Set<? extends ItemEntry> supportedItems() {
		return recipes.keys();
	}
	@Override
	public SelfSet<ItemEntry, SingleRecipe> recipes() {
		return recipes;
	}
	@Override
	public SingleRecipe findRecipe(@Nullable ItemEntry catalyst, ItemEntry in) {
		return recipes.get(in);
	}
	
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
	public SingleRecipe add(ItemEntry in, RecipeOutput out, VoltageTier voltage, double energy, Chance luck) {
		SingleRecipe recipe = new SingleRecipe(energy, voltage, in, out, luck);
		_recipes.add(recipe);
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
	public SingleRecipe add(ItemEntry in, ItemEntry out, int amount, VoltageTier voltage, double energy, Chance luck) {
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
	public SingleRecipe add(ItemEntry in, RecipeOutput out, VoltageTier voltage, double energy) {
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
	public SingleRecipe add(ItemEntry in, ItemEntry out, int amount, VoltageTier voltage, double energy) {
		return add(in, out.stack(amount), voltage, energy);
	}
	
	//Others
	@Override
	public SingleRecipeView createView() {
		return new SingleRecipeView();
	}
	@Override
	public boolean isCatalyzed() {
		return false;
	}
	

}
