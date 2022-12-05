/**
 * 
 */
package mmbgame.agro;

import java.util.Set;
import javax.annotation.Nonnull;

import mmb.menu.world.craft.RecipeView;
import mmbeng.chance.Chance;
import mmbeng.craft.GlobalRecipeRegistrar;
import mmbeng.craft.Recipe;
import mmbeng.craft.RecipeOutput;
import mmbeng.craft.rgroups.AbstractRecipeGroup;
import mmbeng.inv.Inventory;
import mmbeng.item.ItemEntry;
import mmbgame.CraftingGroups;
import mmbgame.electric.VoltageTier;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 *
 */
public class AgroRecipeGroup extends AbstractRecipeGroup<AgroRecipeGroup.AgroProcessingRecipe>{
	/**
	 * Creates a list of crop outputs
	 * @param id group ID (normally "alcohol")
	 */
	public AgroRecipeGroup(String id) {
		super(id);
	}
	
	/**
	 * @author oskar
	 * A recipe with one input item and output
	 */
	public class AgroProcessingRecipe implements Identifiable<ItemEntry>, Recipe<AgroProcessingRecipe>{
		/** The input crop */
		@Nonnull public final ItemEntry input;
		/** The crop's output */		
		@Nonnull public final RecipeOutput output;
		/** Duration between drops in ticks */
		         public final int duration;
		
		/**
		 * Creates an agricultural recipe
		 * @param input input crop
		 * @param output output items
		 * @param duration duration in ticks between drops
		 */
		public AgroProcessingRecipe(ItemEntry input, RecipeOutput output, int duration) {
			this.input = input;
			this.output = output;
			this.duration = duration;
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
	
	//Recipe listing
	@Nonnull private final SelfSet<ItemEntry, AgroProcessingRecipe> _recipes = HashSelfSet.createNonnull(AgroProcessingRecipe.class);
	@Nonnull public final SelfSet<ItemEntry, AgroProcessingRecipe> recipes = Collects.unmodifiableSelfSet(_recipes);
	@Override
	public Set<? extends ItemEntry> supportedItems() {
		return recipes.keys();
	}
	@Override
	public SelfSet<ItemEntry, AgroProcessingRecipe> recipes() {
		return recipes;
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
		_recipes.add(recipe);
		GlobalRecipeRegistrar.addRecipe(recipe);
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
	@Override
	public boolean isCatalyzed() {
		return false;
	}
}
