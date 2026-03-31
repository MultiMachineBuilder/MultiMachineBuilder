package mmb.handler;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mmb.covers.Cover;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.block.Blocks;
import mmb.engine.item.Item;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.world.World;
import mmbtest.StandardTestReferences;

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

	private static TestCover t1;
	private static TestCover t2;
	private static TestCover s1;
	private static TestCover s2;

	@BeforeAll
	static void staticSetup() {
		t1 = new TestCover("t1");
		t2 = new TestCover("t2");
		s1 = new TestCover("s1");
		s2 = new TestCover("s2");
	}
	
	@BeforeEach
	void setUp() {
		world = new World(3, 3, 0, 0);
		system = new HandlerSystem(world);

		keyA = key(1, 1);
		keyB = key(2, 2);
		
		world.set(StandardTestReferences.block, keyA.x(), keyA.y());
		world.set(Blocks.grass, keyB.x(), keyB.y());
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
		assertFalse(list.contains(t1));
		assertEquals(-1, list.indexOf(t1));
		assertEquals(-1, list.lastIndexOf(t1));
		assertArrayEquals(new Object[0], list.toArray());
		assertTrue(list.containsAll(List.of()));
		assertFalse(system.map.containsKey(keyA));
	}

	@Test
	void addCreatesBackingList() {
		List<Cover> list = system.getCoverList(keyA);

		list.add(t1);

		assertEquals(List.of(t1), list);
		assertTrue(system.map.containsKey(keyA));
	}

	// ------------------------------------------------------------
	// Read methods
	// ------------------------------------------------------------

	@Test
	void sizeGetIsEmptyContainsIndexOfLastIndexOfWork() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);
		list.add(t2);
		list.add(t1);

		assertEquals(3, list.size());
		assertFalse(list.isEmpty());

		assertSame(t1, list.get(0));
		assertSame(t2, list.get(1));
		assertSame(t1, list.get(2));

		assertTrue(list.contains(t1));
		assertTrue(list.contains(t2));
		assertFalse(list.contains(s1));

		assertEquals(0, list.indexOf(t1));
		assertEquals(1, list.indexOf(t2));
		assertEquals(2, list.lastIndexOf(t1));
		assertEquals(1, list.lastIndexOf(t2));
		assertEquals(-1, list.indexOf(s1));
		assertEquals(-1, list.lastIndexOf(s1));
	}

	@Test
	void getOnEmptyListThrows() {
		List<Cover> list = system.getCoverList(keyA);
		assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
	}

	@Test
	void toArrayWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);
		list.add(t2);

		Object[] arr = list.toArray();
		assertArrayEquals(new Object[] { t1, t2 }, arr);
	}

	@Test
	void toArrayTypedWithSmallArrayWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);
		list.add(t2);

		Cover[] arr = list.toArray(new Cover[0]);
		assertArrayEquals(new Cover[] { t1, t2 }, arr);
	}

	@Test
	void toArrayTypedWithLargeArrayNullTerminates() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);
		list.add(t2);

		Cover[] arr = new Cover[] { s1, s2, s1, s2 };
		Cover[] out = list.toArray(arr);

		assertSame(arr, out);
		assertSame(t1, out[0]);
		assertSame(t2, out[1]);
		assertNull(out[2]);
	}

	@Test
	void containsAllWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);
		list.add(t2);
		list.add(s1);

		assertTrue(list.containsAll(List.of(t1, t2)));
		assertFalse(list.containsAll(List.of(t1, s2)));
	}

	// ------------------------------------------------------------
	// Write methods
	// ------------------------------------------------------------

	@Test
	void addAtIndexWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);
		list.add(s1);

		list.add(1, t2);

		assertEquals(List.of(t1, t2, s1), list);
	}

	@Test
	void addAtInvalidIndexThrows() {
		List<Cover> list = system.getCoverList(keyA);

		assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, t1));
		assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, t1));
	}

	@Test
	void addAllAppends() {
		List<Cover> list = system.getCoverList(keyA);

		boolean changed = list.addAll(List.of(t1, t2, s1));

		assertTrue(changed);
		assertEquals(List.of(t1, t2, s1), list);
	}

	@Test
	void addAllAtIndexWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);
		list.add(s2);

		boolean changed = list.addAll(1, List.of(t2, s1));

		assertTrue(changed);
		assertEquals(List.of(t1, t2, s1, s2), list);
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
		list.add(t1);
		list.add(t2);

		Cover old = list.set(1, s1);

		assertSame(t2, old);
		assertEquals(List.of(t1, s1), list);
	}

	@Test
	void removeByObjectWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);
		list.add(t2);
		list.add(t1);

		assertTrue(list.remove(t1));
		assertEquals(List.of(t2, t1), list);
		assertFalse(list.remove(s1));
	}

	@Test
	void removeByIndexWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);
		list.add(t2);
		list.add(s1);

		Cover removed = list.remove(1);

		assertSame(t2, removed);
		assertEquals(List.of(t1, s1), list);
	}

	@Test
	void removeLastElementDeletesBackingMapEntry() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);

		assertTrue(system.map.containsKey(keyA));

		Cover removed = list.remove(0);

		assertSame(t1, removed);
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
		list.add(t1);
		list.add(t2);
		list.add(t1);
		list.add(s1);

		boolean changed = list.removeAll(List.of(t1, s2));

		assertTrue(changed);
		assertEquals(List.of(t2, s1), list);
	}

	@Test
	void retainAllWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);
		list.add(t2);
		list.add(s1);

		boolean changed = list.retainAll(List.of(t1, s1));

		assertTrue(changed);
		assertEquals(List.of(t1, s1), list);
	}

	@Test
	void clearWorksAndDeletesBackingMapEntry() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);
		list.add(t2);

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
		list.add(t1);
		list.add(t2);
		list.add(s1);

		Iterator<Cover> it = list.iterator();

		assertTrue(it.hasNext());
		assertSame(t1, it.next());
		assertTrue(it.hasNext());
		assertSame(t2, it.next());
		assertTrue(it.hasNext());
		assertSame(s1, it.next());
		assertFalse(it.hasNext());
		assertThrows(java.util.NoSuchElementException.class, it::next);
	}

	@Test
	void listIteratorForwardBackwardWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);
		list.add(t2);
		list.add(s1);

		ListIterator<Cover> it = list.listIterator();

		assertFalse(it.hasPrevious());
		assertEquals(0, it.nextIndex());
		assertEquals(-1, it.previousIndex());

		assertSame(t1, it.next());
		assertTrue(it.hasPrevious());
		assertEquals(1, it.nextIndex());
		assertEquals(0, it.previousIndex());

		assertSame(t2, it.next());
		assertSame(t2, it.previous());
		assertSame(t2, it.next());
		assertSame(s1, it.next());

		assertFalse(it.hasNext());
		assertThrows(java.util.NoSuchElementException.class, it::next);
	}

	@Test
	void listIteratorAtIndexWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);
		list.add(t2);
		list.add(s1);

		ListIterator<Cover> it = list.listIterator(2);

		assertTrue(it.hasPrevious());
		assertTrue(it.hasNext());
		assertSame(t2, it.previous());
		assertSame(t2, it.next());
		assertSame(s1, it.next());
	}

	@Test
	void listIteratorInvalidIndexThrows() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);

		assertThrows(IndexOutOfBoundsException.class, () -> list.listIterator(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> list.listIterator(2));
	}

	@Test
	void listIteratorRemoveWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);
		list.add(t2);
		list.add(s1);

		ListIterator<Cover> it = list.listIterator();
		assertSame(t1, it.next());

		it.remove();

		assertEquals(List.of(t2, s1), list);
		assertThrows(IllegalStateException.class, it::remove);
	}

	@Test
	void listIteratorSetWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);
		list.add(t2);

		ListIterator<Cover> it = list.listIterator();
		assertSame(t1, it.next());

		it.set(s1);

		assertEquals(List.of(s1, t2), list);
	}

	@Test
	void listIteratorAddWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);
		list.add(s1);

		ListIterator<Cover> it = list.listIterator(1);
		it.add(t2);

		assertEquals(List.of(t1, t2, s1), list);
	}

	@Test
	void listIteratorSetWithoutTraversalThrows() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);

		ListIterator<Cover> it = list.listIterator();
		assertThrows(IllegalStateException.class, () -> it.set(t2));
	}

	@Test
	void listIteratorRemoveWithoutTraversalThrows() {
		List<Cover> list = system.getCoverList(keyA);
		list.add(t1);

		ListIterator<Cover> it = list.listIterator();
		assertThrows(IllegalStateException.class, it::remove);
	}

	// ------------------------------------------------------------
	// subList
	// ------------------------------------------------------------

	@Test
	void subListReadWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.addAll(List.of(t1, t2, s1, s2));

		List<Cover> sub = list.subList(1, 3);

		assertEquals(2, sub.size());
		assertEquals(List.of(t2, s1), sub);
		assertSame(t2, sub.get(0));
		assertSame(s1, sub.get(1));
	}

	@Test
	void subListWriteReflectsInParent() {
		List<Cover> list = system.getCoverList(keyA);
		list.addAll(List.of(t1, t2, s1, s2));

		List<Cover> sub = list.subList(1, 3); // [c2, c3]

		sub.set(0, s2);
		assertEquals(List.of(t1, s2, s1, s2), list);

		sub.add(t2);
		assertEquals(List.of(t1, s2, s1, t2, s2), list);

		sub.remove(1);
		assertEquals(List.of(t1, s2, t2, s2), list);
	}

	@Test
	void subListClearWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.addAll(List.of(t1, t2, s1, s2));

		List<Cover> sub = list.subList(1, 3);
		sub.clear();

		assertEquals(List.of(t1, s2), list);
	}

	@Test
	void nestedSubListWorks() {
		List<Cover> list = system.getCoverList(keyA);
		list.addAll(List.of(t1, t2, s1, s2));

		List<Cover> sub1 = list.subList(1, 4); // [c2, c3, c4]
		List<Cover> sub2 = sub1.subList(1, 3); // [c3, c4]

		assertEquals(List.of(s1, s2), sub2);

		sub2.remove(0);

		assertEquals(List.of(t1, t2, s2), list);
		assertEquals(List.of(t2, s2), sub1);
		assertEquals(List.of(s2), sub2);
	}

	@Test
	void subListInvalidBoundsThrow() {
		List<Cover> list = system.getCoverList(keyA);
		list.addAll(List.of(t1, t2));

		assertThrows(IndexOutOfBoundsException.class, () -> list.subList(-1, 1));
		assertThrows(IndexOutOfBoundsException.class, () -> list.subList(0, 3));
		assertThrows(IndexOutOfBoundsException.class, () -> list.subList(2, 1));
	}

	@Test
	void subListCanBecomeEmptyWhenParentShrinks() {
		List<Cover> list = system.getCoverList(keyA);
		list.addAll(List.of(t1, t2, s1));

		List<Cover> sub = list.subList(2, 3); // [c3]
		assertEquals(List.of(s1), sub);

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

			system.getCoverList(keyA).addAll(List.of(t1, t2));
			system.getCoverList(keyB).addAll(List.of(s1, s2));

			Object result = system.getHandler(keyA, keyB, handlerId);

			assertEquals(
				"FROM[s1](FROM[s2](TO[t2](TO[t1](RAW))))",
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
				t1, t2
			));

			Object result = system.getHandler(keyA, null, handlerId);

			assertEquals("TO[t2](TO[t1](RAW))", result);
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
				s1, s2
			));

			Object result = system.getHandler(keyA, keyB, handlerId);

			assertEquals("FROM[s1](FROM[s2](RAW))", result);
		} finally {
			restoreGetter(assignment, previous);
		}
	}
	
	@Test
	void getHandlerPassesCorrectSourceAndBlockToRawGetter() {
		String handlerId = "test-handler";
		BlockEntry expectedBlock = world.get(keyA.x(), keyA.y());
		BlockType type = expectedBlock.itemType();

		final HandlerKey[] capturedSource = new HandlerKey[1];
		final BlockEntry[] capturedBlock = new BlockEntry[1];

		HandlerAssignment assignment = new HandlerAssignment(handlerId, type);
		HandlerGetter previous = HandlerSystems.handlerGetters.put(assignment, (source, blk) -> {
			capturedSource[0] = source;
			capturedBlock[0] = blk;
			return "RAW";
		});

		try {
			Object result = system.getHandler(keyA, keyB, handlerId);

			assertEquals("RAW", result);
			assertSame(keyB, capturedSource[0], "Source key should be passed to raw getter");
			assertSame(expectedBlock, capturedBlock[0], "Target block from world should be passed to raw getter");
		} finally {
			restoreGetter(assignment, previous);
		}
	}

	@Test
	void getHandlerUsesCorrectAssignmentForTargetBlockType() {
		String handlerId = "test-handler";
		BlockEntry block = world.get(keyA.x(), keyA.y());
		BlockType type = block.itemType();

		HandlerAssignment correct = new HandlerAssignment(handlerId, type);
		HandlerAssignment wrongId = new HandlerAssignment("wrong-id", type);

		HandlerGetter prevCorrect = HandlerSystems.handlerGetters.put(correct, (source, blk) -> "CORRECT");
		HandlerGetter prevWrong = HandlerSystems.handlerGetters.put(wrongId, (source, blk) -> "WRONG");

		try {
			Object result = system.getHandler(keyA, null, handlerId);
			assertEquals("CORRECT", result);
		} finally {
			restoreGetter(correct, prevCorrect);
			restoreGetter(wrongId, prevWrong);
		}
	}

	@Test
	void getHandlerReturnsNullWhenNoGetterExistsForExistingBlock() {
		String handlerId = "definitely-not-registered";
		Object result = system.getHandler(keyA, null, handlerId);
		assertNull(result);
	}

	@Test
	void getHandlerStillAppliesTargetCoversWhenRawHandlerIsNull() {
		String handlerId = "missing-handler";

		system.getCoverList(keyA).addAll(List.of(
			t1, t2
		));

		Object result = system.getHandler(keyA, null, handlerId);

		assertEquals("TO[t2](TO[t1](null))", result);
	}

	@Test
	void getHandlerStillAppliesSourceCoversWhenRawHandlerIsNull() {
		String handlerId = "missing-handler";

		system.getCoverList(keyB).addAll(List.of(
			t1, t2
		));

		Object result = system.getHandler(keyA, keyB, handlerId);

		assertEquals("FROM[t1](FROM[t2](null))", result);
	}

	@Test
	void getHandlerStillAppliesBothTargetAndSourceCoversWhenRawHandlerIsNull() {
		String handlerId = "missing-handler";

		system.getCoverList(keyA).addAll(List.of(
			t1, t2
		));
		system.getCoverList(keyB).addAll(List.of(
			s1, s2
		));

		Object result = system.getHandler(keyA, keyB, handlerId);

		assertEquals(
			"FROM[s1](FROM[s2](TO[t2](TO[t1](null))))",
			result
		);
	}

	@Test
	void getHandlerWithNoCoversReturnsRawHandlerUnchanged() {
		String handlerId = "test-handler";
		Object raw = new Object();

		BlockEntry block = world.get(keyA.x(), keyA.y());
		BlockType type = block.itemType();

		HandlerAssignment assignment = new HandlerAssignment(handlerId, type);
		HandlerGetter previous = HandlerSystems.handlerGetters.put(assignment, (source, blk) -> raw);

		try {
			Object result = system.getHandler(keyA, null, handlerId);
			assertSame(raw, result);
		} finally {
			restoreGetter(assignment, previous);
		}
	}

	@Test
	void getHandlerPassesTargetKeyToTargetCovers() {
		String handlerId = "test-handler";
		Object raw = "RAW";

		BlockEntry block = world.get(keyA.x(), keyA.y());
		BlockType type = block.itemType();

		final HandlerKey[] capturedKey = new HandlerKey[1];

		HandlerAssignment assignment = new HandlerAssignment(handlerId, type);
		HandlerGetter previous = HandlerSystems.handlerGetters.put(assignment, (source, blk) -> raw);

		try {
			class CaptureTargetCover extends Item implements Cover {
				private CaptureTargetCover() {
					super("capture-target");
				}

				@Override
				public Object getHandlerFromBlock(String handler, HandlerKey key, Object innerHandler) {
					return innerHandler;
				}

				@Override
				public Object getHandlerToBlock(String handler, HandlerKey key, Object innerHandler) {
					capturedKey[0] = key;
					return innerHandler;
				}
			}

			Cover cover = new CaptureTargetCover();

			system.getCoverList(keyA).add(cover);

			Object result = system.getHandler(keyA, keyB, handlerId);

			assertEquals("RAW", result);
			assertSame(keyA, capturedKey[0], "Target cover should receive target key");
		} finally {
			restoreGetter(assignment, previous);
		}
	}

	@Test
	void getHandlerPassesTargetKeyToSourceCoversToo() {
		
		
		String handlerId = "test-handler";
		Object raw = "RAW";

		BlockEntry block = world.get(keyA.x(), keyA.y());
		BlockType type = block.itemType();

		final HandlerKey[] capturedKey = new HandlerKey[1];

		HandlerAssignment assignment = new HandlerAssignment(handlerId, type);
		HandlerGetter previous = HandlerSystems.handlerGetters.put(assignment, (source, blk) -> raw);

		try {
			class CaptureSourceCover extends Item implements Cover {
				private CaptureSourceCover() {
					super("capture-source");
				}

				@Override
				public Object getHandlerFromBlock(String handler, HandlerKey key, Object innerHandler) {
					capturedKey[0] = key;
					return innerHandler;
				}

				@Override
				public Object getHandlerToBlock(String handler, HandlerKey key, Object innerHandler) {
					return innerHandler;
				}
			}

			Cover cover = new CaptureSourceCover();

			system.getCoverList(keyB).add(cover);

			Object result = system.getHandler(keyA, keyB, handlerId);

			assertEquals("RAW", result);
			assertSame(keyA, capturedKey[0], "Source cover should receive target key (current implementation)");
		} finally {
			restoreGetter(assignment, previous);
		}
	}

	@Test
	void getHandlerPassesHandlerIdToCovers() {
		String handlerId = "special-handler-id";
		Object raw = "RAW";

		BlockEntry block = world.get(keyA.x(), keyA.y());
		BlockType type = block.itemType();

		final String[] capturedId = new String[1];

		HandlerAssignment assignment = new HandlerAssignment(handlerId, type);
		HandlerGetter previous = HandlerSystems.handlerGetters.put(assignment, (source, blk) -> raw);

		try {
			class CaptureIdCover extends Item implements Cover {
				private CaptureIdCover() {
					super("capture-id");
				}

				@Override
				public Object getHandlerFromBlock(String handler, HandlerKey key, Object innerHandler) {
					return innerHandler;
				}

				@Override
				public Object getHandlerToBlock(String handler, HandlerKey key, Object innerHandler) {
					capturedId[0] = handler;
					return innerHandler;
				}
			}

			Cover cover = new CaptureIdCover();

			system.getCoverList(keyA).add(cover);

			system.getHandler(keyA, null, handlerId);

			assertEquals(handlerId, capturedId[0]);
		} finally {
			restoreGetter(assignment, previous);
		}
	}

	@Test
	void getHandlerWithEmptyTargetAndSourceCoverListsStillReturnsRaw() {
		String handlerId = "test-handler";
		Object raw = "RAW";

		BlockEntry block = world.get(keyA.x(), keyA.y());
		BlockType type = block.itemType();

		HandlerAssignment assignment = new HandlerAssignment(handlerId, type);
		HandlerGetter previous = HandlerSystems.handlerGetters.put(assignment, (source, blk) -> raw);

		try {
			// Touch the cover lists, but keep them empty
			system.getCoverList(keyA);
			system.getCoverList(keyB);

			Object result = system.getHandler(keyA, keyB, handlerId);

			assertEquals("RAW", result);
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