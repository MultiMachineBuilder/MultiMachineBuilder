/**
 * 
 */
package mmb.WORLD.item;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.BEANS.Loader;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 *
 */
public abstract class ItemEntity implements ItemEntry, Loader<JsonNode> {
	private final ItemEntityType type;
	
	protected ItemEntity(ItemEntityType type) {
		this.type = type;
	}

	@Override
	public double volume() {
		return type.volume();
	}

	@Override
	public ItemType type() {
		return type;
	}
}
