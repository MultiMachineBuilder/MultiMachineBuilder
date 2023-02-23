/**
 * 
 */
package mmbtest.testing;

import org.junit.jupiter.api.Test;

import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.storage.SingleStackedInventory;
import mmb.engine.item.Item;

import org.junit.jupiter.api.Assertions;

/**
 * @author oskar
 *
 */
class TestInventoryIO {
	@Test
	public void test() {
		//Test item
		Item test = new Item();
		test.setID("test");
		test.setVolume(0.4);
		
		//Test stacked inventory
		SingleStackedInventory testinv = new SingleStackedInventory();
		InventoryWriter writer = testinv.createWriter();
		int write = writer.insert(test, 2);
		Assertions.assertEquals(2, testinv.itemCount());
		Assertions.assertEquals(2, write);
		
		//Test priority
		SingleStackedInventory testinvA = new SingleStackedInventory();
		SingleStackedInventory testinvB = new SingleStackedInventory();
		InventoryWriter inA = testinvA.createWriter();
		InventoryWriter inB = testinvB.createWriter();
		InventoryWriter prior = new InventoryWriter.Priority(inA, inB);
		testinvA.setCapacity(2.01);
		testinvB.setCapacity(2.01);
		
		prior.insert(test, 7);
		Assertions.assertEquals(5, testinvA.itemCount());
		Assertions.assertEquals(2, testinvB.itemCount());
	}
}
