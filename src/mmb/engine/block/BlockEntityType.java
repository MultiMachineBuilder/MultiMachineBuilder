/**
 * 
 */
package mmb.engine.block;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

import mmb.NN;
import mmb.engine.chance.Chance;
import mmb.engine.texture.BlockDrawer;
import mmb.engine.worlds.world.World;

/**
 * @author oskar
 * This class represents a block entity types
 * <br> All block types can be used as simple items too.
 */
public class BlockEntityType extends BlockBase{
	/** Creates a Block Entity Type*/
	public BlockEntityType() {
		setVolume(0.02);
	}
	
	@Override
	public BlockEntry place(int x, int y, World map) {
		return map.place(this, x, y);
	}
	@Override
	public @NN BlockEntry createBlock() {
		return factory.get();
	}

	//Factory
	private @NN Supplier<@NN BlockEntry> factory = () -> {throw new IllegalStateException("Factory not initialized: "+id());};
	
	/**
	 * Sets a block factory
	 * @param factory the factory to set
	 */
	public void setFactory(Supplier<@NN BlockEntry> factory) {
		this.factory = factory;
	}
	
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture path to texture, starting from `/textures`
	 * @return this
	 */
	@Override
	@NN public BlockEntityType texture(String texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture texture
	 * @return this
	 */
	@Override
	@NN public BlockEntityType texture(BufferedImage texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture color
	 * @return this
	 */
	@Override
	@NN public BlockEntityType texture(Color texture) {
		setTexture(BlockDrawer.ofColor(texture));
		return this;
	}
	/**
	 * Sets texture. This is a convenience chainable method
	 * @param texture texture
	 * @return this
	 */
	@Override
	@NN public BlockEntityType texture(BlockDrawer texture) {
		setTexture(texture);
		return this;
	}
	/**
	 * Sets block factory.This is a convenience chainable method
	 * @param factory1 block entity factory lambda expression
	 * @return this
	 */
	@NN public BlockEntityType factory(Supplier<@NN BlockEntry> factory1) {
		setFactory(factory1);
		return this;
	}
	/**
	 * Sets title.This is a convenience chainable method
	 * @param title title
	 * @return this
	 */
	@Override
	@NN public BlockEntityType title(String title) {
		setTitle(title);
		return this;
	}
	/**
	 * Sets description.This is a convenience chainable method
	 * @param description description
	 * @return this
	 */
	@Override
	@NN public BlockEntityType describe(String description) {
		setDescription(description);
		return this;
	}
	/**
	 * Sets dropped item.This is a convenience chainable method
	 * @param drop drop
	 * @return this
	 */
	@Override
	@NN public BlockEntityType drop(Chance drop) {
		setDrop(drop);
		return this;
	}
	/**
	 * Registers this block. This is a convenience chainable method
	 * @param id block id
	 * @return this
	 */
	@Override
	@NN public BlockEntityType finish(String id) {
		register(id);
		return this;
	}
	/**
	 * Sets title.This is a convenience chainable method
	 * @param block title
	 * @return this
	 */
	@Override
	@NN public BlockEntityType leaveBehind(BlockType block) {
		setLeaveBehind(block);
		return this;
	}
	/**
	 * Sets volume. This is a convenience chainable method
	 * @param volume volume
	 * @return this
	 */
	@Override
	@NN public BlockEntityType volumed(double volume) {
		setVolume(volume);
		return this;
	}
}
