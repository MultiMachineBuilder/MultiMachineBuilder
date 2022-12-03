/**
 * 
 */
package mmb.world.crafting;

import mmb.world.inventory.Inventory;
import mmb.world.inventory.io.InventoryReader;
import mmb.world.inventory.io.InventoryWriter;

/**
 *
 * @author oskar
 *
 */
public class Craftings {
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
}
