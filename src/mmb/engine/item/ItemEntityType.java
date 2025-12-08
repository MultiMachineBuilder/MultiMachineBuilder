/**
 * 
 */
package mmb.engine.item;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

import mmb.annotations.NN;
import mmb.engine.texture.BlockDrawer;

/**
 * An item type for item entities.
 * To create an item entity type specify the title, texture, factory and register the item entity type
 * @author oskar
 */
public class ItemEntityType extends ItemBase{
	/** Creates a new Item Entity Type */
	public ItemEntityType() {
		setVolume(0.002);
	}
	
	private Supplier<@NN ItemEntry> factory;
	@Override
	public ItemEntry create() {
		return factory.get();
	}
	/**
	 * @return current item entity factory
	 */
	public Supplier<@NN ItemEntry> getFactory() {
		return factory;
	}
	/**
	 * @param factory new item entity factory
	 */
	public void setFactory(Supplier<@NN ItemEntry> factory) {
		this.factory = factory;
	}

	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture path to texture, starting from `/textures`
	 * @return this
	 */
	@Override
	public ItemEntityType texture(String texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture texture
	 * @return this
	 */
	@Override
	public ItemEntityType texture(BufferedImage texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture color
	 * @return this
	 */
	@Override
	public ItemEntityType texture(Color texture) {
		setTexture(BlockDrawer.ofColor(texture));
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture texture
	 * @return this
	 */
	@Override
	public ItemEntityType texture(BlockDrawer texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets title.This is a convenience chainable method
	 * @param title title
	 * @return this
	 */
	@Override
	public ItemEntityType title(String title) {
		setTitle(title);
		return this;
	}
	/**
	 * Sets description.This is a convenience chainable method
	 * @param description description
	 * @return this
	 */
	@Override
	public ItemEntityType describe(String description) {
		setDescription(description);
		return this;
	}
	/**
	 * Registers this item. This is a convenience chainable method
	 * @param id block id
	 * @return this
	 */
	@Override
	public ItemEntityType finish(String id) {
		register(id);
		return this;
	}
	/**
	 * Sets volume. This is a convenience chainable method
	 * @param volume volume
	 * @return this
	 */
	@Override
	@NN public ItemEntityType volumed(double volume) {
		setVolume(volume);
		return this;
	}
	/**
	 * Sets factory. This is a convenience chainable method
	 * @param factory1 factory
	 * @return this
	 */
	public ItemEntityType factory(Supplier<@NN ItemEntry> factory1) {
		this.factory = factory1;
		return this;
	}
}
