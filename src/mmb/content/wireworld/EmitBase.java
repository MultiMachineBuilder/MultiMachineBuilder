/**
 * 
 */
package mmbgame.wireworld;

import mmbeng.block.BlockEntityDataless;
import mmbeng.rotate.Side;

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
