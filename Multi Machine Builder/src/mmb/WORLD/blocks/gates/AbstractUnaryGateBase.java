/**
 * 
 */
package mmb.WORLD.blocks.gates;

import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;

/**
 * @author oskar
 * A skeletal implementation for a gate which reads two signals from DL and DR corners, and outputs them to U side
 */
public abstract class AbstractUnaryGateBase extends SkeletalBlockEntityRotary{
	protected boolean result;
	protected abstract boolean run(boolean a);
	@Override
	public boolean provideSignal(Side s) {
		return (s == getRotation().U()) && result;
	}
	@Override
	public void onTick(MapProxy map) {
		boolean a = owner().getAtSide(getRotation().D(), posX(), posY()).provideSignal(getRotation().U());
		result = run(a);
	}
}
