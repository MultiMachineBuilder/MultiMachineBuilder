/**
 * 
 */
package mmb.WORLD.blocks;

import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.Side;
import mmb.WORLD.block.Block;
import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 *
 */
public class OffToggle extends Block implements BlockActivateListener {
	@Override
	public boolean provideSignal(Side s) {
		return false;
	}

	@Override
	public void run(int blockX, int blockY, BlockMap map) {
		map.place(ContentsBlocks.ON, blockX, blockY);
	}

}
