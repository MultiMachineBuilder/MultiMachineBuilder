/**
 * 
 */
package mmb.WORLD.crafting.recipes;

import java.awt.Component;
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
import mmb.WORLD.gui.craft.ComplexRecipeView;
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
public class SimpleProcessingRecipeGroup implements RecipeGroup{
	public final String title;
	public SimpleProcessingRecipeGroup(String title) {
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
	public class SimpleProcessingRecipe implements Identifiable<ItemEntry>, Recipe<SimpleProcessingRecipe>{
		public final double energy;
		@Nonnull public final VoltageTier voltage;
		@Nonnull public final ItemEntry input;
		@Nonnull public final RecipeOutput output;
		@Nonnull public final SimpleProcessingRecipeGroup group;
		public SimpleProcessingRecipe(double energy, VoltageTier voltage, ItemEntry input, RecipeOutput output) {
			super();
			this.energy = energy;
			this.voltage = voltage;
			this.input = input;
			this.output = output;
			group = SimpleProcessingRecipeGroup.this;
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
		public SimpleProcessingRecipe that() {
			return this;
		}
		@Override
		public Component createComponent() {
			SimpleRecipeView component = new SimpleRecipeView();
			ItemStack[] out = SimpleRecipeView.list2arr(output);
			component.set(this, out);
			return component;
		}
	}
	private final SelfSet<ItemEntry, SimpleProcessingRecipe> _recipes = new HashSelfSet<>();
	public final SelfSet<ItemEntry, SimpleProcessingRecipe> recipes = Collects.unmodifiableSelfSet(_recipes);
	/**
	 * Adds a recipes to this recipe group
	 * @param in input item
	 * @param out output
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 */
	public SimpleProcessingRecipe add(ItemEntry in, RecipeOutput out, VoltageTier voltage, double energy) {
		SimpleProcessingRecipe recipe = new SimpleProcessingRecipe(energy, voltage, in, out);
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
	 */
	public SimpleProcessingRecipe add(ItemEntry in, ItemEntry out, int amount, VoltageTier voltage, double energy) {
		return add(in, out.stack(amount), voltage, energy);
	}
	@Override
	public SelfSet<ItemEntry, SimpleProcessingRecipe> recipes() {
		return recipes;
	}

}
