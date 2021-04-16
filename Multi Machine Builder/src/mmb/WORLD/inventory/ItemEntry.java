/**
 * 
 */
package mmb.WORLD.inventory;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.BEANS.Saver;
import mmb.WORLD.item.ItemType;

/**
 * @author oskar
 * An item entry representing a single unit of item
 */
public interface ItemEntry extends Saver<JsonNode> {

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
	@Nullable default public JsonNode save() {
		return null;
	}
}
