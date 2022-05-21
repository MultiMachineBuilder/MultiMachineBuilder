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
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.crafting.Recipe;
import mmb.WORLD.crafting.RecipeGroup;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.crafting.SimpleItemList;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.gui.craft.CraftingRecipeList;
import mmb.WORLD.gui.craft.CraftingRecipeView;
import mmb.WORLD.gui.craft.SimpleRecipeView;
import mmb.WORLD.gui.window.TabRecipes;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.items.ItemEntry;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 *
 */
public class CraftingRecipeGroup implements RecipeGroup {
	public CraftingRecipeGroup(String title) {
		this.title = title;
		Supplier<Tuple2<String, JComponent>> sup = () -> new Tuple2<String, JComponent>(title, new CraftingRecipeList(this));
		TabRecipes.add(sup);
	}
	public final String title;
	/**
	 * Gets the recipe for given item grid
	 * @param grid the item grid
	 * @return recipe output for given grid
	 */
	public CraftingRecipe findRecipe(Grid<ItemEntry> grid) {
		Grid<ItemEntry> trim = grid.trim();
		return recipes.get(trim);
	}
	
	/**
	 * @author oskar
	 * This class defines a crafting recipe
	 */
	public class CraftingRecipe implements Identifiable<Grid<ItemEntry>>, Recipe<CraftingRecipe>{
		/** The recipe group. Usually it is {@link mmb.WORLD.crafting.Craftings#crafting} */
		@Nonnull public final CraftingRecipeGroup group;
		/** The item grid of the recipe */
		@Nonnull public final Grid<ItemEntry> grid;
		/** The outgoing items */
		@Nonnull public final RecipeOutput out;
		/** The incoming items */
		@Nonnull public final RecipeOutput in;
		public CraftingRecipe(Grid<ItemEntry> grid, RecipeOutput out) {
			super();
			group = CraftingRecipeGroup.this;
			Object2IntMap<ItemEntry> map = new Object2IntOpenHashMap<>();
			for(ItemEntry entry: grid) {
				if(entry != null) map.compute(entry, (item, amt) -> amt == null?1:amt+1);
			}
			in = new SimpleItemList(map);
			this.grid = grid;
			this.out = out;
		}
		@Override
		public Grid<ItemEntry> id() {
			return grid;
		}
		@Override
		public int maxCraftable(Inventory src, int amount) {
			return Math.min(amount, Inventory.howManyTimesThisContainsThat(src, in));
		}
		@Override
		public int craft(Inventory src, Inventory tgt, int amount) {
			return Craftings.transact(in, out, tgt, src, amount);
		}
		@Override
		public RecipeOutput output() {
			return out;
		}
		@Override
		public RecipeOutput inputs() {
			return in;
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
		public CraftingRecipe that() {
			return this;
		}
		@Override
		public Component createComponent() {
			ItemStack[] vectorI = SimpleRecipeView.list2arr(in);
			ItemStack[] vectorO = SimpleRecipeView.list2arr(out);
			CraftingRecipeView component = new CraftingRecipeView();
			component.set(this, vectorO, vectorI);
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
	
	@Override
	public Set<? extends ItemEntry> supportedItems() {
		return supported0;
	}
	private final Set<ItemEntry> supported = new HashSet<>();
	private final Set<ItemEntry> supported0 = Collections.unmodifiableSet(supported);
	//internal recipe map
	private final SelfSet<Grid<ItemEntry>, CraftingRecipe> _recipes = new HashSelfSet<>();
	/** A read-only map of all recipes */
	public final SelfSet<Grid<ItemEntry>, CraftingRecipe> recipes = Collects.unmodifiableSelfSet(_recipes);
	
	public CraftingRecipe addRecipe(ItemEntry in, RecipeOutput out) {
		return addRecipe(new FixedGrid<>(1, in), out);
	}
	public CraftingRecipe addRecipe(ItemEntry in, ItemEntry out, int amount) {
		return addRecipe(in, new ItemStack(out, amount));
	}
	public CraftingRecipe addRecipe(Grid<ItemEntry> in, ItemEntry out, int amount) {
		return addRecipe(in, new ItemStack(out, amount));
	}
	public CraftingRecipe addRecipe(Grid<ItemEntry> in, RecipeOutput out) {
		CraftingRecipe recipe = new CraftingRecipe(in, out);
		_recipes.add(recipe);
		GlobalRecipeRegistrar.addRecipe(recipe);
		for(ItemEntry ent: in) {
			supported.add(ent);
		}
		return recipe;		
	}
	public CraftingRecipe addRecipeGrid(ItemEntry in, int w, int h, RecipeOutput out) {
		return addRecipe(FixedGrid.fill(w, h, in), out);
	}
	public CraftingRecipe addRecipeGrid(ItemEntry in, int w, int h, ItemEntry out, int amount) {
		return addRecipe(FixedGrid.fill(w, h, in), out, amount);
	}
	public CraftingRecipe addRecipeGrid(ItemEntry[] in, int w, int h, RecipeOutput out) {
		return addRecipe(new FixedGrid<>(w, h, in), out);
	}
	public CraftingRecipe addRecipeGrid(ItemEntry[] in, int w, int h, ItemEntry out, int amount) {
		return addRecipe(new FixedGrid<>(w, h, in), out, amount);
	}
	@Override
	public SelfSet<Grid<ItemEntry>, CraftingRecipe> recipes() {
		return recipes;
	}
}
