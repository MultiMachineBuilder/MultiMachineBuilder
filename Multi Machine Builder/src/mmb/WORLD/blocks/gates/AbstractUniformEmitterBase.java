/**
 * 
 */
package mmb.WORLD.blocks.gates;

import mmb.WORLD.Side;
import mmb.WORLD.block.BlockEntityDataless;

/**
 * @author oskar
 * Override the onTick method to run
 */
public abstract class AbstractUniformEmitterBase extends BlockEntityDataless {
	protected boolean state;
	@Override
	public boolean provideSignal(Side s) {
		return state;
	}
}
