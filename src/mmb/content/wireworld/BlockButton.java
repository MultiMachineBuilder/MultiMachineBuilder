/**
 * 
 */
package mmbgame.wireworld;

import javax.annotation.Nullable;

import mmb.cgui.BlockActivateListener;
import mmb.menu.world.window.WorldWindow;
import mmbeng.block.BlockEntityDataless;
import mmbeng.block.BlockType;
import mmbeng.rotate.Side;
import mmbeng.worlds.MapProxy;
import mmbeng.worlds.world.World;
import mmbgame.ContentsBlocks;

/**
 * @author oskar
 *
 */
public class BlockButton extends BlockEntityDataless implements BlockActivateListener {
	private int pressed;

	@Override
	public BlockType type() {
		return ContentsBlocks.BUTTON;
	}

	@Override
	public void onTick(MapProxy map) {
		if(pressed > 0) pressed--;
	}

	@Override
	public boolean provideSignal(Side s) {
		return pressed != 0;
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		pressed = 2;
	}

}
