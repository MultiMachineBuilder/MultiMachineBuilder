/**
 * 
 */
package mmb.world.blocks.wireworld;

import mmb.world.block.BlockEntityDataless;
import mmb.world.block.BlockType;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.rotate.Side;
import mmb.world.worlds.MapProxy;

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