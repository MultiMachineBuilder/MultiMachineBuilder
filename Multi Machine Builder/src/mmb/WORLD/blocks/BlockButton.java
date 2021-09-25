/**
 * 
 */
package mmb.WORLD.blocks;

import javax.annotation.Nullable;

import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.BlockEntityDataless;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;

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
