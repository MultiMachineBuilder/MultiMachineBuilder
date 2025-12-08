/**
 * 
 */
package mmb.content.wireworld;

import mmb.annotations.Nil;
import mmb.beans.BlockActivateListener;
import mmb.content.ContentsBlocks;
import mmb.engine.block.Block;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * A turned on toggle switch. Can be toggled manually or by a Block Clicking Claw.
 * @author oskar
 * @see OffToggle
 */
public class OnToggle extends Block implements BlockActivateListener {
	@Override
	public boolean provideSignal(Side s) {
		return true;
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		map.place(ContentsBlocks.OFF, blockX, blockY);
	}

}
