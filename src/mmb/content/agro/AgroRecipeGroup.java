/**
 * 
 */
package mmb.content.agro;

import java.util.Objects;

import mmb.annotations.NN;
import mmb.content.electric.VoltageTier;
import mmb.content.electric.recipes.AbstractRecipeGroupUncatalyzed;
import mmb.engine.chance.Chance;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.Recipe;
import mmb.engine.recipe.RecipeOutput;
import mmb.engine.recipe.RecipeView;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 *
 */
public class AgroRecipeGroup extends AbstractRecipeGroupUncatalyzed<@NN ItemEntry, @NN AgroRecipeGroup.AgroProcessingRecipe>{
	/**
	 * Creates a list of crop outputs
	 * @param id group ID (normally "alcohol")
	 */
	public AgroRecipeGroup(String id) {
		super(id, AgroProcessingRecipe.class);
	}
	
	/**
	 * @author oskar
	 * A recipe with one input item and output
	 */
	public class AgroProcessingRecipe implements Identifiable<ItemEntry>, Recipe<@NN AgroProcessingRecipe>{
		/** The input crop */
		@NN public final ItemEntry input;
		/** The crop's output */		
		@NN public final RecipeOutput output;
		/** Duration between drops in ticks */
		         public final int duration;
		
		/**
		 * Creates an agricultural recipe
		 * @param input input crop
		 * @param output output items
		 * @param duration duration in ticks between drops
		 */
		public AgroProcessingRecipe(ItemEntry input, RecipeOutput output, int duration) {
			Objects.requireNonNull(input, "input crop is null");
			Objects.requireNonNull(output, "output is null");
			this.input = input;
			this.output = output;
			this.duration = duration;
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
			return input;
		}
		@Override
		public AgroRecipeGroup group() {
			return AgroRecipeGroup.this;
		}
		@Override
		public AgroProcessingRecipe that() {
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
	 * @param duration time between successive drops
	 * @return the recipe
	 */
	public AgroProcessingRecipe add(ItemEntry in, RecipeOutput out, int duration) {
		AgroProcessingRecipe recipe = new AgroProcessingRecipe(in, out, duration);
		insert(recipe);
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
	
	//Others
	@Override
	public RecipeView<AgroProcessingRecipe> createView() {
		return new AgroRecipeView();
	}
}
