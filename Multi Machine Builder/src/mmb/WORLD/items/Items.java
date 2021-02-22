/**
 * 
 */
package mmb.WORLD.items;

import mmb.COLLECTIONS.HashSelfSet;
import mmb.COLLECTIONS.SelfSet;
import mmb.WORLD.item.Item;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Items {
	private static Debugger debug = new Debugger("ITEMS");
	public static final SelfSet<String, Item> items = new HashSelfSet<String, Item>();

	/**
	 * @param typ
	 */
	public static void remove(Item typ) {
		remove(typ.id);
	}
	/**
	 * @param o
	 */
	public static void remove(String o) {
		debug.printl("Removing "+o);
		items.remove(o);
	}
	
	public static void register(Item t) {
		items.add(t);
	}

}
