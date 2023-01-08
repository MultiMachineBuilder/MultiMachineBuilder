/**
 * 
 */
package mmb.content.drugs;

import java.util.Set;

import mmb.NN;
import mmb.content.electric.VoltageTier;
import mmb.engine.chance.Chance;
import mmb.engine.craft.GlobalRecipeRegistrar;
import mmb.engine.craft.Recipe;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.craft.rgroups.AbstractRecipeGroup;
import mmb.engine.inv.Inventory;
import mmb.engine.item.ItemEntry;
import mmb.menu.world.craft.RecipeView;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 *
 */
public class AlcoholInfoGroup extends AbstractRecipeGroup<@NN AlcoholInfoGroup.AlcoholInfo>{
	/**
	 * Creates a list of information about alcoholic beverages
	 * @param id group ID (normally "alcohol")
	 */
	public AlcoholInfoGroup(String id) {
		super(id);
	}

	/**
	 * @author oskar
	 * A recipe with one input item and output
	 */
	public class AlcoholInfo implements Identifiable<ItemEntry>, Recipe<AlcoholInfo>{
		/** The alcoholic beverage*/
		@NN public final ItemEntry input;
		/** The packaging for the alcoholic beverage*/
		@NN public final RecipeOutput output;
		/** Amount of alcohol in one serving*/
		public final double dose;
		/**
		 * Creates an alcoholic beverage information table
		 * @param input the alcoholic beverage
		 * @param output the packaging for the alcoholic beverage
		 * @param dose amount of alcohol in one serving
		 */
		public AlcoholInfo(ItemEntry input, RecipeOutput output, double dose) {
			this.input = input;
			this.output = output;
			this.dose = dose;
		}
		
		@Override
		public int maxCraftable(Inventory src, int amount) {
			return Inventory.howManyTimesThisContainsThat(src, input);
		}
		
		@Override
		public ItemEntry id() {
			return input;
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
			return AlcoholInfoGroup.this;
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

		@Override
		public Chance luck() {
			return Chance.NONE;
		}
	}
	//Recipe listing
	@NN private final SelfSet<ItemEntry, AlcoholInfo> _recipes = HashSelfSet.createNonnull(AlcoholInfo.class);
	@NN public final SelfSet<ItemEntry, AlcoholInfo> recipes = Collects.unmodifiableSelfSet(_recipes);
	@Override
	public Set<? extends ItemEntry> supportedItems() {
		return recipes.keys();
	}
	@Override
	public SelfSet<ItemEntry, AlcoholInfo> recipes() {
		return recipes;
	}
	
	//Recipe addition
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
	
	//Others
	@Override public RecipeView<AlcoholInfo> createView() {
		return new AlcoholInfoView();
	}
	@Override
	public boolean isCatalyzed() {
		return false;
	}
}
