/**
 * 
 */
package mmb.world.worlds.world;

import javax.annotation.Nonnull;

import mmb.world.block.BlockType;
import mmb.world.worlds.MapProxy;

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
	void apply0(World that) {
		that.place0(block, x, y);
	}
	public void apply(MapProxy mp) {
		mp.place(block, x, y);
	}
	public void applyImmediately(MapProxy mp) {
		mp.placeImmediately(block, x, y);
	}
}
