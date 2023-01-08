/**
 * 
 */
package mmb.engine.item;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

import org.ainslec.picocog.PicoWriter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;

import mmb.NN;
import mmb.Nil;
import mmb.beans.Saver;
import mmb.engine.block.BlockEntry;
import mmb.engine.chance.Chance;
import mmb.engine.craft.SingleItem;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemStack;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.json.JsonTool;
import mmb.engine.texture.BlockDrawer;
import mmb.engine.worlds.world.World;
import mmb.menu.wtool.WindowTool;

/**
 * @author oskar
 * An item entry representing a single unit of item
 */
public interface ItemEntry extends Saver, SingleItem{
	//Item properties
	/** @return the volume of this item entry */
	public double volume();
	/** @return type of this item entry */
	@NN public ItemType type();
	/** @return title of this item entry */
	public default String title() {
		return type().title();
	}
	/** @return description of this item entry */
	public default String description() {
		return type().description();
	}
	
	//Item operations
	/**
	 * Clone this {@code ItemEntry}
	 * @return a copy of given ItemEntry
	 */
	public ItemEntry itemClone();
	/**
	 * Renders the item entry in menus
	 * @param g graphics context
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param w width
	 * @param h height
	 */
	public default void render(Graphics g, int x, int y, int w, int h) {
		type().getTexture().draw(null, x, y, g, w, h);
	}

	public default WindowTool getTool() {
		return null;
	}
	public static BlockDrawer drawer(ItemEntry item) {
		return new BlockDrawer() {

			@Override
			public void draw(@Nil BlockEntry ent, int x, int y, Graphics g, int w, int h) {
				item.render(g, x, y, w, h);
			}

			@Override
			public Icon toIcon() {
				return item.icon();
			}

			@Override
			public int LOD() {
				return 0;
			}			
		};
	}
	/** @return an icon for this item entry */
	public default @NN Icon icon() {
		return new Icon() {
			@Override public int getIconHeight() {
				return 32;
			}
			@Override public int getIconWidth() {
				return 32;
			}
			@Override public void paintIcon(@Nil Component c, @SuppressWarnings("null") Graphics g, int x, int y) {
				render(g, x, y, 32, 32);
			}
		};
	}
	
	//Item entry ==> stack
	/**
	 * Creates an item stack
	 * @param amount number of the item in a stack
	 * @return an item stack with specified amount of given item
	 */
	@NN public default ItemStack stack(int amount) {
		return new ItemStack(this, amount);
	}
	
	//Single item
	@Deprecated(forRemoval = false) @Override
	/**
	 * @deprecated This method returns the same item, because it pertains to a single item
	 * @apiNote This method is used for compatibility with {@link SingleItem}
	 * @return this item
	 */
	default ItemEntry item() { //NOSONAR false positive
		return this;
	}
	/**
	 * @deprecated This method returns a constant 1, because it pertains to a single item
	 * @apiNote This method is used for compatibility with {@link SingleItem}
	 * @return the integer number 1. This is a single item
	 */
	@Deprecated(forRemoval = false) @Override
	default int amount() { //NOSONAR false positive
		return 1;
	}
	
	//Recipe output & drop methods
	@Override
	default boolean drop(@Nil InventoryWriter inv, @Nil World map, int x, int y) {
		return Chance.tryDrop(this, inv, map, x, y);
	}
	@Override
	default void represent(PicoWriter out) {
		out.write(title());
	}
	
	//Inventory management
	/**
	 * Invoked when the item changes inventories
	 * @param inv new inventory
	 */
	default void resetInventory(Inventory inv) {
		//unused
	}
	
	//Serialization
	/**
	 * Saves the item data, without the type
	 * @return null if item has no data,
	 * or a JSON node if data is present
	 */
	@Override
	public default @Nil JsonNode save() {
		return null;
	}
	
	//Static serialization
	/**
	 * @param item the item to save
	 * @return the JSON representation of this item entry
	 */
	public static JsonNode saveItem(@Nil ItemEntry item) {
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
	@Nil public static ItemEntry loadFromJson(@Nil JsonNode data) {
		return loadFromJsonExpectType(data, null);
	}
	/**
	 * Loads a non-stackable item from the JSON data, restricting the output type
	 * @param data JSON data
	 * @return item it if loaded successfully, or null if failed
	 */
	@Nil public static <T extends ItemEntry> ItemEntry loadFromJsonExpectType(@Nil JsonNode data, @Nil Class<T> cls) {
		if(data == null) return null;
		if(data.isNull()) return null;
		if(data.isArray()) {
			String id = data.get(0).asText();
			JsonNode idata = data.get(1);
			ItemType type = Items.get(id);
			if(type == null) return null;
			return type.<T>loadItemExpectType(idata, cls);
		}
		if(data.isTextual()) {
			String text = data.asText();
			ItemType item = Items.items.get(text);
			if(item == null) {
				new Debugger("ITEMS").printl("Invalid item: "+text);
				return null;
			}
			return item.create();
		}
		return null;
	}
	
}
