package mmb.engine.recipe2.agro;

import java.util.Set;
import java.util.function.Consumer;

import mmb.annotations.NN;
import mmb.engine.block.BlockEntityType;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.RecipeView;
import mmb.engine.recipe2.RecipeGroup;

/**
 * 
 */
public class AgroRecipeGroup extends RecipeGroup<AgroRecipe, BlockEntityType, BlockEntityType> {
	public AgroRecipeGroup(String id) {
		super(AgroRecipe.class, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BlockEntityType findID(AgroRecipe recipe) {
		return recipe.crop();
	}

	@Override
	public RecipeView<@NN AgroRecipe> getRecipeViewComponent() {
		return new AgroRecipeView();
	}

	@Override
	public void findForAutocrafters(ItemEntry stencilOrCatalyst, String craftword, int craftcode,
			Set<@NN ItemEntry> items, Consumer<@NN AgroRecipe> onRecipeHit, boolean exact) {
		var match = recipeKeyIndex.map.get(stencilOrCatalyst);
		if(match != null) onRecipeHit.accept(match);
	}

}
