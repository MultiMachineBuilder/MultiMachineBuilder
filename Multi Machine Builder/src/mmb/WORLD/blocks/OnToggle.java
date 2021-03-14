/**
 * 
 */
package mmb.WORLD.blocks;

import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.Side;
import mmb.WORLD.block.Block;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public class OnToggle extends Block implements BlockActivateListener {
	@Override
	public boolean provideSignal(Side s) {
		return true;
	}

	@Override
	public void run(int blockX, int blockY, World map) {
		map.place(ContentsBlocks.OFF, blockX, blockY);
	}

}
