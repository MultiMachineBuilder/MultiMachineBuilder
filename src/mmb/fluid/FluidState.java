package mmb.fluid;

import mmb.annotations.Nil;
import mmb.engine.Verify;

/**
 * Represents contents of a fluid tank at an instant. Also verifies the contents to make sure they're valid.
 */
public record FluidState(double capacity, double quantity, @Nil Fluid fluid, boolean locked) {
	/**
	 * Creates a fluid state
	 * @param capacity fluid capacity
	 * @param quantity fluid quantiy
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
}
