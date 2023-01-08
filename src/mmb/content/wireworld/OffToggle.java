/**
 * 
 */
package mmb.content.wireworld;

import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.content.ContentsBlocks;
import mmb.engine.block.Block;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

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
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		map.place(ContentsBlocks.ON, blockX, blockY);
	}

}
