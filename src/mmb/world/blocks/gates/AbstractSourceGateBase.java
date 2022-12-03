/**
 * 
 */
package mmb.world.blocks.gates;

import mmb.world.blocks.SkeletalBlockEntityRotary;
import mmb.world.rotate.Side;
import mmb.world.worlds.MapProxy;

/**
 * @author oskar
 * A skeletal implementation for a gate which reads two signals from DL and DR corners, and outputs them to U side
 */
public abstract class AbstractSourceGateBase extends SkeletalBlockEntityRotary{
	
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
