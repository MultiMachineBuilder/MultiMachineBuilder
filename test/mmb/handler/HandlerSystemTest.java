package mmb.handler;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mmb.content.modular.cover.Cover;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.item.Item;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.world.World;

/**
 * Tests for HandlerSystem and HandlerMappedCoverList.
 *
 * NOTE:
 * This test assumes:
 * - HandlerKey is constructible as new HandlerKey(int x, int y, ? side)
 * - HandlerSystems.handlerGetters is accessible and mutable in tests
 *
 * If your actual constructors differ, adjust helper methods accordingly.
 */
class HandlerSystemTest {

	private World world;
	private HandlerSystem system;

	private HandlerKey keyA;
	private HandlerKey keyB;

	private TestCover c1;
	private TestCover c2;
	private TestCover c3;
	private TestCover c4;

	@BeforeEach
	void setUp() {
		world = new World(3, 3, 0, 0);
		system = new HandlerSystem(world);

		keyA = key(1, 1);
		keyB = key(2, 2);

		c1 = new TestCover("c1");
		c2 = new TestCover("c2");
		c3 = new TestCover("c3");
		c4 = new TestCover("c4");
	}

	// ------------------------------------------------------------
	// HandlerSystem.getCoverList / makeList behavior
	// ------------------------------------------------------------

	@Test
	void getCoverListReturnsSameViewForSameKey() {
		List<Cover> a = system.getCoverList(keyA);
		List<Cover> b = system.getCoverList(keyA);

		assertSame(a, b);
	}

	@Test
	void getCoverListDifferentKeysAreDifferentViews() {
		List<Cover> a = system.getCoverList(keyA);
		List<Cover> b = system.getCoverList(keyB);

		assertNotSame(a, b);
	}

	@Test
	void getCoverListRejectsNullKey() {
		assertThrows(NullPointerException.class, () -> system.getCoverList(null));
	}

	@Test
	void emptyListInitiallyBehavesAsEmptyWithoutBackingList() {
		List<Cover> list = system.getCoverList(keyA);

		assertEquals(0, list.size());
		assertTrue(list.isEmpty());
		assertFalse(list.contains(c1));
		assertEquals(-1, list.indexOf(c1));
		assertEquals(-1, list.lastIndexOf(c1));
		assertArrayEquals(new Object[0], list.toArray());
		assertTrue(list.containsAll(List.of()));
		assertFalse(system.map.containsKey(keyA));
	}

	@Test
	void addCreatesBackingList() {
		List<Cover> list = system.getCoverList(keyA);

		list.add(c1);

		assertEquals(List.of(c1), list);
		assertTrue(system.map.containsKey(keyA));
	}

	// ------------------------------------------------------------
	// Read methods
	// ------------------------------------------------------------

	@Test
	void sizeGetIsEmptyContainsIndexOfLastIndexOfWork() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c2);
		list.add(c1);

		assertEquals(3, list.size());
		assertFalse(list.isEmpty());

		assertSame(c1, list.get(0));
		assertSame(c2, list.get(1));
		assertSame(c1, list.get(2));

		assertTrue(list.contains(c1));
		assertTrue(list.contains(c2));
		assertFalse(list.contains(c3));

		assertEquals(0, list.indexOf(c1));
		assertEquals(1, list.indexOf(c2));
		assertEquals(2, list.lastIndexOf(c1));
		assertEquals(1, list.lastIndexOf(c2));
		assertEquals(-1, list.indexOf(c3));
		assertEquals(-1, list.lastIndexOf(c3));
	}

	@Test
	void getOnEmptyListThrows() {
		List<Cover> list = system.getCoverList(keyA);
		assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
	}

	@Test
	void toArrayWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c2);

		Object[] arr = list.toArray();
		assertArrayEquals(new Object[] { c1, c2 }, arr);
	}

	@Test
	void toArrayTypedWithSmallArrayWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c2);

		Cover[] arr = list.toArray(new Cover[0]);
		assertArrayEquals(new Cover[] { c1, c2 }, arr);
	}

	@Test
	void toArrayTypedWithLargeArrayNullTerminates() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c2);

		Cover[] arr = new Cover[] { c3, c4, c3, c4 };
		Cover[] out = list.toArray(arr);

		assertSame(arr, out);
		assertSame(c1, out[0]);
		assertSame(c2, out[1]);
		assertNull(out[2]);
	}

	@Test
	void containsAllWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c2);
		list.add(c3);

		assertTrue(list.containsAll(List.of(c1, c2)));
		assertFalse(list.containsAll(List.of(c1, c4)));
	}

	// ------------------------------------------------------------
	// Write methods
	// ------------------------------------------------------------

	@Test
	void addAtIndexWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c3);

		list.add(1, c2);

		assertEquals(List.of(c1, c2, c3), list);
	}

	@Test
	void addAtInvalidIndexThrows() {
		List<Cover> list = system.getCoverList(keyA);

		assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, c1));
		assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, c1));
	}

	@Test
	void addAllAppends() {
		List<Cover> list = system.getCoverList(keyA);

		boolean changed = list.addAll(List.of(c1, c2, c3));

		assertTrue(changed);
		assertEquals(List.of(c1, c2, c3), list);
	}

	@Test
	void addAllAtIndexWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c4);

		boolean changed = list.addAll(1, List.of(c2, c3));

		assertTrue(changed);
		assertEquals(List.of(c1, c2, c3, c4), list);
	}

	@Test
	void addAllEmptyReturnsFalse() {
		List<Cover> list = system.getCoverList(keyA);

		assertFalse(list.addAll(List.of()));
		assertFalse(list.addAll(0, List.of()));
		assertEquals(List.of(), list);
	}

	@Test
	void setReplacesAndReturnsOldValue() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c2);

		Cover old = list.set(1, c3);

		assertSame(c2, old);
		assertEquals(List.of(c1, c3), list);
	}

	@Test
	void removeByObjectWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c2);
		list.add(c1);

		assertTrue(list.remove(c1));
		assertEquals(List.of(c2, c1), list);
		assertFalse(list.remove(c3));
	}

	@Test
	void removeByIndexWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c2);
		list.add(c3);

		Cover removed = list.remove(1);

		assertSame(c2, removed);
		assertEquals(List.of(c1, c3), list);
	}

	@Test
	void removeLastElementDeletesBackingMapEntry() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);

		assertTrue(system.map.containsKey(keyA));

		Cover removed = list.remove(0);

		assertSame(c1, removed);
		assertTrue(list.isEmpty());
		assertFalse(system.map.containsKey(keyA));
	}

	@Test
	void removeOnEmptyThrows() {
		List<Cover> list = system.getCoverList(keyA);
		assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
	}

	@Test
	void removeAllWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c2);
		list.add(c1);
		list.add(c3);

		boolean changed = list.removeAll(List.of(c1, c4));

		assertTrue(changed);
		assertEquals(List.of(c2, c3), list);
	}

	@Test
	void retainAllWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c2);
		list.add(c3);

		boolean changed = list.retainAll(List.of(c1, c3));

		assertTrue(changed);
		assertEquals(List.of(c1, c3), list);
	}

	@Test
	void clearWorksAndDeletesBackingMapEntry() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c2);

		assertTrue(system.map.containsKey(keyA));

		list.clear();

		assertTrue(list.isEmpty());
		assertFalse(system.map.containsKey(keyA));
	}

	@Test
	void clearEmptyListDoesNothing() {
		List<Cover> list = system.getCoverList(keyA);
		list.clear();
		assertTrue(list.isEmpty());
	}

	// ------------------------------------------------------------
	// Iterator
	// ------------------------------------------------------------

	@Test
	void iteratorIteratesInOrder() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c2);
		list.add(c3);

		Iterator<Cover> it = list.iterator();

		assertTrue(it.hasNext());
		assertSame(c1, it.next());
		assertTrue(it.hasNext());
		assertSame(c2, it.next());
		assertTrue(it.hasNext());
		assertSame(c3, it.next());
		assertFalse(it.hasNext());
		assertThrows(java.util.NoSuchElementException.class, it::next);
	}

	@Test
	void listIteratorForwardBackwardWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c2);
		list.add(c3);

		ListIterator<Cover> it = list.listIterator();

		assertFalse(it.hasPrevious());
		assertEquals(0, it.nextIndex());
		assertEquals(-1, it.previousIndex());

		assertSame(c1, it.next());
		assertTrue(it.hasPrevious());
		assertEquals(1, it.nextIndex());
		assertEquals(0, it.previousIndex());

		assertSame(c2, it.next());
		assertSame(c2, it.previous());
		assertSame(c2, it.next());
		assertSame(c3, it.next());

		assertFalse(it.hasNext());
		assertThrows(java.util.NoSuchElementException.class, it::next);
	}

	@Test
	void listIteratorAtIndexWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c2);
		list.add(c3);

		ListIterator<Cover> it = list.listIterator(2);

		assertTrue(it.hasPrevious());
		assertTrue(it.hasNext());
		assertSame(c2, it.previous());
		assertSame(c2, it.next());
		assertSame(c3, it.next());
	}

	@Test
	void listIteratorInvalidIndexThrows() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);

		assertThrows(IndexOutOfBoundsException.class, () -> list.listIterator(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> list.listIterator(2));
	}

	@Test
	void listIteratorRemoveWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c2);
		list.add(c3);

		ListIterator<Cover> it = list.listIterator();
		assertSame(c1, it.next());

		it.remove();

		assertEquals(List.of(c2, c3), list);
		assertThrows(IllegalStateException.class, it::remove);
	}

	@Test
	void listIteratorSetWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c2);

		ListIterator<Cover> it = list.listIterator();
		assertSame(c1, it.next());

		it.set(c3);

		assertEquals(List.of(c3, c2), list);
	}

	@Test
	void listIteratorAddWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);
		list.add(c3);

		ListIterator<Cover> it = list.listIterator(1);
		it.add(c2);

		assertEquals(List.of(c1, c2, c3), list);
	}

	@Test
	void listIteratorSetWithoutTraversalThrows() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);

		ListIterator<Cover> it = list.listIterator();
		assertThrows(IllegalStateException.class, () -> it.set(c2));
	}

	@Test
	void listIteratorRemoveWithoutTraversalThrows() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(c1);

		ListIterator<Cover> it = list.listIterator();
		assertThrows(IllegalStateException.class, it::remove);
	}

	// ------------------------------------------------------------
	// subList
	// ------------------------------------------------------------

	@Test
	void subListReadWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.addAll(List.of(c1, c2, c3, c4));

		List<Cover> sub = list.subList(1, 3);

		assertEquals(2, sub.size());
		assertEquals(List.of(c2, c3), sub);
		assertSame(c2, sub.get(0));
		assertSame(c3, sub.get(1));
	}

	@Test
	void subListWriteReflectsInParent() {
		List<Cover> list = system.getCoverList(keyA);
		list.addAll(List.of(c1, c2, c3, c4));

		List<Cover> sub = list.subList(1, 3); // [c2, c3]

		sub.set(0, c4);
		assertEquals(List.of(c1, c4, c3, c4), list);

		sub.add(c2);
		assertEquals(List.of(c1, c4, c3, c2, c4), list);

		sub.remove(1);
		assertEquals(List.of(c1, c4, c2, c4), list);
	}

	@Test
	void subListClearWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.addAll(List.of(c1, c2, c3, c4));

		List<Cover> sub = list.subList(1, 3);
		sub.clear();

		assertEquals(List.of(c1, c4), list);
	}

	@Test
	void nestedSubListWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.addAll(List.of(c1, c2, c3, c4));

		List<Cover> sub1 = list.subList(1, 4); // [c2, c3, c4]
		List<Cover> sub2 = sub1.subList(1, 3); // [c3, c4]

		assertEquals(List.of(c3, c4), sub2);

		sub2.remove(0);

		assertEquals(List.of(c1, c2, c4), list);
		assertEquals(List.of(c2, c4), sub1);
		assertEquals(List.of(c4), sub2);
	}

	@Test
	void subListInvalidBoundsThrow() {
		List<Cover> list = system.getCoverList(keyA);
		list.addAll(List.of(c1, c2));

		assertThrows(IndexOutOfBoundsException.class, () -> list.subList(-1, 1));
		assertThrows(IndexOutOfBoundsException.class, () -> list.subList(0, 3));
		assertThrows(IndexOutOfBoundsException.class, () -> list.subList(2, 1));
	}

	@Test
	void subListCanBecomeEmptyWhenParentShrinks() {
		List<Cover> list = system.getCoverList(keyA);
		list.addAll(List.of(c1, c2, c3));

		List<Cover> sub = list.subList(2, 3); // [c3]
		assertEquals(List.of(c3), sub);

		list.remove(2);

		assertTrue(sub.isEmpty());
		assertEquals(0, sub.size());
	}

	// ------------------------------------------------------------
	// getHandler(...)
	// ------------------------------------------------------------

	@Test
	void getHandlerRejectsNullTarget() {
		assertThrows(NullPointerException.class, () -> system.getHandler(null, null, "id"));
	}

	@Test
	void getHandlerRejectsNullId() {
		assertThrows(NullPointerException.class, () -> system.getHandler(keyA, null, null));
	}

	@Test
	void getHandlerReturnsNullWhenNoBlockHandlerAndNoCovers() {
		Object result = system.getHandler(keyA, null, "missing-handler-id");
		assertNull(result);
	}

	@Test
	void getHandlerWrapsTargetThenSourceInCorrectOrder() {
		// Arrange a fake raw handler getter
		String handlerId = "test-handler";
		Object raw = "RAW";

		BlockEntry block = world.get(keyA.x(), keyA.y());
		BlockType type = block.itemType();

		HandlerAssignment assignment = new HandlerAssignment(handlerId, type);
		HandlerGetter previous = HandlerSystems.handlerGetters.put(assignment, (source, blk) -> raw);

		try {
			// target covers applied inner -> outer
			TestCover t1 = new TestCover("T1");
			TestCover t2 = new TestCover("T2");

			// source covers applied reversed
			TestCover s1 = new TestCover("S1");
			TestCover s2 = new TestCover("S2");

			system.getCoverList(keyA).addAll(List.of(t1, t2));
			system.getCoverList(keyB).addAll(List.of(s1, s2));

			Object result = system.getHandler(keyA, keyB, handlerId);

			assertEquals(
				"FROM[S1](FROM[S2](TO[T2](TO[T1](RAW))))",
				result
			);
		} finally {
			restoreGetter(assignment, previous);
		}
	}

	@Test
	void getHandlerUsesOnlyTargetCoversWhenSourceIsNull() {
		String handlerId = "test-handler";
		Object raw = "RAW";

		BlockEntry block = world.get(keyA.x(), keyA.y());
		BlockType type = block.itemType();

		HandlerAssignment assignment = new HandlerAssignment(handlerId, type);
		HandlerGetter previous = HandlerSystems.handlerGetters.put(assignment, (source, blk) -> raw);

		try {
			system.getCoverList(keyA).addAll(List.of(
				new TestCover("T1"),
				new TestCover("T2")
			));

			Object result = system.getHandler(keyA, null, handlerId);

			assertEquals("TO[T2](TO[T1](RAW))", result);
		} finally {
			restoreGetter(assignment, previous);
		}
	}

	@Test
	void getHandlerUsesSourceReversedOrder() {
		String handlerId = "test-handler";
		Object raw = "RAW";

		BlockEntry block = world.get(keyA.x(), keyA.y());
		BlockType type = block.itemType();

		HandlerAssignment assignment = new HandlerAssignment(handlerId, type);
		HandlerGetter previous = HandlerSystems.handlerGetters.put(assignment, (source, blk) -> raw);

		try {
			system.getCoverList(keyB).addAll(List.of(
				new TestCover("S1"),
				new TestCover("S2"),
				new TestCover("S3")
			));

			Object result = system.getHandler(keyA, keyB, handlerId);

			assertEquals("FROM[S1](FROM[S2](FROM[S3](RAW)))", result);
		} finally {
			restoreGetter(assignment, previous);
		}
	}

	// ------------------------------------------------------------
	// Helpers
	// ------------------------------------------------------------

	/**
	 * Adjust this helper to match your actual HandlerKey constructor/factory.
	 */
	private static HandlerKey key(int x, int y) {
		return new HandlerKey(x, y, Side.U);
	}

	private static void restoreGetter(HandlerAssignment assignment, HandlerGetter previous) {
		if (previous == null) {
			HandlerSystems.handlerGetters.remove(assignment);
		} else {
			HandlerSystems.handlerGetters.put(assignment, previous);
		}
	}

	/**
	 * Minimal fake cover for verifying wrapping order.
	 */
	private static final class TestCover extends Item implements Cover {
		private TestCover(String name) {
			super(name);
		}

		@Override
		public Object getHandlerFromBlock(String handler, HandlerKey key, Object innerHandler) {
			return "FROM[" + id + "](" + innerHandler + ")";
		}

		@Override
		public Object getHandlerToBlock(String handler, HandlerKey key, Object innerHandler) {
			return "TO[" + id + "](" + innerHandler + ")";
		}
	}
}