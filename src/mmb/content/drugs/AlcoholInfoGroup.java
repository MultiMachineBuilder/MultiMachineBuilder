/**
 * 
 */
package mmb.content.drugs;

import mmb.NN;
import mmb.content.electric.VoltageTier;
import mmb.engine.chance.Chance;
import mmb.engine.craft.Recipe;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.craft.rgroups.AbstractRecipeGroupUncatalyzed;
import mmb.engine.item.ItemEntry;
import mmb.menu.world.craft.RecipeView;
import monniasza.collects.Identifiable;

/**
 * Contains information about alcoholic beverages
 * @author oskar
 */
public class AlcoholInfoGroup extends AbstractRecipeGroupUncatalyzed<@NN ItemEntry, @NN AlcoholInfoGroup.AlcoholInfo>{
	/**
	 * Creates a list of information about alcoholic beverages
	 * @param id group ID (normally "alcohol")
	 */
	public AlcoholInfoGroup(String id) {
		super(id, AlcoholInfo.class);
	}

	/**
	 * @author oskar
	 * A recipe with one input item and output
	 */
	public class AlcoholInfo implements Identifiable<ItemEntry>, Recipe<@NN AlcoholInfo>{
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
	
	//Recipe addition
	/**
	 * Adds a recipes to this recipe group
	 * @param in input item
	 * @param out output
	 * @param dose time between successive drops
	 * @return the recipe
	 */
	public AlcoholInfo add(ItemEntry in, RecipeOutput out, double dose) {
		@NN AlcoholInfo recipe = new AlcoholInfo(in, out, dose);
		insert(recipe);
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
}
