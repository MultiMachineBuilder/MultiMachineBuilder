/**
 * 
 */
package mmb.WORLD.crafting.recipes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.vavr.Tuple2;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.crafting.Recipe;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.gui.craft.CatalyzedSimpleRecipeView;
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
public class CatalyzedSimpleProcessingRecipeGroup extends AbstractRecipeGroup<CatalyzedSimpleProcessingRecipeGroup.CatalyzedSimpleProcessingRecipe>{
	public CatalyzedSimpleProcessingRecipeGroup(String id) {
		super(id);
	}
	
	/**
	 * @author oskar
	 * A recipe with one input item and output
	 */
	public class CatalyzedSimpleProcessingRecipe implements Identifiable<Tuple2<ItemEntry, ItemEntry>>, Recipe<CatalyzedSimpleProcessingRecipe>{
		public final double energy;
		@Nonnull public final VoltageTier voltage;
		@Nonnull public final ItemEntry input;
		@Nullable public final ItemEntry catalyst;
		@Nonnull public final Tuple2<ItemEntry, ItemEntry> tuple;
		@Nonnull public final RecipeOutput output;
		@Nonnull public final CatalyzedSimpleProcessingRecipeGroup group;
		public CatalyzedSimpleProcessingRecipe(double energy, VoltageTier voltage, ItemEntry input, @Nullable ItemEntry catalyst, RecipeOutput output) {
			super();
			this.energy = energy;
			this.voltage = voltage;
			this.input = input;
			this.output = output;
			this.catalyst = catalyst;
			this.tuple = new Tuple2<>(input, catalyst);
			group = CatalyzedSimpleProcessingRecipeGroup.this;
		}
		@Override
		public Tuple2<ItemEntry, ItemEntry> id() {
			return tuple;
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
			return catalyst;
		}
		@Override
		public CatalyzedSimpleProcessingRecipeGroup group() {
			return group;
		}
		@Override
		public CatalyzedSimpleProcessingRecipe that() {
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
	@Nonnull private final SelfSet<Tuple2<ItemEntry, ItemEntry>, CatalyzedSimpleProcessingRecipe> _recipes = new HashSelfSet<>();
	@Nonnull public final SelfSet<Tuple2<ItemEntry, ItemEntry>, CatalyzedSimpleProcessingRecipe> recipes = Collects.unmodifiableSelfSet(_recipes);
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
	 * @param catalyst catalyst required to run the recipe
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @return a new recipe
	 */
	public CatalyzedSimpleProcessingRecipe add(ItemEntry in, RecipeOutput out, @Nullable ItemEntry catalyst, VoltageTier voltage, double energy) {
		CatalyzedSimpleProcessingRecipe recipe = new CatalyzedSimpleProcessingRecipe(energy, voltage, in, catalyst, out);
		_recipes.add(recipe);
		GlobalRecipeRegistrar.addRecipe(recipe);
		supported.add(in);
		return recipe;
	}
	/**
	 * @param in input item
	 * @param out output item
	 * @param catalyst catalyst required to run the recipe
	 * @param amount amount of output item
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @return a new recipe
	 */
	public CatalyzedSimpleProcessingRecipe add(ItemEntry in, ItemEntry out, @Nullable ItemEntry catalyst, int amount, VoltageTier voltage, double energy) {
		return add(in, out.stack(amount), catalyst, voltage, energy);
	}
	@Override
	public SelfSet<Tuple2<ItemEntry, ItemEntry>, CatalyzedSimpleProcessingRecipe> recipes() {
		return recipes;
	}
	@Override
	public CatalyzedSimpleRecipeView createView() {
		return new CatalyzedSimpleRecipeView();
	}
}
