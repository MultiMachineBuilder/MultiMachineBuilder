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
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.swing.JComponent;

import io.vavr.Tuple2;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.crafting.Recipe;
import mmb.WORLD.crafting.RecipeGroup;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.gui.craft.ComplexProcessingRecipeList;
import mmb.WORLD.gui.craft.ComplexRecipeView;
import mmb.WORLD.gui.craft.SimpleRecipeView;
import mmb.WORLD.gui.craft.StackedProcessingRecipeList;
import mmb.WORLD.gui.window.TabRecipes;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.items.ItemEntry;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 *
 */
public class ComplexProcessingRecipeGroup extends AbstractRecipeGroup{
	/**
	 * The minimum amount of ingredients
	 */
	public final int minIngredients;
	/**
	 * @param title the title of this recipe group
	 * @param minIngredients minimum amount of ingredients, must be >= 1
	 */
	public ComplexProcessingRecipeGroup(String id, int minIngredients) {
		super(id);
		if(minIngredients < 1) throw new IllegalArgumentException("The minimum ingredient count must be >=1, got "+minIngredients);
		this.minIngredients = minIngredients;
	}
	@Override
	protected Tuple2<String, JComponent> createTab() {
		return new Tuple2<String, JComponent>(title, new ComplexProcessingRecipeList(this));
	}
	
	/**
	 * @author oskar
	 * A recipe with one input item and output
	 */
	public class ComplexProcessingRecipe implements Identifiable<Set<ItemEntry>>, Recipe<ComplexProcessingRecipe>{
		public final double energy;
		@Nonnull public final VoltageTier voltage;
		@Nonnull public final RecipeOutput input;
		@Nonnull public final RecipeOutput output;
		@Nonnull public final ComplexProcessingRecipeGroup group;
		@Nonnull private final Set<ItemEntry> id;
		public ComplexProcessingRecipe(double energy, VoltageTier voltage, RecipeOutput in, RecipeOutput output) {
			super();
			this.energy = energy;
			this.voltage = voltage;
			this.input = in;
			this.output = output;
			this.id = Collections.unmodifiableSet(new HashSet<>(input.getContents().keySet()));
			group = ComplexProcessingRecipeGroup.this;
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
		public ComplexProcessingRecipe that() {
			return this;
		}
		@Override
		public Component createComponent() {
			ComplexRecipeView component = new ComplexRecipeView();
			ItemStack[] in = SimpleRecipeView.list2arr(input);
			ItemStack[] out = SimpleRecipeView.list2arr(output);
			component.set(this, out, in);
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
	
	private final SelfSet<Set<ItemEntry>, ComplexProcessingRecipe> _recipes = new HashSelfSet<>();
	public final SelfSet<Set<ItemEntry>, ComplexProcessingRecipe> recipes = Collects.unmodifiableSelfSet(_recipes);
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
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 */
	public ComplexProcessingRecipe add(RecipeOutput in, RecipeOutput out, VoltageTier voltage, double energy) {
		if(in.getContents().size() < minIngredients) throw new IllegalArgumentException("The recipe must have at least "+minIngredients+" inputs");
		if(in.getContents().size() == 0) throw new IllegalArgumentException("The recipe must have at least 1 input");
		ComplexProcessingRecipe recipe = new ComplexProcessingRecipe(energy, voltage, in, out);
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
	 */
	public ComplexProcessingRecipe add(RecipeOutput in, ItemEntry out, int amount, VoltageTier voltage, double energy) {
		return add(in, out.stack(amount), voltage, energy);
	}
	
	@Override
	public SelfSet<Set<ItemEntry>, ComplexProcessingRecipe> recipes() {
		return recipes;
	}
}
