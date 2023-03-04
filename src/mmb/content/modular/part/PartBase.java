/**
 * 
 */
package mmb.content.modular.part;

import java.awt.Color;
import java.awt.image.BufferedImage;

import mmb.NN;
import mmb.engine.chance.Chance;
import mmb.engine.item.Item;
import mmb.engine.recipe.RecipeOutput;
import mmb.engine.texture.BlockDrawer;

/**
 * A base implementation of a part type
 * @author oskar
 */
public abstract class PartBase extends Item implements PartType {
	//Drops
	@NN private Chance drop = Chance.NONE;
	/**
	 * Sets the item drops
	 * @param newDrop new drops
	 * @return this
	 */
	public PartBase drop(Chance newDrop) {
		drop = newDrop;
		return this;
	}
	@Override
	public Chance dropItems() {
		return drop;
	}

	//RTP
	@NN private RecipeOutput rtp = this;
	/**
	 * Sets the Return to Player
	 * @param newrtp new return to player
	 * @return this
	 */
	public PartBase rtp(RecipeOutput newrtp) {
		rtp = newrtp;
		return this;
	}
	@Override
	public RecipeOutput returnToPlayer() {
		return rtp;
	}
	
	//Item methods
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture path to texture, starting from `/textures`
	 * @return this
	 */
	@Override
	public PartBase texture(String texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture texture
	 * @return this
	 */
	@Override
	public PartBase texture(BufferedImage texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture color
	 * @return this
	 */
	@Override
	public PartBase texture(Color texture) {
		setTexture(BlockDrawer.ofColor(texture));
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture texture
	 * @return this
	 */
	@Override
	public PartBase texture(BlockDrawer texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets title.This is a convenience chainable method
	 * @param title title
	 * @return this
	 */
	@Override
	public PartBase title(String title) {
		setTitle(title);
		return this;
	}
	/**
	 * Sets description.This is a convenience chainable method
	 * @param description description
	 * @return this
	 */
	@Override
	public PartBase describe(String description) {
		setDescription(description);
		return this;
	}
	/**
	 * Registers this block. This is a convenience chainable method
	 * @param id block id
	 * @return this
	 */
	@Override
	public PartBase finish(String id) {
		register(id);
		return this;
	}
	/**
	 * Sets volume. This is a convenience chainable method
	 * @param volume volume
	 * @return this
	 */
	@Override
	@NN public PartBase volumed(double volume) {
		setVolume(volume);
		return this;
	}
}
