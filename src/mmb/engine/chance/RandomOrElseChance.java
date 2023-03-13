/**
 * 
 */
package mmb.engine.chance;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.ainslec.picocog.PicoWriter;

import mmb.Nil;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.item.ItemEntry;
import mmb.engine.worlds.world.World;

/**
 * Random chanced item drop with else
 * @author oskar
 */
public class RandomOrElseChance implements Chance {
	
	/** The propability of item drop */
	public final double chance;
	/** The inner item drop */
	public final Chance wrap;
	/** The second item drop */
	public final Chance other;

	/**
	 * Creates a random chance object
	 * @param chance the chance of selecting the first item
	 * @param wrap the item which may be rewarded
	 * @param other the result when first chance is not selected
	 */
	public RandomOrElseChance(double chance, Chance wrap, Chance other) {
		super();
		this.chance = chance;
		this.wrap = wrap;
		this.other = other;
	}

	@Override
	public boolean drop(@Nil InventoryWriter inv, World map, int x, int y) {
		if(Math.random() < chance) return wrap.drop(inv, map, x, y);
		return other.drop(inv, map, x, y);
	}

	@Override
	public void produceResults(InventoryWriter tgt, int amount) {
		if(Math.random() < chance) wrap.produceResults(tgt, amount);
		else other.produceResults(tgt, amount);
	}

	@Override
	public void represent(PicoWriter out) {
		out.write((chance*100)+str_CHANCELSE);
		wrap.represent(out);
		out.write(str_ELSE);
		other.represent(out);
		out.write("]");
	}

	
	@Override
	public boolean contains(ItemEntry item) {
		return wrap.contains(item) || other.contains(item);
	}

	private Set<ItemEntry> items;
	@Override
	public Set<ItemEntry> items() {
		if(items == null) {
			Set<ItemEntry> items0 = new HashSet<>();
			items0.addAll(wrap.items());
			items0.addAll(other.items());
			items = Collections.unmodifiableSet(items0);
		}
		return items;
	}

}
