/**
 * 
 */
package mmb.engine.chance;

import java.util.Set;

import org.ainslec.picocog.PicoWriter;

import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.item.ItemEntry;
import mmb.engine.worlds.world.World;

/**
 * @author oskar
 *
 */
public class RandomChance implements Chance {
	
	public final double chance;
	public final Chance wrap;

	/**
	 * Creates a random chance object
	 * @param chance the chance
	 * @param wrap the item which may be rewarded
	 */
	public RandomChance(double chance, Chance wrap) {
		super();
		this.chance = chance;
		this.wrap = wrap;
	}

	@Override
	public boolean drop(InventoryWriter inv, World map, int x, int y) {
		if(Math.random() < chance) return wrap.drop(inv, map, x, y);
		return true;
	}

	@Override
	public void produceResults(InventoryWriter tgt, int amount) {
		if(Math.random() < chance) wrap.produceResults(tgt, amount);
	}

	@Override
	public void represent(PicoWriter out) {
		out.write((chance*100)+"% chance [");
		wrap.represent(out);
		out.write("]");
	}

	@Override
	public boolean contains(ItemEntry item) {
		return wrap.contains(item);
	}

	@Override
	public Set<ItemEntry> items() {
		return wrap.items();
	}

}
