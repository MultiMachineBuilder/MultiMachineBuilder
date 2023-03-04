/**
 * 
 */
package mmb.engine.recipe;

import java.util.Objects;

import mmb.engine.debug.Debugger;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.io.InventoryReader;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.item.ItemEntry;

/**
 * A set of crafting transactions
 * @author oskar
 */
public class RecipeUtil {
	private RecipeUtil() {}
	private static final Debugger debug = new Debugger("CRAFTING PROCESSOR");
	
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
	/**
	 * Performs a crafting transaction
	 * @param in input item stack
	 * @param out a set of output items
	 * @param from source inventory
	 * @param to destination inventory
	 * @return is the transaction successfull?
	 */
	public static boolean transact(SingleItem in, RecipeOutput out, Inventory from, Inventory to) {
		return transact(in.item(), in.amount(), out, from, to);
	}
	/**
	 * Performs a crafting transaction
	 * @param entry input item
	 * @param inAmount number of input items
	 * @param out a set of output items
	 * @param from source inventory
	 * @param to destination inventory
	 * @return is the transaction successfull?
	 */
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
