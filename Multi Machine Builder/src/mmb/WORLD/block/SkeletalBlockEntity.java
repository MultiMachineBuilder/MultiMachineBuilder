/**
 * 
 */
package mmb.WORLD.block;

import java.awt.Point;

import javax.annotation.Nonnull;

import mmb.WORLD.texture.BlockDrawer;
import mmb.WORLD.worlds.world.BlockMap;
import mmb.debug.Debugger;

/**
 * @author oskar
 * To be converted to interface
 * @discouraged For machines, use {@link mmb.WORLD.machine.Machine}. For blocks, use {@link BlockEntityType}
 */
public abstract class SkeletalBlockEntity implements BlockEntity {
	
	@Override
	public BlockEntity nasBlockEntity() {
		return this;
	}
	private static Debugger debug = new Debugger("BLOCKS");

	//Positioning
	public final int x, y;
	public Point getPosition() {
		return new Point(x, y);
	}
	//Rendering
	public BlockDrawer getTexture() {
		return type().getTexture();
	}
	//Constructor
	/**
	 * Intended for use in implementations
	 * Construct a BlockEntity with given position and map
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param owner map, in which the BlockEntity is located
	 */
	protected SkeletalBlockEntity(int x, int y, BlockMap owner) {
		this.x = x;
		this.y = y;
		this.owner = owner;
	}
	/**
	 * The map, in which the BlockEntity is located
	 */
	@Nonnull public final BlockMap owner;
	/**
	 * Checks if BlockEntity is of given type
	 * @param type type to check
	 * @return is given BlockEntity of given type?
	 */
	public boolean typeof(BlockEntityType type) {
		return type() == type;
	}

	@Override
	public final boolean isBlockEntity() {
		return true;
	}
	@Override
	public final BlockEntity asBlockEntity() {
		return this;
	}
	@Override
	public int posX() {
		return x;
	}
	@Override
	public int posY() {
		return y;
	}
	
	
}
