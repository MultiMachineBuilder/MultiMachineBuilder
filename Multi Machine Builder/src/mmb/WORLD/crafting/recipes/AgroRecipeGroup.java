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
import mmb.GlobalSettings;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.crafting.Recipe;
import mmb.WORLD.crafting.RecipeGroup;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.gui.craft.AgroProcessingRecipeList;
import mmb.WORLD.gui.craft.AgroRecipeView;
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
public class AgroRecipeGroup extends AbstractRecipeGroup{
	public AgroRecipeGroup(String id) {
		super(id);
	}
	@Override
	protected Tuple2<String, JComponent> createTab() {
		return new Tuple2<>(title, new AgroProcessingRecipeList(this));
	}
	
	/**
	 * @author oskar
	 * A recipe with one input item and output
	 */
	public class AgroProcessingRecipe implements Identifiable<ItemEntry>, Recipe<AgroProcessingRecipe>{
		@Nonnull public final ItemEntry input;
		@Nonnull public final RecipeOutput output;
		public final int duration;
		@Nonnull public final AgroRecipeGroup group;
		public AgroProcessingRecipe(ItemEntry input, RecipeOutput output, int duration) {
			this.input = input;
			this.output = output;
			this.duration = duration;
			group = AgroRecipeGroup.this;
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
		public AgroProcessingRecipe that() {
			return this;
		}
		@Override
		public Component createComponent() {
			AgroRecipeView component = new AgroRecipeView();
			ItemStack[] out = SimpleRecipeView.list2arr(output);
			component.set(this, out);
			return component;
		}
		@Override
		public double energy() {
			return 0;
		}
		@Override
		public VoltageTier voltTier() {
			return VoltageTier.V1;
		}
	}
	@Nonnull private final SelfSet<ItemEntry, AgroProcessingRecipe> _recipes = new HashSelfSet<>();
	@Nonnull public final SelfSet<ItemEntry, AgroProcessingRecipe> recipes = Collects.unmodifiableSelfSet(_recipes);
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
	 * @param duration time between successive drops
	 * @return the recipe
	 */
	public AgroProcessingRecipe add(ItemEntry in, RecipeOutput out, int duration) {
		AgroProcessingRecipe recipe = new AgroProcessingRecipe(in, out, duration);
		_recipes.add(recipe);
		GlobalRecipeRegistrar.addRecipe(recipe);
		supported.add(in);
		return recipe;
	}
	/**
	 * @param in input item
	 * @param out output item
	 * @param amount amount of output item
	 * @param duration time between successive drops
	 * @return the recipe
	 */
	public AgroProcessingRecipe add(ItemEntry in, ItemEntry out, int amount, int duration) {
		return add(in, out.stack(amount), duration);
	}
	@Override
	public SelfSet<ItemEntry, AgroProcessingRecipe> recipes() {
		return recipes;
	}
}
