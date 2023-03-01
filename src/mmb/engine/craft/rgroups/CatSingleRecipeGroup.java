/**
 * 
 */
package mmb.engine.craft.rgroups;

import io.vavr.Tuple2;
import mmb.NN;
import mmb.Nil;
import mmb.content.electric.VoltageTier;
import mmb.engine.chance.Chance;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.item.ItemEntry;
import mmb.menu.world.craft.SimpleCatalyzedRecipeView;
import monniasza.collects.Identifiable;

/**
 * A single-item, catalyzed recipe group
 * @author oskar
 */
public class CatSingleRecipeGroup extends AbstractRecipeGroupCatalyzed<@NN ItemEntry, @NN CatSingleRecipeGroup.CatalyzedSimpleRecipe>
implements SimpleRecipeGroup<CatSingleRecipeGroup.CatalyzedSimpleRecipe>{
	/**
	 * Creates a list of catalyzed single-item recipes
	 * @param id group ID
	 */
	public CatSingleRecipeGroup(String id) {
		super(id, CatalyzedSimpleRecipe.class);
	}
	
	/**
	 * @author oskar
	 * A recipe with single input and a catlayst
	 */
	public class CatalyzedSimpleRecipe extends BaseElectricRecipe<@NN CatalyzedSimpleRecipe> implements Identifiable<Tuple2<@NN ItemEntry, ItemEntry>>, SimpleRecipe<@NN CatalyzedSimpleRecipe>{
		/** The input item */
		@NN public final ItemEntry input;
		/** Required catalyst */
		@Nil public final ItemEntry catalyst;
		/** Item tuple */
		@NN public final Tuple2<@NN ItemEntry, ItemEntry> tuple;
		
		/**
		 * Creates a single catalyzed recipe
		 * @param energy energy required for completion in joules
		 * @param voltage voltage tier required for this recipe
		 * @param input the input item
		 * @param output deterministic output of this recipe
		 * @param catalyst required catalyst
		 * @param luck randomized output of this recipe
		 */
		public CatalyzedSimpleRecipe(double energy, VoltageTier voltage, ItemEntry input, @Nil ItemEntry catalyst, RecipeOutput output, Chance luck) {
			super(energy, voltage, output, luck);
			this.input = input;
			this.catalyst = catalyst;
			this.tuple = new Tuple2<>(input, catalyst);
		}
		@Override
		public @NN Tuple2<@NN ItemEntry, ItemEntry> id() {
			return tuple;
		}
		@Override
		public ItemEntry inputs() {
			return input;
		}
		@Override
		public ItemEntry catalyst() {
			return catalyst;
		}
		@Override
		public CatSingleRecipeGroup group() {
			return CatSingleRecipeGroup.this;
		}
		@Override
		public CatalyzedSimpleRecipe that() {
			return this;
		}
	}
	
	//Recipe addition
	/**
	 * Adds a recipes to this recipe group
	 * @param in input item
	 * @param out output
	 * @param catalyst catalyst required to run the recipe
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @param luck random chanced items
	 * @return a new recipe
	 */
	public CatalyzedSimpleRecipe add(ItemEntry in, RecipeOutput out, @Nil ItemEntry catalyst, VoltageTier voltage, double energy, Chance luck) {
		@NN CatalyzedSimpleRecipe recipe = new CatalyzedSimpleRecipe(energy, voltage, in, catalyst, out, luck);
		insert(recipe);
		return recipe;
	}
	/**
	 * @param in input item
	 * @param out output item
	 * @param catalyst catalyst required to run the recipe
	 * @param amount amount of output item
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @param luck random chanced items
	 * @return a new recipe
	 */
	public CatalyzedSimpleRecipe add(ItemEntry in, ItemEntry out, @Nil ItemEntry catalyst, int amount, VoltageTier voltage, double energy, Chance luck) {
		return add(in, out.stack(amount), catalyst, voltage, energy, luck);
	}
	/**
	 * Adds a recipes to this recipe group
	 * @param in input item
	 * @param out output
	 * @param catalyst catalyst required to run the recipe
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @return a new recipe
	 */
	public CatalyzedSimpleRecipe add(ItemEntry in, RecipeOutput out, @Nil ItemEntry catalyst, VoltageTier voltage, double energy) {
		return add(in, out, catalyst, voltage, energy, Chance.NONE);
	}
	/**
	 * @param in input item
	 * @param out output item
	 * @param catalyst catalyst required to run the recipe
	 * @param amount amount of output item
	 * @param voltage voltage tier required by this recipe
	 * @param energy energy consumed by this recipe
	 * @return a new recipe
	 */
	public CatalyzedSimpleRecipe add(ItemEntry in, ItemEntry out, @Nil ItemEntry catalyst, int amount, VoltageTier voltage, double energy) {
		return add(in, out.stack(amount), catalyst, voltage, energy, Chance.NONE);
	}
	
	//Others
	@Override
	public SimpleCatalyzedRecipeView createView() {
		return new SimpleCatalyzedRecipeView();
	}
	@Override
	public CatalyzedSimpleRecipe findRecipe(@Nil ItemEntry catalyst, ItemEntry in) {
		return recipes().get(new Tuple2<>(in, catalyst));
	}
}
