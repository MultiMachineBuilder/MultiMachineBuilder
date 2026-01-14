package mmb.engine.recipe2.crafting;

import java.util.Set;
import java.util.function.Consumer;

import mmb.annotations.NN;
import mmb.content.ditems.Stencil;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe2.CraftingRecipeData;
import mmb.engine.recipe2.RecipeGroup;
import monniasza.collects.grid.Grid;

public class CraftingRecipeGroup extends RecipeGroup<CraftingRecipeData, Grid<ItemEntry>, Grid<ItemEntry>>{
	public CraftingRecipeGroup() {
		super(CraftingRecipeData.class);
	}

	@Override
	public Grid<ItemEntry> findID(CraftingRecipeData recipe) {
		return recipe.items;
	}

	@Override
	public void findForAutocrafters(ItemEntry stencilOrCatalyst, String craftword, int craftcode,
			Set<@NN ItemEntry> items, Consumer<@NN CraftingRecipeData> onRecipeHit, boolean exact) {
		if(stencilOrCatalyst instanceof Stencil stencil) {
			var grid = stencil.grid();
			var hit = recipeKeyIndex.map.get(grid);
			if(hit != null) onRecipeHit.accept(hit);
		}
	}
}
