/**
 * 
 */
package mmb.WORLD.blocks;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityDataless;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.SignalUtils;
import mmb.WORLD.worlds.world.BlockMap;

/**
 * @author oskar
 *
 */
public class WWWire extends SkeletalBlockEntityDataless {
	public WWWire(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.ww_wire;
	}

	@Override
	public void onTick(MapProxy map) {
		int count = SignalUtils.allIncomingSignals(x, y, owner);
		if(count == 1 || count == 2) {
			map.place(ContentsBlocks.ww_head, x, y);
		}
	}
	
}
