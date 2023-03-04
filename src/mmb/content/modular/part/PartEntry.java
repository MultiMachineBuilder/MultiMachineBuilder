/**
 * 
 */
package mmb.content.modular.part;

import java.awt.Graphics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;

import mmb.NN;
import mmb.Nil;
import mmb.beans.Saver;
import mmb.engine.chance.Chance;
import mmb.engine.debug.Debugger;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemType;
import mmb.engine.item.Items;
import mmb.engine.json.JsonTool;
import mmb.engine.recipe.RecipeOutput;
import mmb.engine.texture.BlockDrawer;

/**
 *
 * @author oskar
 *
 */
public interface PartEntry extends Saver{
	/** @return a part-wise copy of this*/
	@NN public PartEntry partClone();
	
	//Type
	/** @return the part type */
	@NN public PartType type();
	/**
	 * @param type block type to check
	 * @return does given type match actual type?
	 */
	public default boolean typeof(PartType type) {
		return type == type();
	}
	
	//Drop & RTP
	/** @return items dropped when module is removed */
	@NN public default Chance dropItems() {
		return type().dropItems();
	}
	/** @return items returned to the player */
	@NN public default RecipeOutput returnToPlayer() {
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
	public static JsonNode savePart(@Nil PartEntry item) {
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
	@Nil public static PartEntry loadFromJson(@Nil JsonNode data) {
		return loadFromJsonExpectType(data, null);
	}
	@Nil public static <T extends PartEntry> PartEntry loadFromJsonExpectType(@Nil JsonNode data, @Nil Class<T> cls) {
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
