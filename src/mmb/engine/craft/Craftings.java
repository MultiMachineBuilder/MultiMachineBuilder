/**
 * 
 */
package mmbeng.craft;

import java.util.Objects;

import mmbeng.debug.Debugger;
import mmbeng.inv.Inventory;
import mmbeng.inv.io.InventoryReader;
import mmbeng.inv.io.InventoryWriter;
import mmbeng.item.ItemEntry;

/**
 *
 * @author oskar
 *
 */
public class Craftings {
	private Craftings() {}
	public static final Debugger debug = new Debugger("CRAFTING PROCESSOR");
	
	/**
	 * Performs a crafting transaction
	 * @param inItems a set of input items
	 * @param reader inventory reader
	 * @param outItems a set of output items
	 * @param writer inventory writer
	 * @param amount number of transactions to perform
	 * @return number of crafting transactions
	 */
	public static int transact(RecipeOutput inItems, InventoryReader reader, RecipeOutput outItems, InventoryWriter writer, int amount) {
		int transactable = writer.toInsertBulk(outItems, amount);
		int moved = reader.extractBulk(inItems, transactable);
		return writer.bulkInsert(outItems, moved);
	}
	/**
	 * Performs a crafting transaction
	 * @param inItems a set of input items
	 * @param reader source inventory
	 * @param outItems a set of output items
	 * @param writer inventory writer
	 * @param amount number of transactions to perform
	 * @return number of crafting transactions
	 */
	public static int transact(RecipeOutput inItems, Inventory reader, RecipeOutput outItems, InventoryWriter writer, int amount) {
		return transact(inItems, reader.createReader(), outItems, writer, amount);
	}
	/**
	 * Performs a crafting transaction
	 * @param inItems a set of input items
	 * @param reader inventory reader
	 * @param outItems a set of output items
	 * @param writer destination inventory
	 * @param amount number of transactions to perform
	 * @return number of crafting transactions
	 */
	public static int transact(RecipeOutput inItems, InventoryReader reader, RecipeOutput outItems, Inventory writer, int amount) {
		return transact(inItems, reader, outItems, writer.createWriter(), amount);
	}
	/**
	 * Performs a crafting transaction
	 * @param inItems a set of input items
	 * @param reader source inventory
	 * @param outItems a set of output items
	 * @param writer destination inventory
	 * @param amount number of transactions to perform
	 * @return number of crafting transactions
	 */
	public static int transact(RecipeOutput inItems, Inventory reader, RecipeOutput outItems, Inventory writer, int amount) {
		return transact(inItems, reader.createReader(), outItems, writer.createWriter(), amount);
}
	
	public static boolean transact(SingleItem in, RecipeOutput out, Inventory from, Inventory to) {
		return transact(in.item(), in.amount(), out, from, to);
	}
	public static boolean transact(ItemEntry entry, int inAmount, RecipeOutput out, Inventory from, Inventory to) {
		Objects.requireNonNull(from, "from is null");
		if(from.get(entry).amount() < inAmount) return false;
		//Calculate capacity
		double volume = out.outVolume();
		double remainCapacity = to.capacity() - to.volume();
		if(volume > remainCapacity) {
			debug.printl("Required "+volume+" capacity, got "+remainCapacity);
			return false;
		} //Not enough space in the output
		//Withdraw from input
		from.extract(entry, inAmount);
		out.produceResults(to.createWriter());
		return true;
	}
	
}
