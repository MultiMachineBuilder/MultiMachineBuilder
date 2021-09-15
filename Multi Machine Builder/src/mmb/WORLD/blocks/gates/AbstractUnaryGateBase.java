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
public abstract class AbstractUnaryGateBase extends SkeletalBlockEntityRotary{
	protected boolean result;
	protected abstract boolean run(boolean a);
	@Override
	public boolean provideSignal(Side s) {
		return (s == side.U()) && result;
	}
	@Override
	public void onTick(MapProxy map) {
		boolean a = owner().getAtSide(side.D(), posX(), posY()).provideSignal(side.U());
		result = run(a);
	}
}
