/**
 * 
 */
package mmb.engine.worlds.world;

import mmb.NN;
import mmb.engine.block.BlockType;
import mmb.engine.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public class BlockChangeRequest {
	/** Block X coordinate*/
	public final int x;
	/** Block Y coordinate*/
	public final int y;
	/** Block to place */
	@NN public final BlockType block;
	
	/**
	 * Creates a block change request
	 * @param x X coordinate of the placement
	 * @param y Y coordinate of the placement
	 * @param block block to place
	 */
	public BlockChangeRequest(int x, int y, BlockType block) {
		super();
		this.x = x;
		this.y = y;
		this.block = block;
	}
	/**
	 * Applies changes to the world
	 * @param that
	 */
	public void apply(World that) {
		block.place(x, y, that);
	}
	void apply0(World that) {
		that.place0(block, x, y);
	}
	/**
	 * Applies changes to a map proxy
	 * @param mp
	 */
	public void apply(MapProxy mp) {
		mp.place(block, x, y);
	}
	/**
	 * Applies changes to a map proxy immediately
	 * @param mp
	 */
	public void applyImmediately(MapProxy mp) {
		mp.placeImmediately(block, x, y);
	}
}
