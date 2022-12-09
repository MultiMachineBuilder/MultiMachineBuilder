/**
 * 
 */
package mmb.content.wireworld;

import mmb.engine.block.BlockEntityRotary;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;

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
