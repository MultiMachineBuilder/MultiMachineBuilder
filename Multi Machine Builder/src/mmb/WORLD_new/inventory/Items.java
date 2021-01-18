/**
 * 
 */
package mmb.WORLD_new.inventory;

import java.util.HashMap;

import mmb.WORLD_new.block.BlockType;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Items {
	private static Debugger debug = new Debugger("ITEMS");
	public static HashMap<String, ItemType> items = new HashMap<String, ItemType>();

	/**
	 * @param typ
	 */
	public static void remove(ItemType typ) {
		remove(typ.id);
	}
	/**
	 * @param o
	 * @return
	 */
	public static ItemType remove(String o) {
		debug.printl("Removing "+o);
		return items.remove(o);
	}
}
