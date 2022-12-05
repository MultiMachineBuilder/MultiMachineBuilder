/**
 * 
 */
package mmb.testing;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;

import mmbeng.debug.Debugger;
import mmbeng.inv.Inventory;
import mmbeng.inv.storage.SimpleInventory;
import mmbeng.item.Item;
import mmbeng.json.JsonTool;

/**
 * @author oskar
 *
 */
public class TestInventorySerial {
	@Nonnull static Item A, B;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Prepare the items
		A = new Item();
		A.register("A");
		B = new Item();
		B.register("B");
		//Prepare the inventory
		SimpleInventory inv = new SimpleInventory();
		inv.setCapacity(1);
		//Populate the inventory
		inv.insert(A, 3);
		inv.insert(B, 2);
		checkinv(inv);
		//Serialize
		JsonNode node = inv.save();
		String jsonData = node.toPrettyString();
		//Print out the data
		Debugger debug = new Debugger("TEST INVENTORY SAVE");
		debug.printl(jsonData);
		//Read data from string
		try { 
			JsonNode data = JsonTool.parse(jsonData);
			SimpleInventory inv2 = new SimpleInventory();
			inv2.load(data);
			checkinv(inv2);
		} catch (Exception e) {
			fail("JSON loading exception", e);
		}
	}
	public static void fail(String msg) {
		throw new Error(msg);
	}
	public static void fail(String msg, Throwable t) {
		throw new Error(msg, t);
	}
	/**
	 * Checks if inventory is of specific content:
	 * 3*A
	 * 2*B
	 * 1m^3 capacity
	 * @param inv
	 */
	public static void checkinv(Inventory inv) {
		int amtA = inv.get(A).amount();
		if(amtA != 3) {
			fail("Expected 3 pieces of A, got "+amtA);
		}
		int amtB = inv.get(B).amount();
		if(amtB != 2) {
			fail("Expected 2 pieces of B, got "+amtB);
		}
		double volume = inv.capacity();
		if(volume != 1) {
			fail("Expected 1 m^3 capacity, got "+volume);
		}
	}
}
