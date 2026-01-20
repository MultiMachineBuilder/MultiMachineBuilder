package mmb.engine.recipe3;

public record RecipeInput(
	Group group,
	int minAmount,
	int maxAmount,
	int consumedAmount
) {
	public static final RecipeInput ANY = new RecipeInput(Group.ANY, 0, Integer.MAX_VALUE, 1);
	public static final RecipeInput NONE = new RecipeInput(Group.NONE, 0, 0, 1);
}
