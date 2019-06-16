package addon;

import recipes.RecipeMachine;
import recipes.RecipeType;

public interface CooperationFeature<A extends AddonCentral, B extends AddonCentral> {
	void addCompatibleRecipeMachine(RecipeType rt, RecipeMachine rm);
}
