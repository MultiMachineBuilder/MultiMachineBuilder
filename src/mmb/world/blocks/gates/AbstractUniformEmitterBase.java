/**
 * 
 */
package mmb.world.blocks.gates;

import mmb.world.blocks.BlockEntityDataless;
import mmb.world.rotate.Side;

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
