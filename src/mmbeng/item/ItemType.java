/**
 * 
 */
package mmbeng.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Icon;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.beans.Titled;
import mmbeng.texture.BlockDrawer;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 *
 */
public interface ItemType extends Titled, Identifiable<String>{
	/**
	 * @return the volume which item takes up
	 */
	public double volume();
	/**
	 * Sets the item's volume
	 * @param volume new volume
	 */
	public void setVolume(double volume);
	
	/**
	 * Sets the item's description
	 * @param description new description
	 */
	public void setDescription(String description);
	/**
	 * A description contains extra information about the item, which can be used by their users.
	 * @return item's description
	 */
	@Nonnull public String description();
	
	/**
	 * Sets the ID. For use only in registration
	 * @param id new id
	 */
	public void setID(String id);
	@Override
	public String id();
	
	/**
	 * Sets the title
	 * @param title new title
	 */
	public void setTitle(String title);
	/**
	 * A title is name which is displayed in toolbars. If title is null, it will be set to the ID.
	 * @return this item's title
	 */
	@Override
	public String title();
	
	public void setTexture(BlockDrawer texture);
	@Nonnull public BlockDrawer getTexture();
	public default Icon getIcon() {
		return getTexture().toIcon();
	}
	
	/**
	 * @return a new instance of the item
	 */
	@Nonnull public ItemEntry create();
	/**
	 * Loads an item entry using JSON payload
	 * @param node data to load from
	 * @return a new item entry with data
	 */
	@Nonnull public default ItemEntry loadItem(@Nullable JsonNode node) {
		ItemEntry item = create();
		item.load(node);
		return item;
	}
	/**
	 * Loads an item entry using JSON payload, restricting the output type
	 * @param <T> expected type
	 * @param node data to load from
	 * @param cls expected type
	 * @return a new item entry with data, or null if failed
	 */
	@Nullable public default <T extends ItemEntry> ItemEntry loadItemExpectType(@Nullable JsonNode node, @Nullable Class<T> cls) {
		ItemEntry item = create();
		if(cls != null && !cls.isInstance(item)) return null;
		item.load(node);
		return item;
	}

	/**
	 * @param value should this item be unstackable?
	 */
	public void setUnstackable(boolean value);
	/**
	 * @return is this item unstackable?
	 */
	public boolean isUnstackable();
	
	/** Invoked when item is registered */
	default void onregister() {
		//unused
	}
}
