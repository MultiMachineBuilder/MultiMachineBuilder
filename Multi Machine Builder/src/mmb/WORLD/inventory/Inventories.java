/**
 * 
 */
package mmb.WORLD.inventory;

import javax.annotation.Nullable;

import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 *
 */
public class Inventories {
	private Inventories(){}
	
	public static int transfer(Inventory src, Inventory tgt, ItemEntry item, int amount) {
		return transfer(src.get(item), tgt, amount);
	}
	public static int transfer(@Nullable ItemRecord src, Inventory tgt, int amount) {
		if(src == null) return 0; //not selected
		if(!src.canExtract()) return 0; //unable to extract from source
		if(!tgt.canInsert()) return 0; //unable to insert to target
		
		ItemRecord to = tgt.get(src.item());
		
		int amount0 = Math.min(src.amount(), amount);
		
		int amount1 = to.insert(amount0);
		src.extract(amount1);
		
		return amount1;
	}
	public static int transfer(@Nullable ItemRecord src, Inventory tgt) {
		return transfer(src, tgt, Integer.MAX_VALUE);
	}
	public static int transfer(Inventory src, Inventory tgt, ItemEntry item) {
		return transfer(src.get(item), tgt);
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
	public static boolean transferFirst(Inventory src, Inventory tgt) {
		for(ItemRecord rec: src) {
			int tf = transfer(rec, tgt, 1);
			if(tf == 1) return true;
		}
		return false;
	}
	public static int transferFirstStack(Inventory src, Inventory tgt) {
		for(ItemRecord rec: src) {
			int tf = transfer(rec, tgt, Integer.MAX_VALUE);
			if(tf > 0) return tf;
		}
		return 0;
	}

	
	public static int transfer(Inventory src, InventoryWriter tgt, ItemEntry item, int amount) {
		return transfer(src.get(item), tgt, amount);
	}
	public static int transfer(@Nullable ItemRecord src, InventoryWriter tgt, int amount) {
		if(src == null) return 0; //not selected
		if(!src.canExtract()) return 0; //unable to extract from source
				
		int amount0 = Math.min(src.amount(), amount);
		
		int amount1 = tgt.write(src.item(), amount0);
		src.extract(amount1);
		
		return amount1;
	}
	public static int transfer(@Nullable ItemRecord src, InventoryWriter tgt) {
		return transfer(src, tgt, Integer.MAX_VALUE);
	}
	public static int transfer(Inventory src, InventoryWriter tgt, ItemEntry item) {
		return transfer(src.get(item), tgt);
	}
	/**
	 * Transfer entire source inventory to the target inventory
	 * @param src source
	 * @param tgt target
	 */
	public static void transfer(Inventory src, InventoryWriter tgt) {
		for(ItemRecord rec: src) {
			transfer(rec, tgt, Integer.MAX_VALUE);
		}
	}
	public static boolean transferFirst(Inventory src, InventoryWriter tgt) {
		for(ItemRecord rec: src) {
			int tf = transfer(rec, tgt, 1);
			if(tf == 1) return true;
		}
		return false;
	}
	public static int transferFirstStack(Inventory src, InventoryWriter tgt) {
		for(ItemRecord rec: src) {
			int tf = transfer(rec, tgt, Integer.MAX_VALUE);
			if(tf > 0) return tf;
		}
		return 0;
	}
	
}
