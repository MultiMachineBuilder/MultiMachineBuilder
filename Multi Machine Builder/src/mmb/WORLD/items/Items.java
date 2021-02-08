/**
 * 
 */
package mmb.WORLD.items;

import mmb.COLLECTIONS.HashSelfSet;
import mmb.COLLECTIONS.SelfSet;
import mmb.WORLD.item.ItemType;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Items {
	private static Debugger debug = new Debugger("ITEMS");
	public static final SelfSet<String, ItemType> items = new HashSelfSet<String, ItemType>();

	/**
	 * @param typ
	 */
	public static void remove(ItemType typ) {
		remove(typ.id);
	}
	/**
	 * @param o
	 */
	public static void remove(String o) {
		debug.printl("Removing "+o);
		items.remove(o);
	}
	
	public static void register(ItemType t) {
		items.add(t);
	}

}
