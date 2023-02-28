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
public class WorldLoadDetector extends BlockEntityDataless{
	private boolean signal = true;
	private boolean hasrun = false;
	@Override
	public BlockType type() {
		return ContentsBlocks.ONLOAD;
	}

	@Override
	public boolean provideSignal(Side s) {
		hasrun = true;
		return signal;
	}

	@Override
	public void onTick(MapProxy map) {
		map.later(() -> signal = !hasrun&&signal);
	}
}