/**
 * 
 */
package mmb.WORLD.item;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;
import mmb.WORLD.items.ItemBase;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.texture.BlockDrawer;

/**
 * @author oskar
 *
 */
public class ItemEntityType extends ItemBase{
	
	private ItemEntityFactory factory;
	/**
	 * @author oskar
	 * This interface is used to define lambda expressions that produce item entitiess
	 */
	@FunctionalInterface public interface ItemEntityFactory{
		/**
		 * Creates an ItemEntity
		 * @return a new {@link ItemEntity}
		 */
		public ItemEntity create();
	}
	@Override
	public ItemEntry create() {
		return factory.create();
	}

	/**
	 * @return current item entity factory
	 */
	public ItemEntityFactory getFactory() {
		return factory;
	}

	/**
	 * @param factory new item entity factory
	 */
	public void setFactory(ItemEntityFactory factory) {
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
	@Nonnull public ItemEntityType volumed(double volume) {
		setVolume(volume);
		return this;
	}

	/**
	 * Sets factory. This is a convenience chainable method
	 * @param factory factory
	 * @return this
	 */
	public ItemEntityType factory(ItemEntityFactory factory) {
		this.factory = factory;
		return this;
	}
}
