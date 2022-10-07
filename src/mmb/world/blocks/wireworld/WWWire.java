/**
 * 
 */
package mmb.world.blocks.wireworld;

import mmb.world.block.BlockEntityDataless;
import mmb.world.block.BlockType;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.worlds.MapProxy;
import mmb.world.worlds.world.WorldUtils;

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
