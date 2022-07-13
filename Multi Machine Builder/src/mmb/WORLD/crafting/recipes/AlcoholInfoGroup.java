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
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.gui.craft.AlcoholInfoView;
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
public class AlcoholInfoGroup extends AbstractRecipeGroup<AlcoholInfoGroup.AlcoholInfo>{
	public AlcoholInfoGroup(String id) {
		super(id);
	}

	/**
	 * @author oskar
	 * A recipe with one input item and output
	 */
	public class AlcoholInfo implements Identifiable<ItemEntry>, Recipe<AlcoholInfo>{
		@Nonnull public final ItemEntry input;
		@Nonnull public final RecipeOutput output;
		@Nonnull public final AlcoholInfoGroup group;
		public final double dose;
		public AlcoholInfo(ItemEntry input, RecipeOutput output, double dose) {
			this.input = input;
			this.output = output;
			this.dose = dose;
			group = AlcoholInfoGroup.this;
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
		public AlcoholInfoGroup group() {
			return group;
		}
		@Override
		public AlcoholInfo that() {
			return this;
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
	@Nonnull private final SelfSet<ItemEntry, AlcoholInfo> _recipes = new HashSelfSet<>();
	@Nonnull public final SelfSet<ItemEntry, AlcoholInfo> recipes = Collects.unmodifiableSelfSet(_recipes);
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
	 * @param dose time between successive drops
	 * @return the recipe
	 */
	public AlcoholInfo add(ItemEntry in, RecipeOutput out, double dose) {
		AlcoholInfo recipe = new AlcoholInfo(in, out, dose);
		_recipes.add(recipe);
		GlobalRecipeRegistrar.addRecipe(recipe);
		supported.add(in);
		return recipe;
	}
	/**
	 * @param in input item
	 * @param out output item
	 * @param amount amount of output item
	 * @param dose amount of alcohol per drink
	 * @return the recipe
	 */
	public AlcoholInfo add(ItemEntry in, ItemEntry out, int amount, double dose) {
		return add(in, out.stack(amount), dose);
	}
	@Override
	public SelfSet<ItemEntry, AlcoholInfo> recipes() {
		return recipes;
	}
	@Override
	public RecipeView<AlcoholInfo> createView() {
		return new AlcoholInfoView();
	}
}
