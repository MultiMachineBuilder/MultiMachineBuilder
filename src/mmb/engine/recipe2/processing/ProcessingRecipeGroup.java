package mmb.engine.recipe2.processing;

import java.util.Set;
import java.util.function.Consumer;

import mmb.annotations.NN;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.RecipeView;
import mmb.engine.recipe2.RecipeGroup;
import mmb.engine.recipe2.RecipeSetup;
import mmb.menu.world.RecipeSetupCellRenderer;

public class ProcessingRecipeGroup extends RecipeGroup<ProcessingRecipeData, RecipeSetup, ProcessingRecipeCriterion>{
	public ProcessingRecipeGroup(String id) {
		super(ProcessingRecipeData.class, id);
	}

	@Override
	public ProcessingRecipeCriterion findID(ProcessingRecipeData recipe) {
		return recipe.id();
	}

	@Override
	public void findForAutocrafters(ItemEntry stencilOrCatalyst, String craftword, int craftcode,
			Set<@NN ItemEntry> items, Consumer<@NN ProcessingRecipeData> onRecipeHit, boolean exact) {
		var setMatches = findRecipes(new RecipeSetup(stencilOrCatalyst, craftword, craftcode), items, exact);
		for(var element: setMatches) onRecipeHit.accept(element);
	}

	@Override
	public RecipeView<@NN ProcessingRecipeData> getRecipeViewComponent() {
		return new ProcessingRecipeView();
	}
	
}
