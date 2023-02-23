/**
 * 
 */
package mmbtest.testing;

import java.util.function.Supplier;

import org.junit.Test;

import mmb.engine.debug.Debugger;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.storage.SimpleInventory;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.Item;

/**
 * @author oskar
 *
 */
public class TestInventory {
	private static final Debugger debug = new Debugger("TEST INVENTORIES");
	
	static Item itemA = new Item();
	static Item itemB = new Item();
	
	@Test
	public void test() {
		itemA.register("A");
		itemB.register("B");
		
		test(SimpleInventory::new);
		test(SingleItemInventory::new);
	}
	
	@SuppressWarnings("null")
	private static void test(Supplier<Inventory> invcls) {
		
		int inserted;
		Inventory inv;
				
		inv = invcls.get();
		//Insert
		final int amount = 3;
		
		//Check if insertion gives correct values
		int npiecesAinsert = inv.insert(itemA, amount);
		int npiecesAactual = inv.get(itemA).amount();
		if(npiecesAinsert != npiecesAactual)
			throw new Error(
				"The number of reported pieces is inconsistent. "
				+ "Inserted: " + npiecesAinsert
				+ ", actual: " + npiecesAactual);
		debug.printl(npiecesAactual + "pieces of A");
		
		int npiecesBinsert = inv.insert(itemB, amount);
		int npiecesBactual = inv.get(itemB).amount();
		if(npiecesBinsert != npiecesBactual)
			throw new Error(
				"The number of reported pieces is inconsistent. "
				+ "Inserted: " + npiecesBinsert
				+ ", actual: " + npiecesBactual);
		debug.printl(npiecesBactual + "pieces of B");
		
		//Withdraw entire inventory
		int npiecesAwithdraw = inv.extract(itemA, npiecesAactual);
		if(npiecesAwithdraw != npiecesAactual)
			throw new Error(
				"The inventory failed to properly extract items. "
				+ "Extracted: " + npiecesAwithdraw
				+ ", expected: " + npiecesAactual);
		
		int npiecesBwithdraw = inv.extract(itemB, npiecesBactual);
		if(npiecesBwithdraw != npiecesBactual)
			throw new Error(
				"The inventory failed to properly extract items. "
				+ "Extracted: " + npiecesBwithdraw
				+ ", expected: " + npiecesBactual);
		
		//No items can be left
		int npiecesAafter = inv.get(itemA).amount();
		if(npiecesAafter != 0) throw new Error(npiecesAafter + " pieces are left, expected 0");
		int npiecesBafter = inv.get(itemB).amount();
		if(npiecesBafter != 0) throw new Error(npiecesBafter + " pieces are left, expected 0");
		
		//Test writer/reader
	}
}
