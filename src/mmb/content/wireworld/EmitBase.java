/**
 * 
 */
package mmb.content.wireworld;

import mmb.engine.block.BlockEntityDataless;
import mmb.engine.rotate.Side;

/**
 * A base implementation for an uniform emitter block.
 * Override the {@link #onTick(mmb.engine.worlds.MapProxy)} to set the state
 * @author oskar
 */
public abstract class EmitBase extends BlockEntityDataless {
	protected boolean state;
	@Override
	public boolean provideSignal(Side s) {
		return state;
	}
}
