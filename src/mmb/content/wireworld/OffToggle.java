/**
 * 
 */
package mmbgame.wireworld;

import javax.annotation.Nullable;

import mmb.cgui.BlockActivateListener;
import mmb.menu.world.window.WorldWindow;
import mmbeng.block.Block;
import mmbeng.rotate.Side;
import mmbeng.worlds.world.World;
import mmbgame.ContentsBlocks;

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
