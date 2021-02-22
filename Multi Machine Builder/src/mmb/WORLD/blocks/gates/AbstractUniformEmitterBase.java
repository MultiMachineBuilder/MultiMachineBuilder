/**
 * 
 */
package mmb.WORLD.blocks.gates;

import mmb.WORLD.Side;
import mmb.WORLD.block.SkeletalBlockEntityDataless;
import mmb.WORLD.worlds.map.BlockMap;

/**
 * @author oskar
 * Override the onTick method to run
 */
public abstract class AbstractUniformEmitterBase extends SkeletalBlockEntityDataless {
	protected AbstractUniformEmitterBase(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}
	protected boolean state;
	@Override
	public boolean provideSignal(Side s) {
		return state;
	}
	
}
