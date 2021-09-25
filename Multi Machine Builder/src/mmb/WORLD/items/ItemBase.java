/**
 * 
 */
package mmb.WORLD.items;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.item.ItemType;
import mmb.WORLD.item.Items;
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
	public void setTexture(@Nonnull BlockDrawer texture) {
		drawer = texture;
	}
	public void setTexture(BufferedImage texture) {
		drawer = BlockDrawer.ofImage(texture);
	}
	public void setTexture(String texture) {
		setTexture(Textures.get(texture));
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

	/**
	 * The volume which item takes up
	 * @deprecated Deprecated in favor of encapsulation. Use {@link #setVolume(double)} and {@link #volume()} instead
	 */
	@Deprecated
	public double volume = 0.02;
	@Override
	public double volume() {
		return volume;
	}
	@Override
	public void setVolume(double volume) {
		this.volume = volume;
	}

	
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture path to texture, starting from `/textures`
	 * @return this
	 */
	@Nonnull public ItemBase texture(String texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture texture
	 * @return this
	 */
	@Nonnull public ItemBase texture(BufferedImage texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture color
	 * @return this
	 */
	@Nonnull public ItemBase texture(Color texture) {
		setTexture(BlockDrawer.ofColor(texture));
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture texture
	 * @return this
	 */
	@Nonnull public ItemBase texture(BlockDrawer texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets title.This is a convenience chainable method
	 * @param title title
	 * @return this
	 */
	@Nonnull public ItemBase title(String title) {
		setTitle(title);
		return this;
	}
	/**
	 * Sets description.This is a convenience chainable method
	 * @param description description
	 * @return this
	 */
	@Nonnull public ItemBase describe(String description) {
		setDescription(description);
		return this;
	}
	/**
	 * Registers this item. This is a convenience chainable method
	 * @param id block id
	 * @return this
	 */
	@Nonnull public ItemBase finish(String id) {
		register(id);
		return this;
	}
	/**
	 * Sets volume. This is a convenience chainable method
	 * @param volume volume
	 * @return this
	 */
	@Nonnull public ItemBase volumed(double volume) {
		this.volume = volume;
		return this;
	}

	
	private boolean stacks;
	@Override
	public void setUnstackable(boolean value) {
		stacks = value;
	}

	@Override
	public boolean isUnstackable() {
		return stacks;
	}
}
