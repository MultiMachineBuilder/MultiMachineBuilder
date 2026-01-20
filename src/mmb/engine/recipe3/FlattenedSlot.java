package mmb.engine.recipe3;

/** A fully flattened input slot */
record FlattenedSlot(
		int position,
        int groupOrdinal,
        int minAmount,
        int maxAmount,
        int consumedAmount
) {

    public FlattenedSlot(int position, RecipeInput recipeInput) {
    	this(position, recipeInput.group().ordinal, recipeInput.minAmount(), recipeInput.maxAmount(), recipeInput.consumedAmount());
	}

	public boolean isCatalyst() {
        return consumedAmount == 0 && minAmount > 0;
    }
	public boolean isAny() {
		return groupOrdinal == 0;
	}
	public boolean isNone() {
		return groupOrdinal == 1;
	}
}
