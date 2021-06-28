/**
 * 
 */
package mmb.testing;

import org.junit.jupiter.api.Assertions;

import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.inventory.storage.SingleStackedInventory;
import mmb.WORLD.item.Item;

/**
 * @author oskar
 *
 */
class TestInventoryIO {
	public static void main(String[] args) {
		//Test item
		Item test = new Item();
		test.setID("test");
		test.setVolume(0.4);
		
		//Test stacked inventory
		SingleStackedInventory testinv = new SingleStackedInventory();
		InventoryWriter writer = testinv.createWriter();
		int write = writer.write(test, 2);
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
		
		prior.write(test, 7);
		Assertions.assertEquals(5, testinvA.itemCount());
		Assertions.assertEquals(2, testinvB.itemCount());
	}
}
