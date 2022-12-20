/**
 * 
 */
package mmb.engine.craft.rgroups;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.vavr.Tuple2;
import mmb.NN;
import mmb.Nil;
import mmb.content.CraftingGroups;
import mmb.content.agro.AgroRecipeGroup.AgroProcessingRecipe;
import mmb.content.electric.VoltageTier;
import mmb.engine.chance.Chance;
import mmb.engine.craft.GlobalRecipeRegistrar;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.craft.singles.SimpleRecipe;
import mmb.engine.craft.singles.SimpleRecipeGroup;
import mmb.engine.inv.Inventory;
import mmb.engine.item.ItemEntry;
import mmbbase.menu.world.craft.SimpleCatalyzedRecipeView;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * A group of recipes with single input and a catalyst
 * @author oskar
 */
public class CatalyzedSingleRecipeGroup extends AbstractRecipeGroup<CatalyzedSingleRecipeGroup.CatalyzedSimpleRecipe>
implements SimpleRecipeGroup<CatalyzedSingleRecipeGroup.CatalyzedSimpleRecipe>{
	/**
	 * Creates a list of catalyzed single-item recipes
	 * @param id group ID
	 */
	public CatalyzedSingleRecipeGroup(String id) {
		super(id);
	}
	
	/**
	 * @author oskar
	 * A recipe with single input and a catlayst
	 */
	public class CatalyzedSimpleRecipe extends BaseElectricRecipe<CatalyzedSimpleRecipe> implements Identifiable<Tuple2<ItemEntry, ItemEntry>>, SimpleRecipe<CatalyzedSimpleRecipe>{
		/** The input item */
		@NN public final ItemEntry input;
		/** Required catalyst */
		@Nil public final ItemEntry catalyst;
		/** Item tuple */
		@NN public final Tuple2<ItemEntry, ItemEntry> tuple;
		
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
		public int maxCraftable(Inventory src, int amount) {
			return Inventory.howManyTimesThisContainsThat(src, input);
		}
		
		@Override
		public Tuple2<ItemEntry, ItemEntry> id() {
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
		public CatalyzedSingleRecipeGroup group() {
			return CatalyzedSingleRecipeGroup.this;
		}
		@Override
		public CatalyzedSimpleRecipe that() {
			return this;
		}
	}
	
	//Recipe listing
	@NN private final SelfSet<Tuple2<ItemEntry, ItemEntry>, CatalyzedSimpleRecipe> _recipes = HashSelfSet.createNonnull(CatalyzedSimpleRecipe.class);
	@NN public final SelfSet<Tuple2<ItemEntry, ItemEntry>, CatalyzedSimpleRecipe> recipes = Collects.unmodifiableSelfSet(_recipes);
	@Override
	public Set<? extends ItemEntry> supportedItems() {
		return supported0;
	}
	private final Set<ItemEntry> supported = new HashSet<>();
	private final Set<ItemEntry> supported0 = Collections.unmodifiableSet(supported);
	
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
		CatalyzedSimpleRecipe recipe = new CatalyzedSimpleRecipe(energy, voltage, in, catalyst, out, luck);
		_recipes.add(recipe);
		GlobalRecipeRegistrar.addRecipe(recipe);
		supported.add(in);
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
	public SelfSet<Tuple2<ItemEntry, ItemEntry>, CatalyzedSimpleRecipe> recipes() {
		return recipes;
	}
	@Override
	public SimpleCatalyzedRecipeView createView() {
		return new SimpleCatalyzedRecipeView();
	}
	@Override
	public boolean isCatalyzed() {
		return false;
	}
	@Override
	public CatalyzedSimpleRecipe findRecipe(@Nil ItemEntry catalyst, ItemEntry in) {
		return recipes.get(new Tuple2<>(in, catalyst));
	}
}
