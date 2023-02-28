/**
 * 
 */
package mmb.content.wireworld;

import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockEntityDataless;
import mmb.engine.block.BlockType;
import mmb.engine.worlds.MapProxy;

/**
 * Turns into a wire
 * @author oskar
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