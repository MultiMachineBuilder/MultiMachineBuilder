/**
 * 
 */
package mmb.WORLD.crafting.recipes;

import java.awt.Component;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import io.vavr.Tuple2;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.crafting.Recipe;
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
 *
 */
public class ComplexCatalyzedProcessingRecipeGroup extends
AbstractRecipeGroup<ComplexCatalyzedProcessingRecipeGroup.ComplexCatalyzedProcessingRecipe>{
	/**
	 * The minimum amount of ingredients
	 */
	public final int minIngredients;
	/**
	 * @param id the title of this recipe group
	 * @param minIngredients minimum amount of ingredients, must be >= 1
	 */
	public ComplexCatalyzedProcessingRecipeGroup(String id, int minIngredients) {
		super(id);
		if(minIngredients < 1) throw new IllegalArgumentException("The minimum ingredient count must be >=1, got "+minIngredients);
		this.minIngredients = minIngredients;
	}
	
	/**
	 * @author oskar
	 * A recipe with one input item and output
	 */
	public class ComplexCatalyzedProcessingRecipe implements Identifiable<Tuple2<Set<ItemEntry>, @Nullable ItemEntry>>, Recipe<ComplexCatalyzedProcessingRecipe>{
		public final double energy;
		@Nonnull public final VoltageTier voltage;
		@Nonnull public final RecipeOutput input;
		@Nonnull public final RecipeOutput output;
		@Nonnull public final ComplexCatalyzedProcessingRecipeGroup group;
		@Nullable public final ItemEntry catalyst;
		@Nonnull private final Tuple2<Set<ItemEntry>, @Nullable ItemEntry> id;
		public ComplexCatalyzedProcessingRecipe(double energy, VoltageTier voltage, RecipeOutput in, RecipeOutput output, @Nullable ItemEntry catalyst) {
			super();
			this.energy = energy;
			this.voltage = voltage;
			this.input = in;
			this.output = output;
			this.id = new Tuple2<>(Collections.unmodifiableSet(new HashSet<>(input.getContents().keySet())), catalyst);
			this.catalyst = catalyst;
			group = ComplexCatalyzedProcessingRecipeGroup.this;
		}
		@Override
		public Tuple2<Set<ItemEntry>, @Nullable ItemEntry> id() {
			return id;
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
		public RecipeGroup<ComplexCatalyzedProcessingRecipe> group() {
			return group;
		}
		@Override
		public ComplexCatalyzedProcessingRecipe that() {
			return this;
		}
		@Override
		public Component createComponent() {
			ComplexCatalyzedRecipeView component = new ComplexCatalyzedRecipeView();
			component.set(this);
			return component;
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
	
	private final SelfSet<Tuple2<Set<ItemEntry>, @Nullable ItemEntry>, ComplexCatalyzedProcessingRecipe> _recipes = new HashSelfSet<>();
	public final SelfSet<Tuple2<Set<ItemEntry>, @Nullable ItemEntry>, ComplexCatalyzedProcessingRecipe> recipes = Collects.unmodifiableSelfSet(_recipes);
	@Override
	public Set<? extends ItemEntry> supportedItems() {
		return supported0;
	}
	private final Set<ItemEntry> supported = new HashSet<>();
	private final Set<ItemEntry> supported0 = Collections.unmodifiableSet(supported);
	@Nonnull private final Map<ItemEntry, VoltageTier> _minVolt4item = new HashMap<>();
	@Nonnull public final Map<ItemEntry, VoltageTier> minVolt4item = Collections.unmodifiableMap(_minVolt4item);
	private void updateMinVolt(VoltageTier volt, ItemEntry item) {
		_minVolt4item.compute(item, (item0, volt0) ->{
			if(volt0 == null || volt0.compareTo(volt) > 0) return volt;
			return volt0;
		});
	}
	
	/**
	 * Adds a recipes to this recipe group
	 * @param in input item
	 * @param out output
	 * @param catalyst catalyst required to run the recipe
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @return 
	 */
	public ComplexCatalyzedProcessingRecipe add(RecipeOutput in, RecipeOutput out, @Nullable ItemEntry catalyst, VoltageTier voltage, double energy) {
		if(in.getContents().size() < minIngredients) throw new IllegalArgumentException("The recipe must have at least "+minIngredients+" inputs");
		if(in.getContents().size() == 0) throw new IllegalArgumentException("The recipe must have at least 1 input");
		ComplexCatalyzedProcessingRecipe recipe = new ComplexCatalyzedProcessingRecipe(energy, voltage, in, out, catalyst);
		_recipes.add(recipe);
		supported.addAll(in.getContents().keySet());
		GlobalRecipeRegistrar.addRecipe(recipe);
		return recipe;
	}
	/**
	 * @param in input item
	 * @param out output item
	 * @param catalyst catalyst required to run the recipe
	 * @param amount amount of output item
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @return 
	 */
	public ComplexCatalyzedProcessingRecipe add(RecipeOutput in, ItemEntry out, @Nullable ItemEntry catalyst, int amount, VoltageTier voltage, double energy) {
		return add(in, out.stack(amount), catalyst, voltage, energy);
	}
	
	@Override
	public SelfSet<Tuple2<Set<ItemEntry>, @Nullable ItemEntry>, ComplexCatalyzedProcessingRecipe> recipes() {
		return recipes;
	}
	
	@Override
	public RecipeView<ComplexCatalyzedProcessingRecipe> createView() {
		return new ComplexCatalyzedRecipeView();
	}
}
