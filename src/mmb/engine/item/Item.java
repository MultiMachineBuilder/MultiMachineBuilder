/**
 * 
 */
package mmb.engine.item;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.engine.debug.Debugger;
import mmb.engine.texture.BlockDrawer;
import monniasza.collects.Identifiable;

/**
 * A basic item.
 * <br> To create an item, specify title, texture and register the item. Description and volume may also be specified
 * @author oskar
 */
public class Item extends ItemBase implements ItemEntry {
	private static final Debugger debug = new Debugger("ITEMS");
	/**
	 * Get the hash code, which is always the same as ID's hash code.
	 * @return item's hash code.
	 * @see java.lang.String#hashCode()
	 */
	@Override
	public final int hashCode() {
		return id().hashCode();
	}
	@Override
	public final boolean equals(@Nil Object other) {
		if(this == other) return true;
		if(other == null) return false;
		if(!(other instanceof Identifiable)) return false;
		Object id1 = ((Identifiable<?>)other).id();
		if(id() == id1) return true;
		if(id1 == null) return false;
		return id1.equals(id());
	}

	@Override
	public ItemEntry create() {
		return this;
	}
	@Override
	public ItemEntry itemClone() {
		return this;
	}
	@Override
	public ItemType type() {
		return this;
	}
	@Override
	public void load(@Nil JsonNode node) {
		debug.printl("Attempting to load a non-data item");
	}

	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture path to texture, starting from `/textures`
	 * @return this
	 */
	@Override
	@NN public Item texture(String texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture texture
	 * @return this
	 */
	@Override
	@NN public Item texture(BufferedImage texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture color
	 * @return this
	 */
	@Override
	@NN public Item texture(Color texture) {
		setTexture(BlockDrawer.ofColor(texture));
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture texture
	 * @return this
	 */
	@Override
	@NN public Item texture(BlockDrawer texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets title.This is a convenience chainable method
	 * @param title title
	 * @return this
	 */
	@Override
	@NN public Item title(String title) {
		setTitle(title);
		return this;
	}
	/**
	 * Sets description.This is a convenience chainable method
	 * @param description description
	 * @return this
	 */
	@Override
	@NN public Item describe(String description) {
		setDescription(description);
		return this;
	}
	/**
	 * Registers this item. This is a convenience chainable method
	 * @param id block id
	 * @return this
	 */
	@Override
	@NN public Item finish(String id) {
		register(id);
		return this;
	}
	/**
	 * Sets volume. This is a convenience chainable method
	 * @param volume volume
	 * @return this
	 */
	@Override
	@NN public Item volumed(double volume) {
		setVolume(volume);
		return this;
	}
}
