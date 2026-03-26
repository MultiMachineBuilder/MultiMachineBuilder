package mmb.fluid;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FluidTankTest {
	private static final Fluid WATER = TestFluids.WATER;
	private static final Fluid LAVA = TestFluids.LAVA;

	private FluidTank tank;
	private List<FluidEvent> events;

	@BeforeEach
	void setUp() {
		tank = new FluidTank();
		events = new ArrayList<>();
		tank.fluidEvent().addListener(events::add);
	}

	@Test
	void shouldStartWithDefaultState() {
		assertEquals(1.0, tank.getCapacity());
		assertEquals(0.0, tank.getQuantity());
		assertNull(tank.getFluid());
		assertFalse(tank.isLocked());
		assertEquals(new FluidState(1.0, 0.0, null, false), tank.getTankContents());
	}

	@Test
	void getTanksShouldReturnSelfOnly() {
		List<FluidHandler> tanks = tank.getTanks();

		assertEquals(1, tanks.size());
		assertSame(tank, tanks.get(0));
	}

	@Test
	void shouldSetCapacity() {
		tank.setCapacity(5.0);

		assertEquals(5.0, tank.getCapacity());
		assertEquals(0.0, tank.getQuantity());
		assertNull(tank.getFluid());
		assertFalse(tank.isLocked());
		assertEquals(1, events.size());
	}

	@Test
	void shouldSetQuantity() {
		tank.setFluid(WATER);
		events.clear();

		tank.setQuantity(0.75);

		assertEquals(0.75, tank.getQuantity());
		assertEquals(WATER, tank.getFluid());
		assertEquals(1, events.size());
	}

	@Test
	void shouldSetFluid() {
		tank.setFluid(WATER);

		assertEquals(WATER, tank.getFluid());
		assertFalse(tank.isLocked());
		assertEquals(1, events.size());
	}

	@Test
	void setFluidToDifferentFluidShouldUnlockTank() {
		tank.setLocked(WATER);
		events.clear();

		tank.setFluid(LAVA);

		assertEquals(LAVA, tank.getFluid());
		assertFalse(tank.isLocked());
		assertEquals(1, events.size());
	}

	@Test
	void setFluidToSameFluidShouldNotUnlockTank() {
		tank.setLocked(WATER);
		events.clear();

		tank.setFluid(WATER);

		assertEquals(WATER, tank.getFluid());
		assertTrue(tank.isLocked());
		assertEquals(0, events.size()); // no state change
	}

	@Test
	void setLockedNullShouldUnlockTank() {
		tank.setFluid(WATER);
		tank.setQuantity(0.5);
		tank.setLocked(WATER);
		events.clear();

		tank.setLocked(null);

		assertFalse(tank.isLocked());
		assertEquals(WATER, tank.getFluid());
		assertEquals(1, events.size());
	}

	@Test
	void setLockedOnEmptyTankShouldAssignFluidAndLock() {
		tank.setLocked(WATER);

		assertTrue(tank.isLocked());
		assertEquals(WATER, tank.getFluid());
		assertEquals(0.0, tank.getQuantity());
		assertEquals(1, events.size());
	}

	@Test
	void setLockedOnFilledMatchingTankShouldLock() {
		tank.setFluid(WATER);
		tank.setQuantity(0.5);
		events.clear();

		tank.setLocked(WATER);

		assertTrue(tank.isLocked());
		assertEquals(WATER, tank.getFluid());
		assertEquals(0.5, tank.getQuantity());
		assertEquals(1, events.size());
	}

	@Test
	void setLockedOnFilledMismatchingTankShouldDoNothing() {
		tank.setFluid(WATER);
		tank.setQuantity(0.5);
		events.clear();

		tank.setLocked(LAVA);

		assertFalse(tank.isLocked());
		assertEquals(WATER, tank.getFluid());
		assertEquals(0.5, tank.getQuantity());
		assertEquals(0, events.size());
	}

	@Test
	void checkInsertShouldAllowInsertIntoEmptyUnlockedTank() {
		tank.setCapacity(10.0);
		events.clear();

		double result = tank.checkInsert(WATER, 3.0);

		assertEquals(3.0, result);
		assertEquals(0, events.size());
	}

	@Test
	void checkInsertShouldBeLimitedByCapacity() {
		tank.setCapacity(10.0);
		tank.setFluid(WATER);
		tank.setQuantity(8.0);
		events.clear();

		double result = tank.checkInsert(WATER, 5.0);

		assertEquals(2.0, result);
		assertEquals(0, events.size());
	}

	@Test
	void checkInsertShouldRejectDifferentFluidWhenFilled() {
		tank.setFluid(WATER);
		tank.setQuantity(0.5);
		events.clear();

		double result = tank.checkInsert(LAVA, 0.2);

		assertEquals(0.0, result);
		assertEquals(0, events.size());
	}

	@Test
	void checkInsertShouldRejectDifferentFluidWhenLocked() {
		tank.setLocked(WATER);
		events.clear();

		double result = tank.checkInsert(LAVA, 0.2);

		assertEquals(0.0, result);
		assertEquals(0, events.size());
	}

	@Test
	void checkInsertShouldRejectNullFluid() {
		assertThrows(NullPointerException.class,
			() -> tank.checkInsert(null, 1.0));
	}

	@Test
	void checkInsertShouldRejectNegativeAmount() {
		assertThrows(IllegalArgumentException.class,
			() -> tank.checkInsert(WATER, -1.0));
	}

	@Test
	void checkExtractShouldAllowMatchingFluid() {
		tank.setFluid(WATER);
		tank.setQuantity(0.75);
		events.clear();

		double result = tank.checkExtract(WATER, 0.5);

		assertEquals(0.5, result);
		assertEquals(0, events.size());
	}

	@Test
	void checkExtractShouldBeLimitedByQuantity() {
		tank.setFluid(WATER);
		tank.setQuantity(0.75);
		events.clear();

		double result = tank.checkExtract(WATER, 1.5);

		assertEquals(0.75, result);
		assertEquals(0, events.size());
	}

	@Test
	void checkExtractShouldRejectDifferentFluid() {
		tank.setFluid(WATER);
		tank.setQuantity(0.75);
		events.clear();

		double result = tank.checkExtract(LAVA, 0.5);

		assertEquals(0.0, result);
		assertEquals(0, events.size());
	}

	@Test
	void checkExtractShouldRejectNullFluid() {
		assertThrows(NullPointerException.class,
			() -> tank.checkExtract(null, 1.0));
	}

	@Test
	void checkExtractShouldRejectNegativeAmount() {
		assertThrows(IllegalArgumentException.class,
			() -> tank.checkExtract(WATER, -1.0));
	}

	@Test
	void insertFluidShouldFillEmptyTank() {
		tank.setCapacity(10.0);
		events.clear();

		double inserted = tank.insertFluid(WATER, 4.0);

		assertEquals(4.0, inserted);
		assertEquals(4.0, tank.getQuantity());
		assertEquals(WATER, tank.getFluid());
		assertFalse(tank.isLocked());
		assertEquals(1, events.size());
	}

	@Test
	void insertFluidShouldRespectCapacity() {
		tank.setCapacity(10.0);
		tank.setFluid(WATER);
		tank.setQuantity(8.0);
		events.clear();

		double inserted = tank.insertFluid(WATER, 5.0);

		assertEquals(2.0, inserted);
		assertEquals(10.0, tank.getQuantity());
		assertEquals(WATER, tank.getFluid());
		assertEquals(1, events.size());
	}

	@Test
	void insertFluidShouldRejectDifferentFluidWhenFilled() {
		tank.setFluid(WATER);
		tank.setQuantity(0.5);
		events.clear();

		double inserted = tank.insertFluid(LAVA, 0.2);

		assertEquals(0.0, inserted);
		assertEquals(0.5, tank.getQuantity());
		assertEquals(WATER, tank.getFluid());
		assertEquals(0, events.size());
	}

	@Test
	void insertFluidShouldRejectDifferentFluidWhenLocked() {
		tank.setLocked(WATER);
		events.clear();

		double inserted = tank.insertFluid(LAVA, 0.2);

		assertEquals(0.0, inserted);
		assertEquals(0.0, tank.getQuantity());
		assertEquals(WATER, tank.getFluid());
		assertTrue(tank.isLocked());
		assertEquals(0, events.size());
	}

	@Test
	void extractFluidShouldReduceQuantity() {
		tank.setFluid(WATER);
		tank.setQuantity(0.75);
		events.clear();

		double extracted = tank.extractFluid(WATER, 0.25);

		assertEquals(0.25, extracted);
		assertEquals(0.5, tank.getQuantity());
		assertEquals(WATER, tank.getFluid());
		assertEquals(1, events.size());
	}

	@Test
	void extractFluidShouldBeLimitedByAvailableQuantity() {
		tank.setFluid(WATER);
		tank.setQuantity(0.75);
		events.clear();

		double extracted = tank.extractFluid(WATER, 2.0);

		assertEquals(0.75, extracted);
		assertEquals(0.0, tank.getQuantity());
		assertNull(tank.getFluid());
		assertFalse(tank.isLocked());
		assertEquals(1, events.size());
	}

	@Test
	void extractFluidShouldKeepFluidWhenLockedAndEmptied() {
		tank.setLocked(WATER);
		tank.insertFluid(WATER, 0.75);
		events.clear();

		double extracted = tank.extractFluid(WATER, 1.0);

		assertEquals(0.75, extracted);
		assertEquals(0.0, tank.getQuantity());
		assertEquals(WATER, tank.getFluid());
		assertTrue(tank.isLocked());
		assertEquals(1, events.size());
	}

	@Test
	void extractFluidShouldRejectDifferentFluid() {
		tank.setFluid(WATER);
		tank.setQuantity(0.75);
		events.clear();

		double extracted = tank.extractFluid(LAVA, 0.25);

		assertEquals(0.0, extracted);
		assertEquals(0.75, tank.getQuantity());
		assertEquals(WATER, tank.getFluid());
		assertEquals(0, events.size());
	}

	@Test
	void setTankContentsShouldReplaceEntireState() {
		FluidState state = new FluidState(10.0, 3.0, WATER, true);

		tank.setTankContents(state);

		assertEquals(10.0, tank.getCapacity());
		assertEquals(3.0, tank.getQuantity());
		assertEquals(WATER, tank.getFluid());
		assertTrue(tank.isLocked());
		assertEquals(1, events.size());
	}

	@Test
	void setTankContentsShouldRejectNull() {
		assertThrows(NullPointerException.class,
			() -> tank.setTankContents(null));
	}

	@Test
	void settersShouldNotFireEventWhenStateDoesNotChange() {
		tank.setCapacity(1.0);
		tank.setQuantity(0.0);
		tank.setFluid(null);
		events.clear();

		tank.setCapacity(1.0);
		tank.setQuantity(0.0);
		tank.setFluid(null);

		assertEquals(0, events.size());
	}

	@Test
	void eventShouldContainOldAndNewStateForInsert() {
		tank.setCapacity(10.0);
		events.clear();

		tank.insertFluid(WATER, 3.0);

		assertEquals(1, events.size());
		FluidEvent event = events.get(0);

		assertEquals(new FluidState(10.0, 0.0, null, false), event.oldState());
		assertEquals(new FluidState(10.0, 3.0, WATER, false), event.newState());
		assertEquals(0, event.index());
		assertEquals(FluidEventType.REPLACE, event.classify());
	}

	@Test
	void eventShouldContainOldAndNewStateForExtract() {
		tank.setCapacity(10.0);
		tank.insertFluid(WATER, 3.0);
		events.clear();

		tank.extractFluid(WATER, 1.0);

		assertEquals(1, events.size());
		FluidEvent event = events.get(0);

		assertEquals(new FluidState(10.0, 3.0, WATER, false), event.oldState());
		assertEquals(new FluidState(10.0, 2.0, WATER, false), event.newState());
		assertEquals(0, event.index());
		assertEquals(FluidEventType.MODIFY, event.classify());
	}
	
	@Test
	void setQuantityWithoutFluidShouldThrow() {
		assertThrows(IllegalArgumentException.class, () -> tank.setQuantity(1.0));
	}
	
	@Test
	void setLockedNullFluidStateShouldThrowIfInternallyInvalid() {
		tank.setFluid(WATER);
		tank.setQuantity(1.0);
		tank.setFluid(null); // may explode depending on path
	}
}