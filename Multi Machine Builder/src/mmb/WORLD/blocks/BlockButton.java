/**
 * 
 */
package mmb.WORLD.blocks;

import mmb.BEANS.BlockActivateListener;
import mmb.WORLD.Side;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityDataless;
import mmb.WORLD.gui.WorldWindow;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;
import mmb.WORLD.worlds.world.World.BlockMap;

/**
 * @author oskar
 *
 */
public class BlockButton extends SkeletalBlockEntityDataless implements BlockActivateListener {
	private int pressed;
	public BlockButton(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}

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
	public void click(int blockX, int blockY, World map, WorldWindow window) {
		pressed = 2;
	}

}
