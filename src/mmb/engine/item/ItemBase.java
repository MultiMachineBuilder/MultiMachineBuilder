/**
 * 
 */
package mmb.engine.item;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Objects;

import javax.swing.Icon;

import mmb.NN;
import mmb.engine.Verify;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.texture.BlockDrawer;
import mmb.engine.texture.Textures;

/**
 * Shared implementation of {@code Item} and {@code ItemEntityType}
 * @author oskar
 */
public abstract class ItemBase implements ItemType {

	private double volume = 0.0002;
	@Override
	public double volume() {
		return volume;
	}
	@Override
	public void setVolume(double volume) {
		Verify.requirePositive(volume);
		this.volume = volume;
	}
	
	/**
	 * Sets volume. This is a convenience chainable method
	 * @param volume volume
	 * @return this
	 */
	@NN public ItemBase volumed(double volume) {
		this.volume = volume;
		return this;
	}

	private boolean stacks;
	@NN private static final String description0 = GlobalSettings.$res("nodescr");
	@NN private String description = description0;
	/**
	 * The texture determines the block's or items's looks.
	 * <br> If texture is null, the block or item won't be drawn.
	 */
	private BlockDrawer drawer;
	@Override
	public Icon getIcon() {
		if(drawer == null) return null;
		return getTexture().toIcon();
	}
	
	private String id;
	@NN private String title = "000 Unnamed Item";
	@Override
	public void setUnstackable(boolean value) {
		stacks = value;
	}

	@Override
	public boolean isUnstackable() {
		return stacks;
	}
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
	@Override
	public String description() {
		return description;
	}
	@Override
	public void setDescription(String description) {
		this.description = GlobalSettings.$str1(description);
	}
	@Override
	public void setTexture(@NN BlockDrawer texture) {
		Objects.requireNonNull(texture, "texture is null");
		drawer = texture;
	}
	public void setTexture(BufferedImage texture) {
		drawer = BlockDrawer.ofImage(texture);
	}
	public void setTexture(String texture) {
		Objects.requireNonNull(texture, "texture is null");
		setTexture(Textures.get(texture));
	}
	@Override
	public BlockDrawer getTexture() {
		BlockDrawer tex = drawer;
		if(tex == null) throw new IllegalStateException("Texture not set");
		return tex;
	}
	@Override
	public void setID(String id) {
		this.id = id;
	}
	@Override
	public String id() {
		return id;
	}
	@Override
	public String title() {
		return title;
	}
	@Override
	public void setTitle(String title) {
		this.title = GlobalSettings.$str1(title);
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture path to texture, starting from `/textures`
	 * @return this
	 */
	@NN
	public ItemBase texture(String texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture texture
	 * @return this
	 */
	@NN
	public ItemBase texture(BufferedImage texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture color
	 * @return this
	 */
	@NN
	public ItemBase texture(Color texture) {
		setTexture(BlockDrawer.ofColor(texture));
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture texture
	 * @return this
	 */
	@NN
	public ItemBase texture(BlockDrawer texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets title.This is a convenience chainable method
	 * @param title title
	 * @return this
	 */
	@NN
	public ItemBase title(String title) {
		setTitle(title);
		return this;
	}
	/**
	 * Sets description.This is a convenience chainable method
	 * @param description description
	 * @return this
	 */
	@NN
	public ItemBase describe(String description) {
		setDescription(description);
		return this;
	}
	/**
	 * Registers this item. This is a convenience chainable method
	 * @param id block id
	 * @return this
	 */
	@NN
	public ItemBase finish(String id) {
		register(id);
		return this;
	}
}
