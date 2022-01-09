/**
 * 
 */
package mmb.WORLD.crafting.recipes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.swing.JComponent;

import io.vavr.Tuple2;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.gui.craft.ComplexProcessingRecipeList;
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
public class ComplexProcessingRecipeGroup {
	public final String title;
	/**
	 * If this flag is set to be true, then recipes must have at least 2 ingredients
	 */
	public final boolean mustBeComplex;
	/**
	 * @param title the title of this recipe group
	 * @param mustBeComplex if this input is set to be true, then recipes must have at least 2 ingredients
	 */
	public ComplexProcessingRecipeGroup(String title, boolean mustBeComplex) {
		super();
		this.title = title;
		this.mustBeComplex = mustBeComplex;
		Supplier<Tuple2<String, JComponent>> sup = () -> new Tuple2<String, JComponent>(title, new ComplexProcessingRecipeList(this));
		TabRecipes.add(sup);
	}
	
	/**
	 * @author oskar
	 * A recipe with one input item and output
	 */
	public class ComplexProcessingRecipe implements Identifiable<Set<ItemEntry>>{
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
	}
	
	private final SelfSet<Set<ItemEntry>, ComplexProcessingRecipe> _recipes = new HashSelfSet<>();
	public final SelfSet<Set<ItemEntry>, ComplexProcessingRecipe> recipes = Collects.unmodifiableSelfSet(_recipes);
	/**
	 * Adds a recipes to this recipe group
	 * @param in input item
	 * @param out output
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 */
	public void add(RecipeOutput in, RecipeOutput out, VoltageTier voltage, double energy) {
		if(mustBeComplex && in.getContents().size() < 2) throw new IllegalArgumentException("The recipe must have at least 2 inputs");
		if(in.getContents().size() == 0) throw new IllegalArgumentException("The recipe must have at least 1 input");
		_recipes.add(new ComplexProcessingRecipe(energy, voltage, in, out));
	}
	/**
	 * @param in input item
	 * @param out output item
	 * @param amount amount of output item
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 */
	public void add(RecipeOutput in, ItemEntry out, int amount, VoltageTier voltage, double energy) {
		add(in, out.stack(amount), voltage, energy);
	}
}
