package mmb.fluid;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FluidStackTest {
	@Test
	void shouldCreateValidFluidStack() {
		FluidStack stack = new FluidStack(TestFluids.LAVA, 2.5);

		assertEquals(TestFluids.LAVA, stack.fluid());
		assertEquals(2.5, stack.amount());
	}

	@Test
	void shouldRejectNullFluid() {
		assertThrows(NullPointerException.class,
			() -> new FluidStack(null, 1.0));
	}

	@Test
	void shouldRejectNegativeAmount() {
		assertThrows(IllegalArgumentException.class,
			() -> new FluidStack(TestFluids.LAVA, -1.0));
	}

	@Test
	void shouldRejectNaNAmount() {
		assertThrows(IllegalArgumentException.class,
			() -> new FluidStack(TestFluids.LAVA, Double.NaN));
	}

	@Test
	void shouldRejectInfiniteAmount() {
		assertThrows(IllegalArgumentException.class,
			() -> new FluidStack(TestFluids.LAVA, Double.POSITIVE_INFINITY));
	}
}