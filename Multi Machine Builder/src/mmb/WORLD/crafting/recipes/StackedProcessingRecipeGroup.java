/**
 * 
 */
package mmb.WORLD.crafting.recipes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.crafting.Recipe;
import mmb.WORLD.crafting.RecipeGroup;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.gui.craft.StackedRecipeView;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.items.ItemEntry;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 *
 */
public class StackedProcessingRecipeGroup extends AbstractRecipeGroup<StackedProcessingRecipeGroup.StackedProcessingRecipe>{
	public StackedProcessingRecipeGroup(String id) {
		super(id);
	}
	/**
	 * @author oskar
	 * A recipe with one input item and output
	 */
	public class StackedProcessingRecipe implements Identifiable<ItemEntry>, Recipe<StackedProcessingRecipe>{
		public final double energy;
		@Nonnull public final VoltageTier voltage;
		@Nonnull public final ItemEntry input;
		@Nonnull public final RecipeOutput output;
		@Nonnull public final StackedProcessingRecipeGroup group;
		public final int amount;
		public StackedProcessingRecipe(double energy, VoltageTier voltage, ItemEntry input, RecipeOutput output, int amount) {
			super();
			this.energy = energy;
			this.voltage = voltage;
			this.input = input;
			this.output = output;
			this.amount = amount;
			group = StackedProcessingRecipeGroup.this;
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
			return Craftings.transact(input, output, tgt, src, amount);
		}
		@Override
		public RecipeOutput output() {
			return output;
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
		public RecipeGroup group() {
			return group;
		}
		@Override
		public StackedProcessingRecipe that() {
			return this;
		}
		@Override
		public double energy() {
			return energy;
		}
		@Override
		public VoltageTier voltTier() {
			return voltage;
		}
	}
	@Nonnull private final SelfSet<ItemEntry, StackedProcessingRecipe> _recipes = new HashSelfSet<>();
	@Nonnull public final SelfSet<ItemEntry, StackedProcessingRecipe> recipes = Collects.unmodifiableSelfSet(_recipes);
	@Override
	public Set<? extends ItemEntry> supportedItems() {
		return supported0;
	}
	private final Set<ItemEntry> supported = new HashSet<>();
	private final Set<ItemEntry> supported0 = Collections.unmodifiableSet(supported);
	/**
	 * Adds a recipes to this recipe group
	 * @param in input item
	 * @param out output
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @return the recipe
	 */
	public StackedProcessingRecipe add(ItemEntry in, RecipeOutput out, VoltageTier voltage, double energy) {
		return add(in, 1, out, voltage, energy);
	}
	/**
	 * @param in input item
	 * @param out output item
	 * @param amount amount of output item
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @return the recipe
	 */
	public StackedProcessingRecipe add(ItemEntry in, ItemEntry out, int amount, VoltageTier voltage, double energy) {
		return add(in, 1, out.stack(amount), voltage, energy);
	}
	/**
	 * Adds a recipes to this recipe group
	 * @param in input item
	 * @param amount 
	 * @param out output
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @return the recipe
	 */
	public StackedProcessingRecipe add(ItemEntry in, int amount, RecipeOutput out, VoltageTier voltage, double energy) {
		StackedProcessingRecipe recipe = new StackedProcessingRecipe(energy, voltage, in, out, amount);
		_recipes.add(recipe);
		GlobalRecipeRegistrar.addRecipe(recipe);
		supported.add(in);
		return recipe;
	}
	/**
	 * @param in input item
	 * @param amounti amount of input item
	 * @param out output item
	 * @param amounto amount of output item
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @return the recipe
	 */
	public StackedProcessingRecipe add(ItemEntry in, int amounti, ItemEntry out, int amounto, VoltageTier voltage, double energy) {
		return add(in, amounti, out.stack(amounto), voltage, energy);
	}
	@Override
	public SelfSet<ItemEntry, StackedProcessingRecipe> recipes() {
		return recipes;
	}
	@Override
	public StackedRecipeView createView() {
		return new StackedRecipeView();
	}
	@Override
	public boolean isCatalyzed() {
		return false;
	}

}
