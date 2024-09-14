/**
 * 
 */
package mmb.engine.inv;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.NumericNode;

import mmb.NN;
import mmb.engine.debug.Debugger;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemType;
import mmb.engine.item.Items;
import mmb.engine.json.JsonTool;
import monniasza.collects.Collects;

/**
 * Loads and saves item record from/to JSON
 * @author oskar
 *
 */
public class InventoryLoader {
	private InventoryLoader() {}
	
	/**
	 * @author oskar
	 * This interface provides encapsulation for item loading
	 */
	public static interface ItemTarget {
		/**
		 * Adds item to the invaentory
		 * @param entry item to be added
		 * @param amount number of items to add
		 */
		public void addItem(ItemEntry entry, int amount);
		/**
		 * Sets capacity of this inventory
		 * @param capacity
		 */
		public void setCapacity(double capacity);
	}
	
	@NN private static final Debugger debug = new Debugger("INVENTORY LOADER");
	/**
	 * Loads a simple inventory
	 * @param data item data
	 * @param tgt inventory to load to
	 */
	public static void load(ArrayNode data, ItemTarget tgt) {
		if(data.size() == 0) {
			debug.printl("Empty data array");
			return;
		}
		for(JsonNode node: data) loadOneInvRow(node, tgt);
	}
	private static void loadOneInvRow(JsonNode node, ItemTarget tgt) {
		if(node.isNumber()) {//number: capacity	
			//This node is a number, use it as capacity
			NumericNode number = (NumericNode) node; //convert to a correct type
			double cap = number.asDouble(-1);
			if(cap < 0) {
				debug.printl("Invalid or missing volume: "+cap);
			}else {
				tgt.setCapacity(cap);
			}
		} else if(node.isArray()) {//array: item
			ArrayNode a = (ArrayNode) node;
			ItemEntry ent = null;
			
			//Get the type
			String id = a.get(0).asText();
			ItemType type = Items.get(id);
			if(type == null) { //unsupported item
				debug.printl("Unsupported item: "+id);
				return;
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
						debug.stacktraceError(e, "Failed to create "+id+" without data");
						return;
					}
					break;
				case 3: //3 items: [type, info, amount]
					try {
						JsonNode idata = a.get(1);
						ent = type.loadItem(idata);
						amt = a.get(2).asInt(-1);
					}catch(Exception e) {
						debug.stacktraceError(e, "Failed to create "+id+" with data");
						return;
					}
					break;
				default:
					debug.printl("Invalid array size: "+size);
					return;
			}
			
			if(amt < 0) { //invalid amount
				debug.printl("Invalid amount: "+ amt);
				return;
			}
			
			//Success
			tgt.addItem(ent, amt);
		}else{ //any other type(text, null, object, very big numbers, boolean,)
			debug.printl("Unsupported JsonNode in inventory: "+node.getNodeType());
		}
	}
	/**
	 * Saves an inventory
	 * @param iter inventory contents
	 * @param capacity inventory's capacity
	 * @return the inventory representation in JSON
	 */
	public static @NN ArrayNode save(Iterable<@NN ? extends ItemRecord> iter, double capacity) {
		Queue<JsonNode> nodes = new ArrayDeque<>(); //prepare the queue
		nodes.add(new DoubleNode(capacity)); // write the volume
		for(ItemRecord n: iter) { //write items
			if(n.amount() == 0) continue; //empty entry, skip
			JsonNode data = n.item().save();
			ArrayNode ent = JsonTool.newArrayNode(); //prepare the item node
			ent.add(n.item().type().id()); //write the type
			if(data != null) //if data is present...
				ent.add(data); //...add data to a item node
			ent.add(n.amount()); //write the amount
			
			nodes.add(ent); //write the item node to the list
		}
		ArrayNode result = JsonTool.newArrayNode();
		result.addAll(nodes); //write nodes from the queue to the result
		return result;
	}
	/**
	 * Saves an inventory
	 * @param iter inventory contents
	 * @param capacity inventory's capacity
	 * @return the inventory representation in JSON
	 */
	public static ArrayNode save(Iterator<@NN ? extends ItemRecord> iter, double capacity) {
		return save(Collects.iter(iter), capacity);
	}
}
