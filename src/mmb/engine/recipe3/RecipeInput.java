package mmb.engine.recipe3;

import java.util.Objects;

public record RecipeInput(
	Group group,
	int minAmount,
	int maxAmount,
	int consumedAmount
) {
	public static final RecipeInput ANY = new RecipeInput(Group.ANY, 1, Integer.MAX_VALUE, 1);
	public static final RecipeInput NONE = new RecipeInput(Group.NONE, 0, 0, 0);
	/** 
	 * Creates a recipe input
	 * @throws NullPointerException when group is null
	 * @throws IllegalArgumentException when minAmount, maxAmount or consumedAmount is negative
	 * @throws IllegalArgumentException when minAmount > maxAmount
	 * @throws IllegalArgumentException when consumedAmount > minAmount
	 */
	public RecipeInput{
		Objects.requireNonNull(group, "group is null");
		if(minAmount < 0) throw new IllegalArgumentException("minAmount < 0");
		if(maxAmount < 0) throw new IllegalArgumentException("maxAmount < 0");
		if(consumedAmount < 0) throw new IllegalArgumentException("consumedAmount < 0");
		if(minAmount > maxAmount) throw new IllegalArgumentException ("minAmount > maxAmount");
		if(consumedAmount > minAmount) throw new IllegalArgumentException ("consumedAmount > minAmount");
	}
	
	//Conversion methods
	//TODO Item stack
	//TODO Item entry
	//TODO Group stack
	//TODO C
}
