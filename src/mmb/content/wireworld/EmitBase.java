/**
 * 
 */
package mmb.content.wireworld;

import mmb.engine.block.BlockEntityDataless;
import mmb.engine.rotate.Side;

/**
 * @author oskar
 * Override the onTick method to run
 */
public abstract class EmitBase extends BlockEntityDataless {
	protected boolean state;
	@Override
	public boolean provideSignal(Side s) {
		return state;
	}
}
