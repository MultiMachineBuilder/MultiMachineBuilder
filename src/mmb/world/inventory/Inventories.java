/**
 * 
 */
package mmb.world.inventory;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import mmb.world.inventory.io.InventoryReader;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.item.ItemEntry;

/**
 * A set of utilities for working with inventories. <br>
 * Major method families:
 * <ul>
 * 	<li>transferAll - transfer all items. These have an optional callback</li>
 *  <li>transferFilter - transfer items which meet given condition. These have an optional callback</li>
 * 
 *  <li>transferFirstStack - transfer the first stack. These return an item stack or null</li>
 *  <li>transferFirstItem - transfer the first item. These return an item or null</li>
 *  
 *  <li>transferStack - transfer the specific stack whole or partial. These return an integer</li>
 *  <li>transferItem - transfer the specific item by one. These return a boolean</li>
 *  
 *  <li>transferRecord - transfer the specific item record. These return an integer</li>
 *  <li>transferRecordItem - transfer the specific item record by one. These return an integer</li>
 * </ul>
 * @author oskar
 */
public class Inventories {
	private Inventories(){}
	
	//From an item record
	/**
	 * Transfers a portion of an item record to an inventory
	 * @param src source inventory
	 * @param tgt target inventory
	 * @param amount number of items to transfer
	 * @return number of items transfered
	 */
	public static int transferRecord(@Nullable ItemRecord src, Inventory tgt, int amount) {
		if(!tgt.canInsert()) return 0; //unable to insert to target
		return transferRecord(src, tgt.createWriter(), amount);
	}
	/**
	 * Transfers an entire item record to an inventory writer
	 * @param src source inventory
	 * @param tgt target inventory
	 * @return number of items transfered
	 */
	public static int transferRecord(@Nullable ItemRecord src, InventoryWriter tgt) {
		return transferRecord(src, tgt, Integer.MAX_VALUE);
	}
	/**
	 * Transfers an entire item record to an inventory
	 * @param src source inventory
	 * @param tgt target inventory
	 * @return number of items transfered
	 */
	public static int transferRecord(@Nullable ItemRecord src, Inventory tgt) {
		if(!tgt.canInsert()) return 0;
		return transferRecord(src, tgt, Integer.MAX_VALUE);
	}
	/**
	 * Transfers a portion of an item record to an inventory writer
	 * @param src source inventory
	 * @param tgt target inventory
	 * @param amount number of items to transfer
	 * @return number of items transfered
	 */
	public static int transferRecord(@Nullable ItemRecord src, InventoryWriter tgt, int amount) {
		if(src == null) return 0; //not selected
		if(!src.canExtract()) return 0; //unable to extract from source
		int amount0 = Math.min(src.amount(), amount);
		
		int amount1 = tgt.write(src.item(), amount0);
		src.extract(amount1);
		
		return amount1;
	}
	
	//Entire inventory
	/**
	 * Transfer entire source inventory to the target inventory
	 * @param src source inventory
	 * @param tgt target target inventory
	 */
	public static void transfer(Inventory src, Inventory tgt) {
		for(ItemRecord rec: src) {
			transferRecord(rec, tgt, Integer.MAX_VALUE);
		}
	}
	/**
	 * Transfer entire source inventory to the target inventory
	 * @param src source inventory
	 * @param tgt inventory writer
	 */
	public static void transfer(Inventory src, InventoryWriter tgt) {
		for(ItemRecord rec: src) {
			transferRecord(rec, tgt, Integer.MAX_VALUE);
		}
	}
	
	//First stack
	/**
	 * Transfers a portion of an item record to an inventory writer
	 * @param src source inventory
	 * @param tgt tagret inventory
	 * @param amount number of items to transfer
	 * @return items transfered, or null if none found
	 */
	@Nullable public static ItemStack transferFirstStack(InventoryReader src, InventoryWriter tgt, int amount) {
		if(!src.hasCurrent()) return null;
		ItemEntry item = src.currentItem();
		if(item == null) return null;
		int amount0 = src.toBeExtracted(amount);
		int amount1 = tgt.write(item, amount0);
		src.extract(amount1);
		return new ItemStack(item, amount1);
	}
	/**
	 * Transfers a first item from inventory reader to an inventory
	 * @param src inventory reader
	 * @param tgt target inventory
	 * @return were items transferred
	 */
	public static ItemEntry transferFirst(InventoryReader src, Inventory tgt) {
		return transferFirst(src, tgt.createWriter());
	}
	/**
	 * Transfers a first item from inventory reader to an inventory writer
	 * @param src inventory reader
	 * @param tgt inventory writer
	 * @return were items transferred
	 */
	public static ItemEntry transferFirst(InventoryReader src, InventoryWriter tgt) {
		ItemStack stk =  transferFirstStack(src, tgt, 1);
		if(stk == null || stk.amount == 0) return null;
		return stk.item;
	}
	/**
	 * Transfers a first stack from an inventory
	 * @param src source inventory
	 * @param tgt inventory writer
	 * @return transferred items, or null if inventory is empty
	 */
	@Nullable public static ItemStack transferFirstStack(Inventory src, Inventory tgt) {
		return transferFirstStack(src, tgt.createWriter());
	}
	
	//From an inventory
	
	/**
	 * Transfer source inventory to the target inventory, where predicate accepts it
	 * @param src source inventory
	 * @param tgt target target inventory
	 * @param filter item filter
	 */
	public static void transfer(Inventory src, Inventory tgt, Predicate<ItemRecord> filter) {
		for(ItemRecord rec: src) {
			if(filter.test(rec)) transferRecord(rec, tgt, Integer.MAX_VALUE);
		}
	}
	/**
	 * Transfers first item from an inventory to other inventory
	 * @param src source inventory
	 * @param tgt target inventory
	 * @return item transferred, or null if none
	 */
	@Nullable public static ItemEntry transferFirst(Inventory src, Inventory tgt) {
		return transferFirst(src, tgt.createWriter());
	}
		
	/**
	 * Transfers first item from an inventory to an inventory writer
	 * @param src source inventory
	 * @param tgt inventory writer
	 * @return item transferred, or null if none
	 */
	@Nullable public static ItemEntry transferFirst(Inventory src, InventoryWriter tgt) {
		for(ItemRecord rec: src) {
			int tf = transferRecord(rec, tgt, 1);
			if(tf == 1) return rec.item();
		}
		return null;
	}
	/**
	 * Transfers a first stack from an inventory
	 * @param src source inventory
	 * @param tgt inventory writer
	 * @return transferred items, or null if inventory is empty
	 */
	@Nullable public static ItemStack transferFirstStack(Inventory src, InventoryWriter tgt) {
		for(ItemRecord rec: src) {
			int tf = transferRecord(rec, tgt, Integer.MAX_VALUE);
			if(tf > 0) return new ItemStack(rec.item(), tf);
		}
		return null;
	}	
			
	//Specific stacks
	/** 
	 * Transfer a specified amount of given item into other inventory
	 * @param src source inventory
	 * @param tgt target inventory
	 * @param item item to transfer
	 * @param amount number of items to transfer
	 * @return number of items transferred
	 */
	public static int transferStack(Inventory src, Inventory tgt, ItemEntry item, int amount) {
		return transferRecord(src.get(item), tgt, amount);
	}
	/** 
	 * Transfer all of given item into other inventory
	 * @param src source inventory
	 * @param tgt target inventory
	 * @param item item to transfer
	 * @return number of items transferred
	 */
	public static int transferStack(Inventory src, Inventory tgt, ItemEntry item) {
		return transferRecord(src.get(item), tgt);
	}
	/**
	 * Transfers a first stack from an inventory reader if supported
	 * @param reader inventory reader
	 * @param inv target inventory
	 * @return transferred items
	 * @throws UnsupportedOperationException when the inventory reader does not support random access
	 */
	public static ItemStack transferStack(InventoryReader reader, Inventory inv) {
		return transferStack(reader, inv, Integer.MAX_VALUE);
	}
	/**
	 * Transfers a portion of the first stack from an inventory reader if supported
	 * @param reader inventory reader
	 * @param inv target inventory
	 * @param amount amount to transfer
	 * @return transferred items
	 * @throws UnsupportedOperationException when the inventory reader does not support random access
	 */
	public static ItemStack transferStack(InventoryReader reader, Inventory inv, int amount) {
		ItemEntry item = reader.currentItem();
		int amount0 = inv.insertibleRemain(amount, item);
		int read = reader.extract(amount0);
		inv.insert(item, read);
		return new ItemStack(item, amount);
	}
	/**
	 * Transfers all units of a specific item from an inventory reader if supported
	 * @param reader inventory reader
	 * @param inv target inventory
	 * @param item item to transfer
	 * @return number of transferred items
	 * @throws UnsupportedOperationException when the inventory reader does not support random access
	 */
	public static int transferStack(InventoryReader reader, Inventory inv, ItemEntry item) {
		return transferStack(reader, inv, item, Integer.MAX_VALUE);
	}
	/**
	 * Transfers a specific item from an inventory reader if supported
	 * @param reader inventory reader
	 * @param inv target inventory
	 * @param item item to transfer
	 * @param amount amount to transfer
	 * @return number of transferred items
	 * @throws UnsupportedOperationException when the inventory reader does not support random access
	 */
	public static int transferStack(InventoryReader reader, Inventory inv, ItemEntry item, int amount) {
		int amount0 = inv.insertibleRemain(amount, item);
		int read = reader.extract(item, amount0);
		return inv.insert(item, read);
	}
	/**
	 * Transfers items from inventory to inventory writer
	 * @param src source inventory
	 * @param tgt target inventory
	 * @param item item to transfer
	 * @param amount number of items
	 * @return number of items transferred
	 */
	public static int transferStack(Inventory src, InventoryWriter tgt, ItemEntry item, int amount) {
		return transferRecord(src.get(item), tgt, amount);
	}
	/**
	 * Transfers the entire specified stack from inventory to inventory writer
	 * @param src source inventory
	 * @param tgt target inventory
	 * @param item item to transfer
	 * @return number of items transferred
	 */
	public static int transferStack(Inventory src, InventoryWriter tgt, ItemEntry item) {
		return transferRecord(src.get(item), tgt);
	}
}
