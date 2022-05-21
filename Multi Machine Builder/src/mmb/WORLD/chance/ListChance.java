/**
 * 
 */
package mmb.WORLD.chance;

import org.ainslec.picocog.PicoWriter;

import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public class ListChance implements Chance {
	private final Chance[] chances;
	public ListChance(Chance... chances) {
		super();
		this.chances = chances;
	}

	@Override
	public boolean drop(InventoryWriter inv, World map, int x, int y) {
		boolean result = false;
		for(Chance chance: chances) {
			result |= chance.drop(inv, map, x, y);
		}
		return result;
	}

	@Override
	public void produceResults(InventoryWriter tgt, int amount) {
		for(Chance chance: chances) {
			chance.produceResults(tgt, amount);
		}
	}

	@Override
	public void represent(PicoWriter out) {
		out.write("{");
		out.indentRight();
		out.writeln("");
		for(Chance chance: chances) {
			chance.represent(out);
			out.writeln("");
		}
		out.indentLeft();
		out.write("}");
	}

	@Override
	public boolean contains(ItemEntry item) {
		for(Chance chance: chances) {
			if(chance.contains(item)) return true;
		}
		return false;
	}

}
