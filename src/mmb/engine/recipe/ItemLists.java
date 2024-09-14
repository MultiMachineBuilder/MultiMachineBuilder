/**
 * 
 */
package mmb.engine.recipe;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.NN;
import mmb.Nil;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.InventoryLoader;
import mmb.engine.inv.InventoryLoader.ItemTarget;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.item.ItemEntry;
import mmb.engine.json.JsonTool;
import monniasza.collects.Collects;

/**
 * A set of utilites for loading, saving and processing item lists
 * @author oskar
 */
public class ItemLists {
	private ItemLists() {}
	private static final Debugger debug = new Debugger("ITEM LISTS");
	/**
	 * Loads a recipe output as {@code SimpleItemList}
	 * @param node saved recipe output
	 * @return loaded recipe output
	 */
	public static SimpleItemList read(@Nil JsonNode node) {
		if(node == null) {
			debug.printl("Item list node is null");
			return null;
		}else if(node.isArray()) {
			SimpleItemList list = new SimpleItemList();
			InventoryWriter writer = list.createWriter();
			InventoryLoader.load((ArrayNode)node, new ItemTarget() {
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
		ArrayNode result = JsonTool.newArrayNode();
		for(Entry<ItemEntry> n: list.getContents().object2IntEntrySet()) { //write items
			if(n.getIntValue() == 0) continue;
			JsonNode data = n.getKey().save();
			ArrayNode ent = JsonTool.newArrayNode(); //prepare the item node
			ent.add(n.getKey().type().id()); //write the type
			if(data != null) //if data is present...
				ent.add(data); //...add data to a item node
			ent.add(n.getIntValue()); //write the amount
			result.add(ent); //write the item node to the list
		}
		return result;
	}
	/**
	 * @return a stream collector to an item list
	 */
	@NN public static Collector<mmb.engine.recipe.ItemStack, @NN List<mmb.engine.recipe.ItemStack>, @NN SimpleItemList> collectToItemList(){
		return new ILCollector();
	}

}
class ILCollector implements Collector<mmb.engine.recipe.ItemStack, @NN List<mmb.engine.recipe.ItemStack>, @NN SimpleItemList>{

	@Override
	public @NN Supplier<@NN List<mmb.engine.recipe.ItemStack>> supplier() {
		return ArrayList::new;
	}

	@Override
	public @NN BiConsumer<@NN List<mmb.engine.recipe.ItemStack>, mmb.engine.recipe.ItemStack> accumulator() {
		return List::add;
	}

	@Override
	public @NN BinaryOperator<@NN List<mmb.engine.recipe.ItemStack>> combiner() {
		return Collects::inplaceAddLists;
	}

	@Override
	public @NN Function<@NN List<mmb.engine.recipe.ItemStack>, @NN SimpleItemList> finisher() {
		return SimpleItemList::new;
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Set.of(Characteristics.UNORDERED);
	}
	
}
