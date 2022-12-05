/**
 * 
 */
package mmbgame.wireworld;

import mmbeng.block.BlockEntityDataless;
import mmbeng.block.BlockType;
import mmbeng.rotate.Side;
import mmbeng.worlds.MapProxy;
import mmbgame.ContentsBlocks;

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