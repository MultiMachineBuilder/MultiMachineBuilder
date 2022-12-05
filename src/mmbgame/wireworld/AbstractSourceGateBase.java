/**
 * 
 */
package mmbgame.wireworld;

import mmbeng.block.BlockEntityRotary;
import mmbeng.rotate.Side;
import mmbeng.worlds.MapProxy;

/**
 * @author oskar
 * A skeletal implementation for a gate which reads two signals from DL and DR corners, and outputs them to U side
 */
public abstract class AbstractSourceGateBase extends BlockEntityRotary{
	
	protected boolean result;

	protected abstract boolean run();
	
	@Override
	public boolean provideSignal(Side s) {
		return (s == getRotation().U()) && result;
	}
	@Override
	public void onTick(MapProxy map) {
		result = run();
	}

}
