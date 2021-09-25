/**
 * 
 */
package mmb.WORLD.blocks;

import javax.annotation.Nullable;

import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.block.Block;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.world.World;

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
