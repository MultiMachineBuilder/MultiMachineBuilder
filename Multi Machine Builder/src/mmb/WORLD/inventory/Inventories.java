/**
 * 
 */
package mmb.WORLD.inventory;

import javax.annotation.Nullable;

import mmb.WORLD.item.ItemType;

/**
 * @author oskar
 *
 */
public class Inventories {
	private Inventories(){};
	
	public static void transfer(Inventory src, Inventory tgt, ItemEntry item, int amount) {
		transfer(src.get(item), tgt, amount);
	}
	public static void transfer(@Nullable ItemRecord src, Inventory tgt, int amount) {
		if(src == null) return; //not selected
		if(!src.canExtract()) return; //unable to extract from source
		if(!tgt.canInsert()) return; //unable to insert to target
		
		ItemRecord to = tgt.get(src.item());
		
		int amount0 = Math.min(src.amount(), amount);
		
		int amount1 = to.insert(amount0);
		src.extract(amount1);
	}
	public static void transfer(@Nullable ItemRecord src, Inventory tgt) {
		transfer(src, tgt, Integer.MAX_VALUE);
	}
	public static void transfer(Inventory src, Inventory tgt, ItemEntry item) {
		transfer(src.get(item), tgt);
	}
	/**
	 * Transfer entire source inventory to the target inventory
	 * @param src source
	 * @param tgt target
	 */
	public static void transfer(Inventory src, Inventory tgt) {
		for(ItemRecord rec: src) {
			transfer(rec, tgt, Integer.MAX_VALUE);
		}
	}
}
