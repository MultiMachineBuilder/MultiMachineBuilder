/**
 * 
 */
package mmb.WORLD.items;

import java.awt.Component;
import java.awt.Graphics;

import javax.annotation.Nullable;
import javax.swing.Icon;

import org.ainslec.picocog.PicoWriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;

import mmb.BEANS.Saver;
import mmb.DATA.json.JsonTool;
import mmb.WORLD.block.Drop;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.item.Items;
import mmb.WORLD.tool.WindowTool;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 * An item entry representing a single unit of item
 */
public interface ItemEntry extends Saver<@Nullable JsonNode>, RecipeOutput {

	@Override
	default double outVolume() {
		return volume();
	}
	//Recipe output
	@Override
	default void produceResults(InventoryWriter tgt, int amount) {
		tgt.write(this, amount);
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
			return type.loadItem(idata);
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
	public default void render(Graphics g, int x, int y, int side) {
		type().getTexture().draw(null, x, y, g, side);
	}
	public default WindowTool getTool() {
		return null;
	}
	@Override
	default boolean drop(InventoryWriter inv, World map, int x, int y) {
		return Drop.tryDrop(this, inv, map, x, y);
	}
	/**
	 * @return
	 */
	public default Icon icon() {
		return new Icon() {
			@Override public int getIconHeight() {
				return 32;
			}
			@Override public int getIconWidth() {
				return 32;
			}
			@Override public void paintIcon(Component c, Graphics g, int x, int y) {
				render(g, x, y, 32);
			}
		};
	}
}
