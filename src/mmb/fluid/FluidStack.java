package mmb.fluid;

import java.util.Objects;

import mmb.engine.Verify;
import mmb.engine.block.BlockEntry;

//TODO create a dedicated fluid type
/**
 * Represents a quantity of fluid.
 */
public record FluidStack(Fluid fluid, double amount) {
	/**
	 * Creates a new fluid stack
	 * @param fluid fluid to be represented
	 * @param amount amount of fluid stored
	 * @throws NullPointerException when fluid == null
	 * @throws IllegalArgumentException when amount is negative, NaN or infinite
	 */
	public FluidStack{
		Objects.requireNonNull(fluid, "fluid is null");
		Verify.requireNonNegative(amount);
	}
}
