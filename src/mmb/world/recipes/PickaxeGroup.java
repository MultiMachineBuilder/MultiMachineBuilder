/**
 * 
 */
package mmb.world.recipes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.swing.JComponent;

import io.vavr.Tuple2;
import mmb.GlobalSettings;
import mmb.beans.Titled;
import mmb.menu.world.craft.PickaxeList;
import mmb.menu.world.window.TabRecipes;
import mmb.world.inventory.Inventory;
import mmb.world.item.ItemEntry;
import mmb.world.item.ItemType;
import mmb.world.recipes.AgroRecipeGroup.AgroProcessingRecipe;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 * Contains information about pickaxes.
 * THIS IS NOT A RECIPE GROUP
 */
public class PickaxeGroup implements Identifiable<String>, Titled{
	@Nonnull private final String title;
	@Nonnull private final String id;
	public PickaxeGroup(String id) {
		this.id = id;
		this.title = GlobalSettings.$res("machine-"+id);
		Supplier<Tuple2<String, JComponent>> sup = () -> createTab();
		TabRecipes.add(sup);
	}
	/**
	 * Create a recipe tab. Invoked once per recipe group
	 * @return
	 */
	protected final Tuple2<String, JComponent> createTab(){
		return new Tuple2<>(title, new PickaxeList(this));
	}

	/**
	 * @author oskar
	 * A recipe with one input item and output
	 */
	public class PickaxeInfo implements Identifiable<ItemEntry>{
		@Nonnull public final ItemEntry input;
		@Nonnull public final ItemType output;
		@Nonnull public final PickaxeGroup group;
		public PickaxeInfo(ItemEntry input, ItemType output) {
			this.input = input;
			this.output = output;
			group = PickaxeGroup.this;
		}
		@Override
		public ItemEntry id() {
			return input;
		}
		public int maxCraftable(Inventory src, int amount) {
			return Inventory.howManyTimesThisContainsThat(src, input);
		}
		public int craft(Inventory src, Inventory tgt, int amount) {
			return CraftingGroups.transact(input, output.create(), tgt, src, amount);
		}
		public ItemType output() {
			return output;
		}
		public ItemEntry inputs() {
			return input;
		}
		public PickaxeGroup group() {
			return group;
		}
		public PickaxeInfo that() {
			return this;
		}
	}
	@Nonnull private final SelfSet<ItemEntry, PickaxeInfo> _recipes = HashSelfSet.createNonnull(PickaxeInfo.class);
	@Nonnull public final SelfSet<ItemEntry, PickaxeInfo> recipes = Collects.unmodifiableSelfSet(_recipes);
	public Set<? extends ItemEntry> supportedItems() {
		return supported0;
	}
	private final Set<ItemEntry> supported = new HashSet<>();
	private final Set<ItemEntry> supported0 = Collections.unmodifiableSet(supported);
	/**
	 * Adds a recipes to this recipe group
	 * @param in input item
	 * @param out output
	 * @return the recipe
	 */
	public PickaxeInfo add(ItemEntry in, ItemType out) {
		PickaxeInfo recipe = new PickaxeInfo(in, out);
		_recipes.add(recipe);
		supported.add(in);
		return recipe;
	}
	public SelfSet<ItemEntry, PickaxeInfo> recipes() {
		return recipes;
	}
	@Override
	public String title() {
		return title;
	}
	@Override
	public String id() {
		return id;
	}
}
