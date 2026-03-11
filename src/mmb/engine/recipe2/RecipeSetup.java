package mmb.engine.recipe2;

import mmb.annotations.Nil;
import mmb.engine.item.ItemEntry;

public record RecipeSetup(
	@Nil ItemEntry catalyst,
	String craftWord,
	int craftCode
) {}
