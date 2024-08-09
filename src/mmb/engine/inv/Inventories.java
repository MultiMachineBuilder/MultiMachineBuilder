/**
 * 
 */
package mmb.engine.inv;

import java.util.function.Predicate;

import mmb.Nil;
import mmb.engine.inv.io.InventoryReader;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemStack;
import mmb.engine.recipe.RecipeOutput;

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
	public static int transferRecord(@Nil ItemRecord src, Inventory tgt, int amount) {
		if(!tgt.canInsert()) return 0; //unable to insert to target
		return transferRecord(src, tgt.createWriter(), amount);
	}
	/**
	 * Transfers an entire item record to an inventory writer
	 * @param src source inventory
	 * @param tgt target inventory
	 * @return number of items transfered
	 */
	public static int transferRecord(@Nil ItemRecord src, InventoryWriter tgt) {
		return transferRecord(src, tgt, Integer.MAX_VALUE);
	}
	/**
	 * Transfers an entire item record to an inventory
	 * @param src source inventory
	 * @param tgt target inventory
	 * @return number of items transfered
	 */
	public static int transferRecord(@Nil ItemRecord src, Inventory tgt) {
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
	public static int transferRecord(@Nil ItemRecord src, InventoryWriter tgt, int amount) {
		if(src == null) return 0; //not selected
		if(!src.canExtract()) return 0; //unable to extract from source
		int tomove = Math.min(src.amount(), amount);
		int moved = tgt.insert(src.item(), tomove);
		src.extract(moved);
		
		return moved;
	}
	
	//Entire inventory
	/**
	 * Transfer entire source inventory to the target inventory
	 * @param src source inventory
	 * @param tgt target target inventory
	 */
	public static void transferAll(Inventory src, Inventory tgt) {
		transferAll(src, tgt.createWriter());
	}
	/**
	 * Transfer entire source inventory to the target inventory
	 * @param src source inventory
	 * @param tgt inventory writer
	 */
	public static void transferAll(Inventory src, InventoryWriter tgt) {
		transferAll(src.createReader(), tgt);
	}
	/**
	 * Transfer entire source inventory to the target inventory
	 * @param src inventory reader
	 * @param tgt inventory writer
	 */
	public static void transferAll(InventoryReader src, InventoryWriter tgt) {
		while(src.hasNext()) {
			src.next();
			ItemEntry item = src.currentItem();
			int tomove = src.toBeExtracted(Integer.MAX_VALUE);
			int moved = tgt.insert(item, tomove);
			src.extract(moved);
		}
	}
	public static void transferAll(InventoryReader src, Inventory buffer) {
		transferAll(src, buffer.createWriter());
	}
	
	//First stack
	/**
	 * Transfers a first stack from an inventory reader if supported
	 * @param reader inventory reader
	 * @param inv target inventory
	 * @return transferred items
	 * @throws UnsupportedOperationException when the inventory reader does not support random access
	 */
	public static ItemStack transferFirstStack(InventoryReader reader, Inventory inv) {
		return transferFirstStack(reader, inv, Integer.MAX_VALUE);
	}
	/**
	 * Transfers a portion of the first stack from an inventory reader if supported
	 * @param reader inventory reader
	 * @param inv target inventory
	 * @param amount amount to transfer
	 * @return transferred items
	 * @throws UnsupportedOperationException when the inventory reader does not support random access
	 */
	public static ItemStack transferFirstStack(InventoryReader reader, Inventory inv, int amount) {
		return transferFirstStack(reader, inv.createWriter(), amount);
	}
	/**
	 * Transfers a portion of an item record to an inventory writer
	 * @param src source inventory
	 * @param tgt tagret inventory
	 * @param amount number of items to transfer
	 * @return items transfered, or null if none found
	 */
	@Nil public static ItemStack transferFirstStack(InventoryReader src, InventoryWriter tgt, int amount) {
		if(!src.hasNext()) return null;
		ItemEntry item = src.currentItem();
		int tomove = src.toBeExtracted(amount);
		int moved = tgt.insert(item, tomove);
		src.extract(moved);
		return new ItemStack(item, moved);
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
	
	//Filtered
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
	
	//First item
	/**
	 * Transfers first item from an inventory to other inventory
	 * @param src source inventory
	 * @param tgt target inventory
	 * @return item transferred, or null if none
	 */
	@Nil public static ItemEntry transferFirst(Inventory src, Inventory tgt) {
		return transferFirst(src, tgt.createWriter());
	}
	/**
	 * Transfers first item from an inventory to an inventory writer
	 * @param src source inventory
	 * @param tgt inventory writer
	 * @return item transferred, or null if none
	 */
	@Nil public static ItemEntry transferFirst(Inventory src, InventoryWriter tgt) {
		for(ItemRecord rec: src) {
			int moved = transferRecord(rec, tgt, 1);
			if(moved == 1) return rec.item();
		}
		return null;
	}
	
	//First stack
	/**
	 * Transfers a first stack from an inventory
	 * @param src source inventory
	 * @param tgt inventory writer
	 * @return transferred items, or null if inventory is empty
	 */
	@Nil public static ItemStack transferFirstStack(Inventory src, Inventory tgt) {
		return transferFirstStack(src, tgt.createWriter());
	}
	/**
	 * Transfers a first stack from an inventory
	 * @param src source inventory
	 * @param tgt inventory writer
	 * @return transferred items, or null if inventory is empty
	 */
	@Nil public static ItemStack transferFirstStack(Inventory src, InventoryWriter tgt) {
		for(ItemRecord rec: src) {
			int tf = transferRecord(rec, tgt, Integer.MAX_VALUE);
			if(tf > 0) return new ItemStack(rec.item(), tf);
		}
		return null;
	}
	/**
	 * Transfers a first stack from an inventory
	 * @param src source inventory
	 * @param tgt inventory writer
	 * @return transferred items, or null if inventory is empty
	 */
	@Nil public static ItemStack transferFirstStack(InventoryReader src, InventoryWriter tgt) {
		if(src.hasNext()) {
			src.next();
			ItemEntry ient = src.currentItem();
			int tomove = src.toBeExtracted(Integer.MAX_VALUE);
			int moved = tgt.insert(ient, tomove);
			src.extract(moved);
			return new ItemStack(ient, moved);
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
		int tomove = inv.insertibleRemain(amount, item);
		int moved = reader.extract(item, tomove);
		return inv.insert(item, moved);
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
	/**
	 * Transfers a specific item from an inventory reader if supported
	 * @param reader inventory reader
	 * @param tgt inventory writer
	 * @param item item to transfer
	 * @param amount amount to transfer
	 * @return number of transferred items
	 * @throws UnsupportedOperationException when the inventory reader does not support random access
	 */
	public static int transferStack(InventoryReader reader, InventoryWriter tgt, ItemEntry item, int amount) {
		int tomove = reader.toBeExtracted(item, amount);
		int moved = tgt.insert(item, tomove);
		return reader.extract(item, moved);
	}
	/**
	 * Transfers a specific item from an inventory reader if supported
	 * @param reader inventory reader
	 * @param tgt inventory writer
	 * @param item item to transfer
	 * @param amount amount to transfer
	 * @param maxvol maximum volume
	 * @return number of transferred items
	 * @throws UnsupportedOperationException when the inventory reader does not support random access
	 */
	public static int transferStackVolumeLimited(InventoryReader reader, InventoryWriter tgt, ItemEntry item, int amount, double maxvol) {
		double outvol = item.outVolume();
		if(outvol == 0) return 0;
		double voltomove = outvol*amount;
		int tomove = amount;
		if(voltomove > maxvol) tomove = (int)(maxvol/outvol);
		if(tomove == 0) return 0;
		return transferStack(reader, tgt, item, tomove);
	}
	/**
	 * Transfers a specific stack from an inventory reader if supported
	 * @param reader inventory reader
	 * @param tgt inventory writer
	 * @param item item to transfer
	 * @return number of transferred items
	 * @throws UnsupportedOperationException when the inventory reader does not support random access
	 */
	public static int transferStack(InventoryReader reader, InventoryWriter tgt, ItemEntry item) {
		return transferStack(reader, tgt, item, Integer.MAX_VALUE);
	}

	
	//Multi-stack
	/**
	 * Transfers multiple stacks of items
	 * @param reader source
	 * @param writer destination
	 * @param items items to move
	 * @param amount number of units to transfer
	 * @throws UnsupportedOperationException when the inventory reader does not support random access
	 */
	public static void transferMulti(InventoryReader reader, InventoryWriter writer, RecipeOutput items, int amount) {
		for(ItemStack stk: items) transferStack(reader, writer, stk.item, stk.amount);
	}
	/**
	 * Transfers multiple stacks of items
	 * @param reader source
	 * @param writer destination
	 * @param items items to move
	 * @param amount number of units to transfer
	 * @param maxvol maximum volume
	 * @return number of units transferred
	 * @throws UnsupportedOperationException when the inventory reader does not support random access
	 */
	public static int transferMultiVolumeLimited(InventoryReader reader, InventoryWriter writer, RecipeOutput items, int amount, double maxvol) {
		double outvol = items.outVolume();
		if(outvol == 0) return 0;
		double voltomove = outvol*amount;
		int tomove = amount;
		if(voltomove > maxvol) tomove = (int)(maxvol/outvol);
		if(tomove == 0) return 0;
		transferMulti(reader, writer, items, tomove);
		return tomove;
	}

	//Bulk
	/**
	 * Transfers transfer units of items, without breaking them up
	 * @param reader source
	 * @param writer destination
	 * @param items transfer unit
	 * @param amount number of units to transfer
	 * @return number of units transferred
	 * @throws UnsupportedOperationException when the inventory reader does not support random access
	 */
	public static int transferBulk(InventoryReader reader, InventoryWriter writer, RecipeOutput items, int amount) {
		int tomove = reader.toBeExtractedBulk(items, amount);
		int moved = writer.bulkInsert(items, tomove);
		return reader.extractBulk(items, moved);
	}
	/**
	 * Transfers transfer units of items, without breaking them up
	 * @param reader source
	 * @param writer destination
	 * @param items transfer unit
	 * @param amount number of units to transfer
	 * @param maxvol maximum volume
	 * @return number of units transferred
	 * @throws UnsupportedOperationException when the inventory reader does not support random access
	 */
	public static int transferBulkVolumeLimited(InventoryReader reader, InventoryWriter writer, RecipeOutput items, int amount, double maxvol) {
		double outvol = items.outVolume();
		if(outvol == 0) return 0;
		double voltomove = outvol*amount;
		int tomove = amount;
		if(voltomove > maxvol) tomove = (int)(maxvol/outvol);
		if(tomove == 0) return 0;
		return transferBulk(reader, writer, items, tomove);
	}
}
