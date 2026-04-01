package mmb.inventory2.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mmb.PropertyExtension;
import mmb.engine.block.Blocks;
import mmb.engine.item.Item;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemList;
import mmb.engine.recipe.SimpleItemList;
import mmb.inventory2.ItemEvent;
import mmb.inventory2.ItemListener;
import mmb.inventory2.storage.Inventory;
import mmb.rx.TestObserver;

class InventoryTest {

	private static Item iron = new Item("invtest.iron", PropertyExtension.setStorageVolume(1.0));
	private static Item copper = new Item("invtest.copper", PropertyExtension.setStorageVolume(0.5));
	private static Item gold = new Item("invtest.gold", PropertyExtension.setStorageVolume(2.0));

	private Inventory inv;
	private TestObserver<ItemEvent> observer;

	@BeforeEach
	void setUp() {
		inv = new Inventory(10.0, 3);
		observer = new TestObserver<>();
	}

	@Test
	void newInventoryStartsEmpty() {
		assertTrue(inv.isEmpty());
		assertEquals(0.0, inv.volume(), 1e-12);
		assertEquals(10.0, inv.capacity(), 1e-12);
		assertEquals(3, inv.maxSlots());
		assertTrue(inv.copyContents().isEmpty());
	}

	@Test
	void insertAddsItemsAndUpdatesVolume() {		
		int inserted = inv.insert(iron, 4);

		assertEquals(4, inserted);
		assertFalse(inv.isEmpty());
		assertEquals(4.0, inv.volume(), 1e-12);
		assertEquals(4, inv.copyContents().getInt(iron));
	}

	@Test
	void extractRemovesItemsAndUpdatesVolume() {
		inv.insert(iron, 5);

		int extracted = inv.extract(iron, 2);

		assertEquals(2, extracted);
		assertEquals(3, inv.copyContents().getInt(iron));
		assertEquals(3.0, inv.volume(), 1e-12);
	}

	@Test
	void extractingMoreThanStoredExtractsOnlyAvailableAmount() {
		inv.insert(iron, 3);

		int extracted = inv.extract(iron, 10);

		assertEquals(3, extracted);
		assertEquals(0, inv.copyContents().getInt(iron));
		assertFalse(inv.copyContents().containsKey(iron));
		assertEquals(0.0, inv.volume(), 1e-12);
		assertTrue(inv.isEmpty());
	}

	@Test
	void insertRespectsVolumeLimit() {
		int inserted = inv.insert(gold, 10); // each = 2.0, capacity = 10.0

		assertEquals(5, inserted);
		assertEquals(10.0, inv.volume(), 1e-12);
		assertEquals(5, inv.copyContents().getInt(gold));
	}

	@Test
	void insertableRemainMatchesImmediateInsert() {
		int remain = inv.insertableAmount(10, gold);
		int inserted = inv.insert(gold, 10);

		assertEquals(remain, inserted);
	}

	@Test
	void extractableRemainMatchesImmediateExtract() {
		inv.insert(iron, 7);

		int remain = inv.extractableAmount(10, iron);
		int extracted = inv.extract(iron, 10);

		assertEquals(remain, extracted);
	}

	@Test
	void insertRespectsMaxDistinctSlots() {
		assertEquals(1, inv.insert(iron, 1));
		assertEquals(1, inv.insert(copper, 1));
		assertEquals(1, inv.insert(gold, 1));
		assertEquals(0, inv.insert(Blocks.air, 1));
		
		assertFalse(inv.copyContents().containsKey(Blocks.air));
		assertEquals(3, inv.copyContents().size());
	}

	@Test
	void insertingExistingTypeStillWorksWhenSlotLimitReached() {
		assertEquals(1, inv.insert(iron, 1));
		assertEquals(1, inv.insert(copper, 1));
		assertEquals(1, inv.insert(gold, 1));

		assertEquals(5, inv.insert(iron, 5));
		assertEquals(6, inv.copyContents().getInt(iron));
	}

	@Test
	void zeroAmountInsertAndExtractDoNothing() {
		assertEquals(0, inv.insert(iron, 0));
		assertEquals(0, inv.extract(iron, 0));
		assertTrue(inv.isEmpty());
		assertEquals(0.0, inv.volume(), 1e-12);
	}

	@Test
	void extractAbsentItemReturnsZero() {
		assertEquals(0, inv.extract(iron, 5));
		assertTrue(inv.isEmpty());
	}

	@Test
	void setMaxVolumeAcceptsTooSmallValue() {
		inv.insert(iron, 5); // volume = 5

		assertDoesNotThrow(() -> inv.setMaxVolume(4.0));
	}

	@Test
	void setMaxVolumeAllowsValidResize() {
		inv.insert(iron, 5);

		inv.setMaxVolume(6.0);

		assertEquals(6.0, inv.getMaxVolume(), 1e-12);
		assertEquals(6.0, inv.capacity(), 1e-12);
	}

	@Test
	void setMaxSlotsAcceptsTooSmallValue() {
		inv.insert(iron, 1);
		inv.insert(copper, 1);

		assertDoesNotThrow(() -> inv.setMaxSlots(1));
	}

	@Test
	void setMaxSlotsAllowsValidResize() {
		inv.insert(iron, 1);
		inv.insert(copper, 1);

		inv.setMaxSlots(2);

		assertEquals(2, inv.getMaxSlots());
		assertEquals(2, inv.maxSlots());
	}

	@Test
	void bulkInsertAddsAllItemsInCorrectProportions() {
		ItemList recipe = new SimpleItemList()
			.add(iron, 2)
			.add(copper, 3);

		int insertedUnits = inv.bulkInsert(recipe, 2);

		assertEquals(2, insertedUnits);
		assertEquals(4, inv.copyContents().getInt(iron));
		assertEquals(6, inv.copyContents().getInt(copper));
		assertEquals(4.0 + 3.0, inv.volume(), 1e-12);
	}

	@Test
	void bulkInsertRespectsVolumeLimit() {
		ItemList recipe = new SimpleItemList()
			.add(iron, 2)   // 2.0
			.add(gold, 1);  // 2.0
		                     // total = 4.0 per unit

		int insertedUnits = inv.bulkInsert(recipe, 10);

		assertEquals(2, insertedUnits); // 8 volume used, 3 would need 12
		assertEquals(4, inv.copyContents().getInt(iron));
		assertEquals(2, inv.copyContents().getInt(gold));
		assertEquals(8.0, inv.volume(), 1e-12);
	}

	@Test
	void bulkExtractRemovesOnlyWholeUnits() {
		ItemList recipe = new SimpleItemList()
			.add(iron, 2)
			.add(copper, 1);

		inv.insert(iron, 5);
		inv.insert(copper, 2);

		int extractedUnits = inv.bulkExtract(recipe, 10);

		assertEquals(2, extractedUnits);
		assertEquals(1, inv.copyContents().getInt(iron));
		assertFalse(inv.copyContents().containsKey(copper));
	}

	@Test
	void insertableRemainBulkMatchesImmediateBulkInsert() {
		ItemList recipe = new SimpleItemList()
			.add(iron, 2)
			.add(copper, 1);

		int remain = inv.insertableAmountBulk(10, recipe);
		int inserted = inv.bulkInsert(recipe, 10);

		assertEquals(remain, inserted);
	}

	@Test
	void extractableRemainBulkMatchesImmediateBulkExtract() {
		ItemList recipe = new SimpleItemList()
			.add(iron, 2)
			.add(copper, 1);

		inv.insert(iron, 7);
		inv.insert(copper, 3);

		int remain = inv.extractableAmountBulk(10, recipe);
		int extracted = inv.bulkExtract(recipe, 10);

		assertEquals(remain, extracted);
	}

	@Test
	void mutationEmitsItemEvent() {
		final ItemEvent[] captured = new ItemEvent[1];
		
		ItemListener listener = event -> captured[0] = event;

		var disposable = inv.addItemListener(null, null, listener);

		inv.insert(iron, 3);

		assertNotNull(captured[0]);
		assertEquals(inv, captured[0].inventory());
		assertEquals(iron, captured[0].item());
		assertEquals(0, captured[0].before());
		assertEquals(3, captured[0].after());

		disposable.dispose();
	}
	
	@Test
	void setListenerIgnoresOtherItemAndEventEquals() {
		final List<ItemEvent> events = new ArrayList<>();
		ItemListener listener = events::add;
		var disposable = inv.addItemListener(Set.of(iron), null, listener);

		inv.insert(iron, 3);
		inv.insert(copper, 4);
		
		assertEquals(List.of(new ItemEvent(inv, iron, 0, 3)), events);

		disposable.dispose();
	}
	
	@Test
	void listenerDoesNotPassExceptionsToOutside() {
		ItemListener listener = event -> {throw new RuntimeException();};
		var disposable = inv.addItemListener(null, null, listener);

		assertDoesNotThrow(() -> inv.insert(copper, 4));

		disposable.dispose();
	}
}