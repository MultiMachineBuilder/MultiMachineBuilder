/**
 * 
 */
package mmb.engine.block;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Objects;

import mmb.annotations.NN;
import mmb.engine.chance.Chance;
import mmb.engine.item.Item;
import mmb.engine.item.Items;
import mmb.engine.texture.BlockDrawer;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * Shared implementation for {@link Block} and {@link BlockEntityType}.
 * This class should not be subclassed by mods.
 * @author oskar
 */
public abstract class BlockBase extends Item implements BlockType {
	@Override
	public void preview(Graphics g, Point renderStartPos, World map, Point targetLocation, int side) {
		getTexture().draw(null, renderStartPos, g, side);
	}
	
	private BlockType leaveBehind;
	private Chance drop = this;

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
		Objects.requireNonNull(block, "leave behind is null");
		this.leaveBehind = block;
	}
	@Override
	public BlockType leaveBehind() {
		BlockType leave0 = leaveBehind;
		if(leave0 == null) {
			leaveBehind = Blocks.grass;
			return Blocks.grass;
		}
		return leave0;
	}
	@Override
	public Chance getDrop() {
		return drop;
	}
	@Override
	public void setDrop(Chance drop) {
		this.drop = Objects.requireNonNull(drop, "drop is null");
	}

	@Override
	public void register() {
		if(leaveBehind == null) leaveBehind = Blocks.grass; //NOSONAR
		if(drop == null) drop = this;
		Items.register(this);
	}
	@Override
	public void register(String id) {
		Objects.requireNonNull(id, "id is null");
		if(leaveBehind == null) leaveBehind = Blocks.grass;
		setID(id);
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
	 * @param newDrop drop
	 * @return this
	 */
	public BlockBase drop(Chance newDrop) {
		setDrop(newDrop);
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
	@NN public BlockBase volumed(double volume) {
		setVolume(volume);
		return this;
	}
	
	private int pickaxe = 0;
	@Override
	public int getRequiredPickaxe() {
		return pickaxe;
	}
	@Override
	public void setRequiredPickaxe(int level) {
		pickaxe = level;
	}
}
