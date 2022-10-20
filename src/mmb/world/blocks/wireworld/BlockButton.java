/**
 * 
 */
package mmb.world.blocks.wireworld;

import javax.annotation.Nullable;

import mmb.cgui.BlockActivateListener;
import mmb.menu.world.window.WorldWindow;
import mmb.world.block.BlockEntityDataless;
import mmb.world.block.BlockType;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.rotate.Side;
import mmb.world.worlds.MapProxy;
import mmb.world.worlds.world.World;

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
