/**
 * 
 */
package mmb.WORLD.inventory.storage;

import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import mmb.BEANS.Loader;
import mmb.BEANS.Saver;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemLoader;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.inventory.ItemLoader.ItemTarget;
import mmb.WORLD.items.ItemEntry;
import mmb.debug.Debugger;
import monniasza.collects.Collects;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 *
 */
public class SimpleInventory implements Inventory, Saver<JsonNode>, Loader<@Nullable JsonNode>{
	private static final Debugger debug = new Debugger("INVENTORIES");
	@Nonnull private SelfSet<ItemEntry, Node> contents = new HashSelfSet<>();
	private double volume = 0;
	@Deprecated public double capacity = 2;
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
			if(n < 0) throw new IllegalArgumentException("Inserting negative items. Use extract() instead.");
			if(n == 0) return 0;
			
			double remainVolume = capacity - volume;
			int remainAmount = (int) (remainVolume / type.volume());
			int result = Math.min(n, remainAmount);
			
			volume += type.volume(result);
			amount += result;
			
			return result;
		}
		@Override
		public int extract(int n) {
			if(n < 0) return -insert(n);
			if(n == 0) return 0;
			
			int result = Math.min(n, amount);
			
			volume -= type.volume(result);
			amount -= result;
			
			return result;
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
	
	/**
	 * Sets capacity of this inventory
	 * @param capacity new capacity
	 */
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	@Override
	public double volume() {
		return volume;
	}

	@Override
	public Iterator<ItemRecord> iterator() {
		return Collects.downcastIterator(contents.iterator());
	}

	@Override
	public void load(@Nullable JsonNode data) {	
		if(data == null) return;
		//Prepare
		contents.clear();
		capacity = 2;
		volume = 0;
		
		if(!data.isArray()) {
			debug.printl("Expected array, got "+data.getNodeType());
		}
		//Ensure it is an array
		ArrayNode arr = (ArrayNode) data;
		
		if(arr.size() == 0) {
			debug.printl("Empty data array");
			return;
		}
		ItemLoader.load(arr, new ItemTarget() {
			@Override
			public void addItem(ItemEntry ent, int amt) {
				Node existing = contents.get(ent); //get existing node if it exists
				if(existing == null) { //A new entry
					Node irecord = new Node(amt, ent);
					volume += irecord.volume();
					contents.add(irecord);
				}else{ //It already exists
					existing.insert(amt);
				}
			}
			@Override
			public void setCapacity(double cap) {
				capacity = cap;
			}
		});
	}

	@Override
	public JsonNode save() {
		return ItemLoader.save(contents, capacity); //return the saved inventory
	}

	@Override
	public ItemRecord nget(ItemEntry entry) {
		return contents.get(entry);
	}
}
