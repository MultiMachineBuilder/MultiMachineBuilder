/**
 * 
 */
package mmb.testing;

import javax.annotation.Nonnull;

import mmb.ERRORS.UndeclarableThrower;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.SimpleInventory;
import mmb.WORLD.item.Item;
import mmb.WORLD.item.NoSuchItemEntry;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class TestInventory {
	private static final Debugger debug = new Debugger("TEST INVENTORIES");
	
	public static void main(String[] args) {
		Item itemA = new Item();
		itemA.register("A");
		
		Item itemB = new Item();
		itemA.register("B");
		
		test(SimpleInventory.class);
	}
	
	@SuppressWarnings("null")
	private static void test(Class<? extends Inventory> invcls) {
		//Do not accept NoSuchItemEntry
		int inserted;
		Inventory inv;
		
		inv = create(invcls);
		inserted = inv.insert(NoSuchItemEntry.INSTANCE, 1);
		if(inserted == 1) throw new Error("Accepts NoSuchItemEntry");
	}
	
	@SuppressWarnings("null")
	@Nonnull private static <T> T create(Class<? extends T> cls) {
		try {
			return cls.newInstance();
		} catch (Exception e) {
			debug.pstm(e, "The class does not work");
			UndeclarableThrower.shoot(e);
			return null; //unreachable, but required
		}
	}
}
