/**
 * 
 */
package mmb.world.blocks.wireworld;

import mmb.debug.Debugger;
import mmb.world.block.BlockEntityDataless;
import mmb.world.block.BlockType;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.rotate.Side;
import mmb.world.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public class WorldLoadDetector extends BlockEntityDataless{
	private boolean signal = true;
	private boolean hasrun = false; //not set properly
	private static final Debugger debug = new Debugger("ON LOAD SIGNAL PROVIDER");
	@Override
	public BlockType type() {
		return ContentsBlocks.ONLOAD;
	}

	@Override
	public boolean provideSignal(Side s) {
		if(signal) debug.printl("Signal provided");
		hasrun = true;
		return signal;
	}

	@Override
	public void onTick(MapProxy map) {
		map.later(() -> signal = !hasrun&&signal);
		if(hasrun) debug.printl("Signal provided");
	}
}
