package mmb.engine.recipe3;

public class Recipe {
	public final FlattenedRecipeGroup group;
	public final RecipeSpec recipe;
	       final FlattenedRecipe flattened;
	Recipe(FlattenedRecipeGroup group, RecipeSpec spec){
		this.flattened = FlattenedRecipe.flatten(spec);
		this.recipe = spec;
		this.group = group;
	}
} 
