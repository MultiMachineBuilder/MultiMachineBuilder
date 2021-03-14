/**
 * 
 */
package mmb.WORLD.blocks;

import mmb.WORLD.Side;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityDataless;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World.BlockMap;

/**
 * @author oskar
 *
 */
public class WWHead extends SkeletalBlockEntityDataless{
	public WWHead(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.ww_head;
	}

	@Override
	public boolean provideSignal(Side s) {
		return true;
	}

	@Override
	public void onTick(MapProxy map) {
		map.place(ContentsBlocks.ww_tail, x, y);
	}
	
}