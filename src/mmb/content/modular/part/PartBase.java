/**
 * 
 */
package mmbgame.modular.part;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;

import mmbeng.chance.Chance;
import mmbeng.craft.RecipeOutput;
import mmbeng.item.Item;
import mmbeng.texture.BlockDrawer;

/**
 * A base implementation of a part type
 * @author oskar
 */
public abstract class PartBase extends Item implements PartType {
	//Drops
	@Nonnull private Chance drop = Chance.NONE;
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
	@Nonnull private RecipeOutput rtp = this;
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
	@Nonnull public PartBase volumed(double volume) {
		setVolume(volume);
		return this;
	}
}
