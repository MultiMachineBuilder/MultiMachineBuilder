/**
 * 
 */
package mmb.WORLD.blocks;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.block.BlockEntityDataless;
import mmb.WORLD.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public class WWHead extends BlockEntityDataless{

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
		map.place(ContentsBlocks.ww_tail, posX(), posY());
	}
	
}