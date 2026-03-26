package mmb.fluid;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FluidStateTest {
	@Test
	void testLockedNoFluid() {
		assertThrows(IllegalArgumentException.class, () -> new FluidState(10.0, 0.0, null, true));
	}
	
	@Test
	void testNoFluidWithQuantity() {
		assertThrows(IllegalArgumentException.class, () -> new FluidState(10.0, 0.0, TestFluids.WATER, false));
	}

	@Test
	void shouldCreateValidUnlockedEmptyState() {
		FluidState state = new FluidState(10.0, 0.0, null, false);

		assertEquals(10.0, state.capacity());
		assertEquals(0.0, state.quantity());
		assertNull(state.fluid());
		assertFalse(state.locked());
	}

	@Test
	void shouldCreateValidFilledUnlockedState() {
		FluidState state = new FluidState(10.0, 4.5, TestFluids.WATER, false);

		assertEquals(10.0, state.capacity());
		assertEquals(4.5, state.quantity());
		assertEquals(TestFluids.WATER, state.fluid());
		assertFalse(state.locked());
	}

	@Test
	void shouldCreateValidFilledLockedState() {
		FluidState state = new FluidState(10.0, 4.5, TestFluids.WATER, true);

		assertEquals(10.0, state.capacity());
		assertEquals(4.5, state.quantity());
		assertEquals(TestFluids.WATER, state.fluid());
		assertTrue(state.locked());
	}

	@Test
	void shouldRejectNullFluidWithNonZeroQuantity() {
		assertThrows(IllegalArgumentException.class,
			() -> new FluidState(10.0, 1.0, null, false));
	}

	@Test
	void shouldRejectLockedNullFluid() {
		assertThrows(IllegalArgumentException.class,
			() -> new FluidState(10.0, 0.0, null, true));
	}

	@Test
	void shouldRejectNegativeQuantity() {
		assertThrows(IllegalArgumentException.class,
			() -> new FluidState(10.0, -1.0, TestFluids.WATER, false));
	}

	@Test
	void shouldRejectNaNQuantity() {
		assertThrows(IllegalArgumentException.class,
			() -> new FluidState(10.0, Double.NaN, TestFluids.WATER, false));
	}

	@Test
	void shouldRejectInfiniteQuantity() {
		assertThrows(IllegalArgumentException.class,
			() -> new FluidState(10.0, Double.POSITIVE_INFINITY, TestFluids.WATER, false));
	}

	@Test
	void shouldRejectZeroCapacity() {
		assertThrows(IllegalArgumentException.class,
			() -> new FluidState(0.0, 0.0, null, false));
	}

	@Test
	void shouldRejectNegativeCapacity() {
		assertThrows(IllegalArgumentException.class,
			() -> new FluidState(-5.0, 0.0, null, false));
	}

	@Test
	void shouldRejectNaNCapacity() {
		assertThrows(IllegalArgumentException.class,
			() -> new FluidState(Double.NaN, 0.0, null, false));
	}

	@Test
	void shouldRejectInfiniteCapacity() {
		assertThrows(IllegalArgumentException.class,
			() -> new FluidState(Double.POSITIVE_INFINITY, 0.0, null, false));
	}
}