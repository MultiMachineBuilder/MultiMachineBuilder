/**
 * 
 */
package mmb.world.part;

import java.awt.Graphics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;

import mmb.beans.Saver;
import mmb.data.json.JsonTool;
import mmb.debug.Debugger;
import mmb.texture.BlockDrawer;
import mmb.world.chance.Chance;
import mmb.world.crafting.RecipeOutput;
import mmb.world.item.ItemEntry;
import mmb.world.item.ItemType;
import mmb.world.item.Items;

/**
 *
 * @author oskar
 *
 */
public interface PartEntry extends Saver{
	/** @return a part-wise copy of this*/
	@Nonnull public PartEntry partClone();
	
	//Type
	/** @return the part type */
	@Nonnull public PartType type();
	/**
	 * @param type block type to check
	 * @return does given type match actual type?
	 */
	public default boolean typeof(PartType type) {
		return type == type();
	}
	
	//Drop & RTP
	/** @return items dropped when module is removed */
	@Nonnull public default Chance dropItems() {
		return type().dropItems();
	}
	/** @return items returned to the player */
	@Nonnull public default RecipeOutput returnToPlayer() {
		return type().returnToPlayer();
	}
	
	//Rendering
	/**
	 * Renders a part
	 * @param x left X coordinate
	 * @param y upper Y coordinate
	 * @param g graphics context
	 * @param side side size
	 */
	public default void render(int x, int y, Graphics g, int side) {
		BlockDrawer drawer = type().getTexture();
		drawer.draw(null, x, y, g, side);
	}
	
	//Serialization (akin to ItemEntry)
	/**
	 * @param item the item to save
	 * @return the JSON representation of this item entry
	 */
	public static JsonNode savePart(@Nullable PartEntry item) {
		if(item == null) return NullNode.instance;
		JsonNode save = item.save();
		if(save == null) return new TextNode(item.type().id());
		ArrayNode array = JsonTool.newArrayNode();
		array.add(item.type().id());
		array.add(save);
		return array;
	}
	/**
	 * Loads a non-stackable item from the JSON data
	 * @param data JSON data
	 * @return item it if loaded successfully, or null if failed
	 */
	@Nullable public static PartEntry loadFromJson(@Nullable JsonNode data) {
		return loadFromJsonExpectType(data, null);
	}
	@Nullable public static <T extends PartEntry> PartEntry loadFromJsonExpectType(@Nullable JsonNode data, @Nullable Class<T> cls) {
		if(data == null) return null;
		if(data.isNull()) return null;
		if(data.isArray()) {
			String id = data.get(0).asText();
			JsonNode idata = data.get(1);
			PartType type = Items.getExpectType(id, PartType.class);
			if(type == null) return null;
			return type.<T>loadPartExpectType(idata, cls);
		}
		if(data.isTextual()) {
			String id = data.asText();
			PartType item = Items.getExpectType(id, PartType.class);
			if(item == null) {
				new Debugger("BLOCK PARTS").printl("Invalid part: "+id);
				return null;
			}
			return item.createPart();
		}
		return null;
	}
}
