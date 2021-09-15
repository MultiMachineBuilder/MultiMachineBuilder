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
public abstract class AbstractBiGateBase extends SkeletalBlockEntityRotary{
	protected boolean result;

	protected abstract boolean run(boolean a, boolean b);
	
	@Override
	public boolean provideSignal(Side s) {
		return (s == side.U()) && result;
	}
	@Override
	public void onTick(MapProxy map) {
		boolean a = owner().getAtSide(side.DL(), posX(), posY()).provideSignal(side.DL());
		boolean b = owner().getAtSide(side.DR(), posX(), posY()).provideSignal(side.DR());
		result = run(a, b);
	}
}
