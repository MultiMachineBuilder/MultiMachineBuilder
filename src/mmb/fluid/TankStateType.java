package mmb.fluid;

/**
 * Classifies fluid tanks based on fill degree.
 * 
 * The fill degree is 0 for empty, 1 for partial and 2 for full.
 */
public enum TankStateType {
    EMPTY,
    PARTIAL,
    FULL,
    LOCKED_EMPTY,
    LOCKED_PARTIAL,
    LOCKED_FULL;
	
	public final boolean isEmpty;
	public final boolean isFull;
	public final boolean isNotEmpty;
	public final boolean isNotFull;
	public final boolean isPartial;
	public final boolean isLocked;
	public final boolean isUnlocked;
	public final int fillDegree;
	
	private TankStateType() {
		fillDegree = ordinal() % 3;
		isEmpty = (fillDegree == 0);
		isPartial = (fillDegree == 1);
		isFull = (fillDegree == 2);
		isNotEmpty = !isEmpty;
		isNotFull = !isFull;
		isLocked = ordinal() >= 3;
		isUnlocked = !isLocked;
	}
	
	/**
	 * Returns a tank state type with given fill index and locked attribute
	 * @param fillIndex fill index
	 * @param locked is the state locked?
	 * @return tank state with given values
	 */
	public static TankStateType of(int fillIndex, boolean locked) {
		if(fillIndex < 0 || fillIndex > 2) throw new IllegalArgumentException("Fill degree must be between 0 and 2");
		int ordinal = fillIndex + (locked ? 3 : 0);
		switch(ordinal) {
			case 0: return EMPTY;
			case 1: return PARTIAL;
			case 2: return FULL;
			case 3: return LOCKED_EMPTY;
			case 4: return LOCKED_PARTIAL;
			default: return LOCKED_FULL;
		}
	}
}