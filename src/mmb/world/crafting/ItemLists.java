/**
 * 
 */
package mmb.world.crafting;

import java.util.ArrayDeque;
import java.util.Queue;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.data.json.JsonTool;
import mmb.debug.Debugger;
import mmb.world.inventory.ItemLoader;
import mmb.world.inventory.ItemLoader.ItemTarget;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.items.ItemEntry;

/**
 * @author oskar
 *
 */
public class ItemLists {
	private ItemLists() {}
	private static final Debugger debug = new Debugger("ITEM LISTS");
	/**
	 * Loads a recipe output as {@code SimpleItemList}
	 * @param node saved recipe output
	 * @return loaded recipe output
	 */
	public static SimpleItemList read(@Nullable JsonNode node) {
		if(node == null) {
			debug.printl("Item list node is null");
			return null;
		}else if(node.isArray()) {
			SimpleItemList list = new SimpleItemList();
			InventoryWriter writer = list.createWriter();
			ItemLoader.load((ArrayNode)node, new ItemTarget() {
				@Override
				public void addItem(ItemEntry ent, int amt) {
					writer.write(ent, amt);
				}
				@Override
				public void setCapacity(double cap) {
					//do nothing
				}
			});
			return list;
		}else{
			debug.printl("Unsupported item list node type: ");
			return null;
		}
	}
	/**
	 * Saves a recipe output
	 * @param list recipe output to save
	 * @return saved recipe output
	 */
	public static JsonNode save(RecipeOutput list) {
		Queue<JsonNode> nodes = new ArrayDeque<>(); //prepare the queue
		for(Entry<ItemEntry> n: list.getContents().object2IntEntrySet()) { //write items
			if(n.getIntValue() == 0) continue;
			JsonNode data = n.getKey().save();
			ArrayNode ent = JsonTool.newArrayNode(); //prepare the item node
			ent.add(n.getKey().type().id()); //write the type
			if(data != null) //if data is present...
				ent.add(data); //...add data to a item node
			ent.add(n.getIntValue()); //write the amount
			
			nodes.add(ent); //write the item node to the list
		}
		ArrayNode result = JsonTool.newArrayNode();
		result.addAll(nodes); //write nodes from the queue to the result
		return result;
	}

}
