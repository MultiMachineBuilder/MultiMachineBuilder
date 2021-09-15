/**
 * 
 */
package mmb.WORLD.worlds;

import javax.annotation.Nonnull;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public class BlockChangeRequest {
	public final int x, y;
	@Nonnull public final BlockType block;
	public BlockChangeRequest(int x, int y, BlockType block) {
		super();
		this.x = x;
		this.y = y;
		this.block = block;
	}
	public void apply(World that) {
		block.place(x, y, that);
	}
	public void apply(MapProxy mp) {
		mp.place(block, x, y);
	}
	public void applyImmediately(MapProxy mp) {
		mp.placeImmediately(block, x, y);
	}
}
