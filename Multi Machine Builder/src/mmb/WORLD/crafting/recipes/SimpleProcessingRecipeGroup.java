/**
 * 
 */
package mmb.WORLD.crafting.recipes;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.swing.JComponent;

import io.vavr.Tuple2;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.gui.craft.SimpleProcessingRecipeList;
import mmb.WORLD.gui.window.TabRecipes;
import mmb.WORLD.items.ItemEntry;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 *
 */
public class SimpleProcessingRecipeGroup {
	public final String title;
	public SimpleProcessingRecipeGroup(String title) {
		super();
		this.title = title;
		Supplier<Tuple2<String, JComponent>> sup = () -> new Tuple2<String, JComponent>(title, new SimpleProcessingRecipeList(this));
		TabRecipes.add(sup);
	}
	/**
	 * @author oskar
	 * A recipe with one input item and output
	 */
	public class SimpleProcessingRecipe implements Identifiable<ItemEntry>{
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
	public void add(ItemEntry in, RecipeOutput out, VoltageTier voltage, double energy) {
		_recipes.add(new SimpleProcessingRecipe(energy, voltage, in, out));
	}
	/**
	 * @param in input item
	 * @param out output item
	 * @param amount amount of output item
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 */
	public void add(ItemEntry in, ItemEntry out, int amount, VoltageTier voltage, double energy) {
		_recipes.add(new SimpleProcessingRecipe(energy, voltage, in, out.stack(amount)));
	}

}
