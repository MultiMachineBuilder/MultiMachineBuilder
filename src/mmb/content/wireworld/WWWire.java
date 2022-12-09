/**
 * 
 */
package mmbgame.wireworld;

import mmbeng.block.BlockEntityDataless;
import mmbeng.block.BlockType;
import mmbeng.worlds.MapProxy;
import mmbeng.worlds.world.WorldUtils;
import mmbgame.ContentsBlocks;

/**
 * @author oskar
 *
 */
public class WWWire extends BlockEntityDataless {

	@Override
	public BlockType type() {
		return ContentsBlocks.ww_wire;
	}

	@Override
	public void onTick(MapProxy map) {
		int count = WorldUtils.allIncomingSignals(posX(), posY(), owner());
		if(count == 1 || count == 2) {
			map.place(ContentsBlocks.ww_head, posX(), posY());
		}
	}
	
}
