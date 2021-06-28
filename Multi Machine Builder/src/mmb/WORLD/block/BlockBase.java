/**
 * 
 */
package mmb.WORLD.block;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Objects;

import javax.annotation.Nonnull;

import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.item.Item;
import mmb.WORLD.item.ItemEntityType;
import mmb.WORLD.texture.BlockDrawer;

/**
 * @author oskar
 * Shared implementation for {@link Block} and {@link BlockEntityType}. More types will be added in future
 */
public abstract class BlockBase extends Item implements BlockType {
	@Deprecated
	public BlockType leaveBehind;
	@Deprecated
	public BlockDrawer texture;
	private Drop drop;

	@Override
	public void openGUI(WorldWindow window) {
		//unused
	}

	@Override
	public void closeGUI(WorldWindow window) {
		//unused
	}

	@Override
	public void setLeaveBehind(BlockType block) {
		this.leaveBehind = block;
	}
	@Override
	public BlockType leaveBehind() {
		return leaveBehind;
	}
	@Override
	public Drop getDrop() {
		return drop;
	}
	@Override
	public void setDrop(Drop drop) {
		this.drop = Objects.requireNonNull(drop, "drop is null");
	}

	@Override
	public void register() {
		if(leaveBehind == null) leaveBehind = ContentsBlocks.grass; //NOSONAR
		if(drop == null) drop = (i, m, x, y) -> Drop.tryDrop(this, i, m, x, y);
		Blocks.register(this);
	}
	@Override
	public void register(String id) {
		this.id = id;
		register();
	}
	
	

	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture path to texture, starting from `/textures`
	 * @return this
	 */
	@Override
	public BlockBase texture(String texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture texture
	 * @return this
	 */
	@Override
	public BlockBase texture(BufferedImage texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture color
	 * @return this
	 */
	@Override
	public BlockBase texture(Color texture) {
		setTexture(BlockDrawer.ofColor(texture));
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture texture
	 * @return this
	 */
	@Override
	public BlockBase texture(BlockDrawer texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets title.This is a convenience chainable method
	 * @param title title
	 * @return this
	 */
	@Override
	public BlockBase title(String title) {
		setTitle(title);
		return this;
	}
	/**
	 * Sets description.This is a convenience chainable method
	 * @param description description
	 * @return this
	 */
	@Override
	public BlockBase describe(String description) {
		setDescription(description);
		return this;
	}
	/**
	 * Sets dropped item.This is a convenience chainable method
	 * @param drop drop
	 * @return this
	 */
	public BlockBase drop(Drop drop) {
		setDrop(drop);
		return this;
	}
	/**
	 * Registers this block. This is a convenience chainable method
	 * @param id block id
	 * @return this
	 */
	@Override
	public BlockBase finish(String id) {
		register(id);
		return this;
	}
	/**
	 * Sets title.This is a convenience chainable method
	 * @param block title
	 * @return this
	 */
	public BlockBase leaveBehind(BlockType block) {
		setLeaveBehind(block);
		return this;
	}
	/**
	 * Sets volume. This is a convenience chainable method
	 * @param volume volume
	 * @return this
	 */
	@Override
	@Nonnull public BlockBase volumed(double volume) {
		this.volume = volume;
		return this;
	}
}
