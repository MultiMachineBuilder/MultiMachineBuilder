/**
 * 
 */
package mmb.content.wireworld;

import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockEntityDataless;
import mmb.engine.block.BlockType;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;

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