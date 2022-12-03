/**
 * 
 */
package mmb.world.crafting;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.data.json.JsonTool;
import mmb.debug.Debugger;
import mmb.world.inventory.ItemLoader;
import mmb.world.inventory.ItemLoader.ItemTarget;
import mmb.world.inventory.ItemStack;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.item.ItemEntry;
import monniasza.collects.Collects;

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
					writer.insert(ent, amt);
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
	
	/**
	 * @return a stream collector to an item list
	 */
	@Nonnull public static Collector<ItemStack, List<ItemStack>, SimpleItemList> collectToItemList(){
		return new ILCollector();
	}

}
class ILCollector implements Collector<ItemStack, List<ItemStack>, SimpleItemList>{

	@Override
	public Supplier<List<ItemStack>> supplier() {
		return ArrayList::new;
	}

	@Override
	public BiConsumer<List<ItemStack>, ItemStack> accumulator() {
		return List::add;
	}

	@Override
	public BinaryOperator<List<ItemStack>> combiner() {
		return Collects::inplaceAddLists;
	}

	@Override
	public Function<List<ItemStack>, SimpleItemList> finisher() {
		return SimpleItemList::new;
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Set.of(Characteristics.UNORDERED);
	}
	
}
