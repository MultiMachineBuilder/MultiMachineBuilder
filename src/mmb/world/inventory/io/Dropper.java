/**
 * 
 */
package mmb.world.inventory.io;

import mmb.beans.Positioned;
import mmb.world.crafting.RecipeOutput;
import mmb.world.items.ItemEntry;
import mmb.world.worlds.world.World;

/**
 * @author oskar
 * An inventory writer, which drops items
 */
public class Dropper implements InventoryWriter, Positioned {
	private final World map;
	private int x, y;
	public Dropper(World map) {
		this.map = map;
	}

	@Override
	public int write(ItemEntry ent, int amount) {
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

}
