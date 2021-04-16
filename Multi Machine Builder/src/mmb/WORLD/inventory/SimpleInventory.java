/**
 * 
 */
package mmb.WORLD.inventory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import mmb.BEANS.Loader;
import mmb.BEANS.Saver;
import mmb.COLLECTIONS.Collects;
import mmb.COLLECTIONS.HashSelfSet;
import mmb.COLLECTIONS.SelfSet;
import mmb.DATA.json.JsonTool;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.item.Items;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class SimpleInventory implements Inventory, Saver<JsonNode>, Loader<JsonNode>{
	private static final Debugger debug = new Debugger("INVENTORIES");
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

	@Override
	public double volume() {
		return volume;
	}

	@SuppressWarnings("null")
	@Override
	public Iterator<ItemRecord> iterator() {
		return Collects.downcastIterator(contents.iterator());
	}

	@Override
	public void load(JsonNode data) {	
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
		loop:
		for(JsonNode node: arr) {
			if(node.isNumber()) {//number: capacity	
				//This node is a number, use it as capacity
				NumericNode number = (NumericNode) node; //convert to a correct type
				double cap = number.asDouble(-1);
				if(cap < 0) {
					debug.printl("Invalid or missing volume: "+cap);
				}else {
					capacity = cap;
				}
			}
			else if(node.isArray()) {//array: item
				ArrayNode a = (ArrayNode) node;
				ItemEntry ent = null;
				
				//Get the type
				String id = a.get(0).asText();
				ItemType type = Items.items.get(id);
				if(type == null) { //unsupported item
					debug.printl("Unsupported item: "+id);
					continue loop;
				}
				
				int amt = -1;
				int size = a.size();
				switch(size) {
					case 2: //2 items: [type, amount]
						//A plain item
						try {
							ent = type.create();
							amt = a.get(1).asInt(-1);
						}catch(Exception e) {
							debug.pstm(e, "Failed to create "+id+" without data");
							continue loop;
						}
						break;
					case 3: //3 items: [type, info, amount]
						try {
							JsonNode idata = a.get(1);
							ent = type.load(idata);
							amt = a.get(2).asInt(-1);
						}catch(Exception e) {
							debug.pstm(e, "Failed to create "+id+" with data");
							continue loop;
						}
						break;
					default:
						debug.printl("Invalid array size: "+size);
						continue loop;
				}
				
				if(amt < 0) { //invalid amount
					debug.printl("Invalid amount: "+ amt);
					continue loop;
				}
				if(ent == null) { //failed to load
					debug.printl("Failed to load the item");
					continue loop;
				}
				
				//Success
				Node existing = contents.get(ent); //get existing node if it exists
				if(existing == null) { //A new entry
					Node irecord = new Node(amt, ent);
					volume += irecord.volume();
					contents.add(irecord);
				}else{ //It already exists
					existing.insert(amt);
				}
			}
			else{ //any other type(text, null, object, very big numbers, boolean,)
				debug.printl("Unsupported JsonNode in inventory: "+node.getNodeType());
			}
		}
	}

	@Override
	public JsonNode save() {
		// deepcode ignore ApiMigration: used as queue		Queue<JsonNode> nodes = new LinkedList<>(); //prepare the queue
		nodes.add(new DoubleNode(capacity)); // write the volume
		for(Node n: contents) { //write items
			JsonNode data = n.type.save();
			ArrayNode ent = JsonTool.newArrayNode(); //prepare the item node
			ent.add(n.type.type().id()); //write the type
			if(data != null) //if data is present...
				ent.add(data); //...add data to a item node
			ent.add(n.amount); //write the amount
			
			nodes.add(ent); //write the item node to the list;
		}
		ArrayNode result = JsonTool.newArrayNode();
		result.addAll(nodes); //write nodes from the queue to the result
		return result; //return the saved inventory
	}

	@Override
	public ItemRecord nget(ItemEntry entry) {
		return contents.get(entry);
	}
}
