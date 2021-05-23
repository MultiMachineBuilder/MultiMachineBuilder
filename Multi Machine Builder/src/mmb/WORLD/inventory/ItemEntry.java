/**
 * 
 */
package mmb.WORLD.inventory;

import java.awt.Graphics;

import javax.annotation.Nullable;

import org.ainslec.picocog.PicoWriter;
import org.joml.Vector3d;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import mmb.BEANS.Saver;
import mmb.DATA.json.JsonTool;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.item.Items;

/**
 * @author oskar
 * An item entry representing a single unit of item
 */
public interface ItemEntry extends Saver<@Nullable JsonNode>, RecipeOutput {

	@Override
	default void calcVolumes(Vector3d out) {
		out.x += volume();
		out.y += volume();
		out.z += volume();
	}
	//Recipe output
	@Override
	default void produceResults(Inventory tgt, int amount) {
		tgt.insert(this, amount);
	}
	@Override
	default void represent(PicoWriter out) {
		out.write("1× "+title());
	}
	
	
	/**
	 * @return the volume of single given {@code ItemEntry}
	 */
	public double volume();
	public default double volume(int amount) {
		return volume() * amount;
	}
	/**
	 * Clone this {@code ItemEntry}
	 * @return a copy of given ItemEntry
	 */
	public ItemEntry itemClone();
	public ItemType type();
	public default String title() {
		return type().title();
	}
	public default String description() {
		return type().description();
	}
	default public boolean exists() {
		return true;
	}
	/**
	 * Saves the item data.
	 * 
	 * @return null if item has no data
	 * , or a JSON node if data is present
	 */
	@Override
	default public @Nullable JsonNode save() {
		return null;
	}
	/**
	 * Loads a non-stackable item from the JSON data
	 * @param data
	 * @return item it if loaded successfully, or null if failed
	 */
	@SuppressWarnings("null")
	@Nullable public static ItemEntry loadFromJson(@Nullable JsonNode data) {
		if(data == null) return null;
		if(data.isNull()) return null;
		if(data.isArray()) {
			String id = data.get(0).asText();
			JsonNode idata = data.get(1);
			ItemType type = Items.items.get(id);
			if(type == null) return null;
			return type.load(idata);
		}
		if(data.isTextual()) {
			return Items.items.get(data.asText()).create();
		}
		return null;
	}
	public static JsonNode saveItem(@Nullable ItemEntry item) {
		if(item == null) return NullNode.instance;
		JsonNode save = item.save();
		if(save == null) return new TextNode(item.type().id());
		ArrayNode array = JsonTool.newArrayNode();
		array.add(item.type().id());
		array.add(save);
		return array;
	}
	public default void render(Graphics g, int x, int y) {
		type().getTexture().draw(null, x, y, g);
	}
}
