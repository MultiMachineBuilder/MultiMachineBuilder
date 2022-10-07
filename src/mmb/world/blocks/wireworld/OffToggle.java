/**
 * 
 */
package mmb.world.blocks.wireworld;

import javax.annotation.Nullable;

import mmb.beans.BlockActivateListener;
import mmb.menu.world.window.WorldWindow;
import mmb.world.block.Block;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.rotate.Side;
import mmb.world.worlds.world.World;

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
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		map.place(ContentsBlocks.ON, blockX, blockY);
	}

}
