/**
 * 
 */
package mmb.WORLD.crafting.recipes;

import java.awt.Component;
import java.util.Collections;
import java.util.HashSet;
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
import mmb.WORLD.gui.craft.SimpleProcessingRecipeList;
import mmb.WORLD.gui.craft.SimpleRecipeView;
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
public class ElectroSimpleProcessingRecipeGroup implements RecipeGroup{
	public final String title;
	public ElectroSimpleProcessingRecipeGroup(String title) {
		super();
		this.title = title;
		Supplier<Tuple2<String, JComponent>> sup = () -> new Tuple2<String, JComponent>(title, new SimpleProcessingRecipeList(this));
		TabRecipes.add(sup);
		GlobalRecipeRegistrar.addRecipeGroup(this);
	}
	/**
	 * @author oskar
	 * A recipe with one input item and output
	 */
	public class ElectroSimpleProcessingRecipe implements Identifiable<ItemEntry>, Recipe<ElectroSimpleProcessingRecipe>{
		public final double energy;
		@Nonnull public final VoltageTier voltage;
		@Nonnull public final ItemEntry input;
		@Nonnull public final RecipeOutput output;
		@Nonnull public final ElectroSimpleProcessingRecipeGroup group;
		public ElectroSimpleProcessingRecipe(double energy, VoltageTier voltage, ItemEntry input, RecipeOutput output) {
			super();
			this.energy = energy;
			this.voltage = voltage;
			this.input = input;
			this.output = output;
			group = ElectroSimpleProcessingRecipeGroup.this;
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
		public ElectroSimpleProcessingRecipe that() {
			return this;
		}
		@Override
		public Component createComponent() {
			SimpleRecipeView component = new SimpleRecipeView();
			ItemStack[] out = SimpleRecipeView.list2arr(output);
			component.set(this, out);
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
	@Nonnull private final SelfSet<ItemEntry, ElectroSimpleProcessingRecipe> _recipes = new HashSelfSet<>();
	@Nonnull public final SelfSet<ItemEntry, ElectroSimpleProcessingRecipe> recipes = Collects.unmodifiableSelfSet(_recipes);
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
	 */
	public ElectroSimpleProcessingRecipe add(ItemEntry in, RecipeOutput out, VoltageTier voltage, double energy) {
		ElectroSimpleProcessingRecipe recipe = new ElectroSimpleProcessingRecipe(energy, voltage, in, out);
		_recipes.add(recipe);
		GlobalRecipeRegistrar.addRecipe(recipe);
		supported.add(in);
		return recipe;
	}
	/**
	 * @param in input item
	 * @param out output item
	 * @param amount amount of output item
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 */
	public ElectroSimpleProcessingRecipe add(ItemEntry in, ItemEntry out, int amount, VoltageTier voltage, double energy) {
		return add(in, out.stack(amount), voltage, energy);
	}
	@Override
	public SelfSet<ItemEntry, ElectroSimpleProcessingRecipe> recipes() {
		return recipes;
	}

}
