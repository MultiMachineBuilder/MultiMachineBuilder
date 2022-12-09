/**
 * 
 */
package mmb.engine.inv.io;

import mmb.engine.craft.RecipeOutput;
import mmb.engine.item.ItemEntry;
import mmb.engine.worlds.world.World;
import mmbbase.beans.Positioned;

/**
 * A dropper is an inventory writer, which drops items at a specific location
 * @author oskar
 */
public class Dropper implements InventoryWriter, Positioned {
	private final World map;
	private int x, y;
	/**
	 * Creates a dropper
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param map
	 */
	public Dropper(int x, int y, World map) {
		this.map = map;
		this.x  = x;
		this.y = y;
	}

	@Override
	public int insert(ItemEntry ent, int amount) {
		map.dropItem(ent, amount, x, y);
		return amount; //always accepts
	}

	@Override
	public int posX() {
		return x;
	}

	@Override
	public int posY() {
		return y;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int bulkInsert(RecipeOutput block, int amount) {
		map.dropItems(block, amount, x, y);
		return amount; //always accepts
	}

	@Override
	public int toInsertBulk(RecipeOutput block, int amount) {
		return amount;
	}

	@Override
	public int toInsert(ItemEntry item, int amount) {
		return amount;
	}

}
