/**
 * 
 */
package mmb.WORLD.blocks.wireworld;

import mmb.WORLD.block.BlockEntityDataless;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.worlds.MapProxy;

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