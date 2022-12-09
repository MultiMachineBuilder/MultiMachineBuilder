/**
 * 
 */
package mmbgame.wireworld;

import mmbeng.block.BlockEntityDataless;
import mmbeng.block.BlockType;
import mmbeng.worlds.MapProxy;
import mmbgame.ContentsBlocks;

/**
 * @author oskar
 *
 */
public class WWTail extends BlockEntityDataless{

	@Override
	public BlockType type() {
		return ContentsBlocks.ww_tail;
	}

	@Override
	public void onTick(MapProxy map) {
		map.place(ContentsBlocks.ww_wire, posX(), posY());
	}
	
}