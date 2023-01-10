/**
 * 
 */
package mmb.engine.craft.rgroups;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.NN;
import mmb.Nil;
import mmb.content.electric.VoltageTier;
import mmb.engine.chance.Chance;
import mmb.engine.craft.Recipe;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.craft.SimpleItemList;
import mmb.engine.inv.ItemStack;
import mmb.engine.item.ItemEntry;
import mmb.menu.world.craft.CraftingRecipeView;
import monniasza.collects.Identifiable;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

/**
 * @author oskar
 *
 */
public class CraftingRecipeGroup extends AbstractRecipeGroup<@NN Grid<ItemEntry>, @NN CraftingRecipeGroup.CraftingRecipe> {
	public CraftingRecipeGroup(String id) {
		super(id, CraftingRecipe.class);
	}
	/**
	 * Gets the recipe for given item grid
	 * @param grid the item grid
	 * @return recipe output for given grid, or null if not found
	 */
	@Nil public CraftingRecipe findRecipe(Grid<ItemEntry> grid) {
		Grid<ItemEntry> trim = grid.trim();
		return recipes().get(trim);
	}
	
	/**
	 * @author oskar
	 * This class defines a crafting recipe
	 */
	public class CraftingRecipe implements Identifiable<Grid<ItemEntry>>, Recipe<@NN CraftingRecipe>{
		/** The recipe group. Usually it is {@link mmb.content.CraftingGroups#crafting} */
		@NN public final CraftingRecipeGroup group;
		/** The item grid of the recipe */
		@NN public final Grid<ItemEntry> grid;
		/** The outgoing items */
		@NN public final RecipeOutput out;
		/** The incoming items */
		@NN public final RecipeOutput in;
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
		public CraftingRecipeGroup group() {
			return group;
		}
		@Override
		public CraftingRecipe that() {
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
		@NN CraftingRecipe recipe = new CraftingRecipe(in, out);
		insert(recipe);
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
	public CraftingRecipeView createView() {
		return new CraftingRecipeView();
	}
	@Override
	public boolean isCatalyzed() {
		return true;
	}
	@Override
	public Set<ItemEntry> items4id(Grid<ItemEntry> id) {
		Set<ItemEntry> items = new HashSet<>();
		for(ItemEntry item: id) {
			items.add(item);
		}
		return Collections.unmodifiableSet(items);
	}
}
