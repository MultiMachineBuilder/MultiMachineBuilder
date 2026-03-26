package mmb.fluid;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FluidEventTest {
	@Test
	void shouldRejectBothStatesNull() {
		assertThrows(IllegalArgumentException.class,
			() -> new FluidEvent(null, null, 0));
	}

	@Test
	void shouldRejectNegativeIndex() {
		FluidState state = new FluidState(10.0, 1.0, TestFluids.WATER, false);

		assertThrows(IndexOutOfBoundsException.class,
			() -> new FluidEvent(state, state, -1));
	}

	@Test
	void shouldClassifyAdd() {
		FluidState newState = new FluidState(10.0, 1.0, TestFluids.WATER, false);
		FluidEvent event = new FluidEvent(null, newState, 0);

		assertEquals(FluidEventType.ADD, event.classify());
	}

	@Test
	void shouldClassifyRemove() {
		FluidState oldState = new FluidState(10.0, 1.0, TestFluids.WATER, false);
		FluidEvent event = new FluidEvent(oldState, null, 0);

		assertEquals(FluidEventType.REMOVE, event.classify());
	}

	@Test
	void shouldClassifyModifyWhenFluidStaysSame() {
		FluidState oldState = new FluidState(10.0, 1.0, TestFluids.WATER, false);
		FluidState newState = new FluidState(10.0, 2.0, TestFluids.WATER, false);
		FluidEvent event = new FluidEvent(oldState, newState, 0);

		assertEquals(FluidEventType.MODIFY, event.classify());
	}

	@Test
	void shouldClassifyReplaceWhenFluidChanges() {
		FluidState oldState = new FluidState(10.0, 1.0, TestFluids.WATER, false);
		FluidState newState = new FluidState(10.0, 1.0, TestFluids.LAVA, false);
		FluidEvent event = new FluidEvent(oldState, newState, 0);

		assertEquals(FluidEventType.REPLACE, event.classify());
	}
}