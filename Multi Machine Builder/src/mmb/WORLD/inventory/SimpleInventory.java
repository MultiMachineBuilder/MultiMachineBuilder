/**
 * 
 */
package mmb.WORLD.inventory;

import java.util.Iterator;

import javax.annotation.Nonnull;

import mmb.COLLECTIONS.Collects;
import mmb.COLLECTIONS.HashSelfSet;
import mmb.COLLECTIONS.SelfSet;

/**
 * @author oskar
 *
 */
public class SimpleInventory implements Inventory {
	private SelfSet<ItemEntry, Node> contents = new HashSelfSet<>();
	private double volume = 0;
	public double capacity = 2;
	private final class Node implements ItemRecord{
		private Node(int amount, ItemEntry type) {
			this.amount = amount;
			this.type = type;
		}
		protected int amount;
		@Nonnull public final ItemEntry type;
		@Override
		public ItemEntry item() {
			return type;
		}
		@Override
		public int insert(int n) {
			if(n < 0) throw new IllegalArgumentException("Extracting negative items. Use extract() instead.");
			if(n == 0) return 0;
			
			double remainVolume = capacity - volume;
			int remainAmount = (int) (remainVolume / type.volume());
			int result = Math.min(n, remainAmount);
			volume += type.volume(result);
			amount += n;
			
			return result;
		}
		@Override
		public int extract(int n) {
			if(n < 0) return -insert(n);
			if(n == 0) return 0;
			
			if(n >= amount) {
				double vol = type.volume(amount);
				volume -= vol;
				return amount;
			}
			double vol = type.volume(n);
			volume -= vol;
			amount -= n;
			return n;
		}
		@Override
		public int amount() {
			return amount;
		}
		@Override
		public Inventory inventory() {
			return SimpleInventory.this;
		}
	}
	@Override
	public ItemRecord get(ItemEntry entry) {
		ItemRecord result = contents.get(entry);
		if(result == null) {
			Node node = new Node(0, entry);
			contents.add(node);
			return node;
		}
		return result;
	}

	@Override
	public int insert(ItemEntry ent, int amount) {
		if(!ent.exists()) return 0;
		Node node = contents.get(ent);
		if(node == null) {
			node = new Node(0, ent);
			contents.add(node);
		}
		return node.insert(amount);
	}

	@Override
	public int extract(ItemEntry ent, int amount) {
		if(!ent.exists()) return 0;
		Node node = contents.get(ent);
		if(node == null) return 0;
		return node.extract(amount);
	}

	@Override
	public double capacity() {
		return capacity;
	}

	@Override
	public double volume() {
		return volume;
	}

	@SuppressWarnings("null")
	@Override
	public Iterator<ItemRecord> iterator() {
		return Collects.downcastIterator(contents.iterator());
	}
}
