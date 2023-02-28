/**
 * 
 */
package mmb.content.wireworld;

import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockEntityDataless;
import mmb.engine.block.BlockType;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * Emits a signal when clicked manually or by a Block Clicking Claw
 * @author oskar
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
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		pressed = 2;
	}

}
