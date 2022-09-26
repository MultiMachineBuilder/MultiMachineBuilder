/**
 * 
 */
package mmb.WORLD.blocks.wireworld;

import mmb.WORLD.block.BlockEntityDataless;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;
import mmb.debug.Debugger;

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
