/**
 * 
 */
package mmb.WORLD.item;

import javax.annotation.Nullable;
import javax.swing.Icon;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.WORLD.block.Drop;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemEntry;
import mmb.WORLD.worlds.world.World.BlockMap;
import mmb.debug.Debugger;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 *
 */
public class Item extends ItemBase implements ItemEntry, Drop {
	private static final Debugger debug = new Debugger("ITEMS");
	/**
	 * Get the hash code, which is always the same as ID's hash code.
	 * @return item's hash code.
	 * @see java.lang.String#hashCode()
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	@Override
	public boolean equals(@Nullable Object other) {
		if(this == other) return true;
		if(other == null) return false;
		if(!(other instanceof Identifiable)) return false;
		Object id1 = ((Identifiable<?>)other).id();
		if(id == id1) return true;
		if(id1 == null) return false;
		return id1.equals(id);
	}

	@Override
	public ItemEntry create() {
		return this;
	}
	@Override
	public ItemEntry itemClone() {
		return this;
	}
	@Override
	public ItemType type() {
		return this;
	}
	@Override
	public boolean exists() {
		return true;
	}
	@Override
	public Icon getIcon() {
		return drawer.toIcon();
	}
	@Override
	public ItemEntry load(JsonNode node) {
		debug.printl("Attempting to load a non-data item");
		return this;
	}
	@Override
	public boolean drop(@Nullable Inventory inv, BlockMap map, int x, int y) {
		if(inv == null || !inv.exists()) {
			//TODO Drop to world
			return true; //NYI
		}
		//Drop to inventory
		return inv.insert(this, 1) == 1;
	}
}
