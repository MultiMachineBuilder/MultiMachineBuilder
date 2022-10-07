/**
 * 
 */
package mmb.world.blocks.wireworld;

import mmb.world.block.BlockEntityDataless;
import mmb.world.block.BlockType;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.worlds.MapProxy;

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