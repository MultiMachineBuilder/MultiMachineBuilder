/**
 * 
 */
package mmb.WORLD.blocks.gates;

import mmb.WORLD.Side;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.worlds.MapProxy;

/**
 * @author oskar
 * A skeletal implementation for a gate which reads two signals from DL and DR corners, and outputs them to U side
 */
public abstract class AbstractSourceGateBase extends SkeletalBlockEntityRotary{
	
	protected boolean result;

	protected abstract boolean run();
	
	@Override
	public boolean provideSignal(Side s) {
		return (s == side.U()) && result;
	}
	@Override
	public void onTick(MapProxy map) {
		result = run();
	}

}
