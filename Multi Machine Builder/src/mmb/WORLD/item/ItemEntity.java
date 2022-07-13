/**
 * 
 */
package mmb.WORLD.item;

import javax.annotation.Nonnull;

import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 *
 */
public abstract class ItemEntity implements ItemEntry{
	@Nonnull private final ItemEntityType type;
	
	protected ItemEntity(ItemEntityType type) {
		this.type = type;
	}

	@Override
	public double volume() {
		return type.volume();
	}

	@Override
	public ItemEntityType type() {
		return type;
	}
}
