/**
 * 
 */
package mmb.content.wireworld;

import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockEntityDataless;
import mmb.engine.block.BlockType;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.WorldUtils;

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
