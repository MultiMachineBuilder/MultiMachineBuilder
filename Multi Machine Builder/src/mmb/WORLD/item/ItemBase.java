/**
 * 
 */
package mmb.WORLD.item;

import mmb.WORLD.texture.BlockDrawer;

/**
 * @author oskar
 *
 */
public abstract class ItemBase implements ItemType {

	@Override
	public String toString() {
		return "Item " + title + "(" + id + ")";
	}

	/**
	 * Register this item with new ID
	 * @param id new identifier
	 * @throws NullPointerException if ID is null
	 */
	public void register(String id) {
		this.id = id;
		Items.register(this);
	}

	/**
	 * Register this item with current ID.
	 * @throws NullPointerException if ID is null
	 */
	public void register() {
		Items.register(this);
	}

	/**
	 * The volume which item takes up
	 * @deprecated Deprecated in favor of encapsulation. Use {@link #setVolume(double)} and {@link #volume()} instead
	 */
	@Deprecated
	public double volume = 0.02;
	/** 
	 * A description contains extra information about the item, which can be used by their users.
	 * @deprecated Deprecated in favor of encapsulation. {@link #setDescription(String)} and {@link #description()} instead
	 */
	@Deprecated
	public String description;

	@Override
	public String description() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * The texture determines the block's or items's looks.
	 * <br> If texture is null, the block or item won't be drawn.
	 * @deprecated Deprecated in favor of encapsulation. {@link #setTexture(BlockDrawer)} and {@link #getTexture()} instead
	 */
	@Deprecated
	public BlockDrawer drawer;

	@Override
	public void setTexture(BlockDrawer texture) {
		drawer = texture;
	}

	@Override
	public BlockDrawer getTexture() {
		return drawer;
	}

	/**
	 * The identifier is an unique name used in code. If identifier is null, a {@link NullPointerException} is thrown.
	 * @deprecated Deprecated in favor of encapsulation. {@link #setID(String)} and {@link #id()} instead
	 */
	@Deprecated
	public String id;

	@Override
	public void setID(String id) {
		this.id = id;
	}

	@Override
	public String id() {
		return id;
	}

	/**
	 * A title is name which is displayed in toolbars. If title is null, it will be set to the ID.
	 * @deprecated Deprecated in favor of encapsulation. {@link #setTitle(String)} and {@link #title()} instead
	 */
	@Deprecated
	public String title;

	@Override
	public String title() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public double volume() {
		return volume;
	}

	@Override
	public void setVolume(double volume) {
		this.volume = volume;
	}

}
