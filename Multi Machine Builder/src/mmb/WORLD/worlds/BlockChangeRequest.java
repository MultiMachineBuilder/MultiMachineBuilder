/**
 * 
 */
package mmb.WORLD.worlds;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 *
 */
public class BlockChangeRequest {
	public final int x, y;
	public final BlockType block;
	public BlockChangeRequest(int x, int y, BlockType block) {
		super();
		this.x = x;
		this.y = y;
		this.block = block;
	}
	public void apply(BlockMap map) {
		block.place(x, y, map);
	}
	public void apply(MapProxy mp) {
		mp.place(block, x, y);
	}
	public void applyImmediately(MapProxy mp) {
		mp.placeImmediately(block, x, y);
	}
}
