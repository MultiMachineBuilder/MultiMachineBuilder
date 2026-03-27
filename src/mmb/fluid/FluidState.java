package mmb.fluid;

import mmb.annotations.Nil;
import mmb.engine.Verify;

/**
 * Represents contents of a fluid tank at an instant.
 * Also verifies the contents to make sure they're valid.
 */
public record FluidState(double capacity, double quantity, @Nil Fluid fluid, boolean locked) {
	/**
	 * Creates a fluid state
	 * @param capacity fluid capacity
	 * @param quantity fluid quantity
	 * @param fluid fluid type
	 * @param locked is the tank locked?
	 * @throws IllegalArgumentException when fluid == null && quantity != 0
	 * @throws IllegalArgumentException when locked && fluid == null
	 * @throws IllegalArgumentException when quantity is not non-negative (less than 0, NaN or infinite)
	 * @throws IllegalArgumentException when capacity is not positive
	 */
	public FluidState{
		if(fluid == null && quantity != 0) throw new IllegalArgumentException("Tanks with no fluid must not have a quantity");
		if(locked && fluid == null) throw new IllegalArgumentException("Fluid tanks must not be locked to null");
		Verify.requireNonNegative(quantity);
		Verify.requirePositive(capacity);
	}
	
	/**
	 * Classifies this fluid state based on how full it is and whether it is locked
	 * @return this fluid state's tank state type
	 */
	public TankStateType classify() {
		boolean isEmpty = quantity == 0;
		boolean isFull = quantity >= capacity;
		if(locked) {
			if(isEmpty) return TankStateType.LOCKED_EMPTY;
			if(isFull) return TankStateType.LOCKED_FULL;
			return TankStateType.LOCKED_PARTIAL;
		}
		if(isEmpty) return TankStateType.EMPTY;
		if(isFull) return TankStateType.FULL;
		return TankStateType.PARTIAL;
	}
}
