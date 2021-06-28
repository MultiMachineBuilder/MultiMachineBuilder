/**
 * 
 */
package mmb.testing;

import javax.annotation.Nonnull;

import mmb.ERRORS.UndeclarableThrower;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.storage.SimpleInventory;
import mmb.WORLD.item.Item;
import mmb.WORLD.items.NoSuchItemEntry;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class TestInventory {
	private static final Debugger debug = new Debugger("TEST INVENTORIES");
	
	static Item itemA = new Item();
	static Item itemB = new Item();
	
	public static void main(String[] args) {
		itemA.register("A");
		itemB.register("B");
		
		test(SimpleInventory.class);
	}
	
	@SuppressWarnings("null")
	private static void test(Class<? extends Inventory> invcls) {
		
		int inserted;
		Inventory inv;
		
		//Do not accept NoSuchItemEntry
		inv = create(invcls);
		inserted = inv.insert(NoSuchItemEntry.INSTANCE, 1);
		if(inserted == 1) throw new Error("Accepts NoSuchItemEntry");
		
		inv = create(invcls);
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
	
	@SuppressWarnings("null")
	@Nonnull private static <T> T create(Class<? extends T> cls) {
		try {
			return cls.newInstance();
		} catch (Exception e) {
			debug.pstm(e, "The class does not work");
			UndeclarableThrower.shoot(e);
			//NOSONAR unreachable, but required
			return null;
		}
	}
}
