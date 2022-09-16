/**
 * 
 */
package mmb.WORLD.items;

import java.awt.Component;
import java.awt.Graphics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Icon;

import org.ainslec.picocog.PicoWriter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;

import mmb.BEANS.Saver;
import mmb.DATA.json.JsonTool;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.chance.Chance;
import mmb.WORLD.crafting.SingleItem;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.item.Items;
import mmb.WORLD.texture.BlockDrawer;
import mmb.WORLD.tool.WindowTool;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 * An item entry representing a single unit of item
 */
public interface ItemEntry extends Saver<@Nullable JsonNode>, SingleItem{	
	
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
	@Nonnull public ItemType type();
	public default String title() {
		return type().title();
	}
	public default String description() {
		return type().description();
	}
	public default boolean exists() {
		return true;
	}
	/**
	 * Saves the item data.
	 * 
	 * @return null if item has no data
	 * , or a JSON node if data is present
	 */
	@Override
	public default @Nullable JsonNode save() {
		return null;
	}
	
	public default void render(Graphics g, int x, int y, int w, int h) {
		type().getTexture().draw(null, x, y, g, w, h);
	}

	public default WindowTool getTool() {
		return null;
	}
	public static BlockDrawer drawer(ItemEntry item) {
		return new BlockDrawer() {

			@Override
			public void draw(@Nullable BlockEntry ent, int x, int y, Graphics g, int w, int h) {
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
	public default @Nonnull Icon icon() {
		return new Icon() {
			@Override public int getIconHeight() {
				return 32;
			}
			@Override public int getIconWidth() {
				return 32;
			}
			@Override public void paintIcon(@Nullable Component c, @SuppressWarnings("null") Graphics g, int x, int y) {
				render(g, x, y, 32, 32);
			}
		};
	}
	
	//Item entry ==> stack
	/**
	 * @return an item stack with one unit of this item
	 */
	@Nonnull public default ItemStack single() {
		return new ItemStack(this, 1);
	}
	/**
	 * @param amount
	 * @return an item stack with specified amount of given item
	 */
	@Nonnull public default ItemStack stack(int amount) {
		return new ItemStack(this, amount);
	}
	
	//Recipe output & drop methods
	@Override
	default boolean drop(@Nullable InventoryWriter inv, @Nullable World map, int x, int y) {
		return Chance.tryDrop(this, inv, map, x, y);
	}
	@Override
	default void represent(PicoWriter out) {
		out.write(title());
	}
	
	//Inventory management
	/**
	 * @param inv
	 */
	default void resetInventory(Inventory inv) {
		//unused
	}
	
	//Serialization
	/**
	 * @param item the item to save
	 * @return the JSON representation of this item entry
	 */
	public static JsonNode saveItem(@Nullable ItemEntry item) {
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
	@SuppressWarnings("null")
	@Nullable public static ItemEntry loadFromJson(@Nullable JsonNode data) {
		if(data == null) return null;
		if(data.isNull()) return null;
		if(data.isArray()) {
			String id = data.get(0).asText();
			JsonNode idata = data.get(1);
			ItemType type = Items.get(id);
			if(type == null) return null;
			return type.loadItem(idata);
		}
		if(data.isTextual()) {
			return Items.items.get(data.asText()).create();
		}
		return null;
	}
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
	default int amount() {
		return 1;
	}
}
