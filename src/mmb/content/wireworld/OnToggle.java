/**
 * 
 */
package mmb.content.wireworld;

import javax.annotation.Nullable;

import mmb.content.ContentsBlocks;
import mmb.engine.block.Block;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.world.World;
import mmbbase.cgui.BlockActivateListener;
import mmbbase.menu.world.window.WorldWindow;

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
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		map.place(ContentsBlocks.OFF, blockX, blockY);
	}

}
