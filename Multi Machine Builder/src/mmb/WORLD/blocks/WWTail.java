/**
 * 
 */
package mmb.WORLD.blocks;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityDataless;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 *
 */
public class WWTail extends SkeletalBlockEntityDataless{
	public WWTail(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.ww_tail;
	}

	@Override
	public void onTick(MapProxy map) {
		map.place(ContentsBlocks.ww_wire, x, y);
	}
	
}