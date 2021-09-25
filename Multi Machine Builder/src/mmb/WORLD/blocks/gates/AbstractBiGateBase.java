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
public abstract class AbstractBiGateBase extends SkeletalBlockEntityRotary{
	protected boolean result;

	protected abstract boolean run(boolean a, boolean b);
	
	@Override
	public boolean provideSignal(Side s) {
		return (s == getRotation().U()) && result;
	}
	@Override
	public void onTick(MapProxy map) {
		boolean a = owner().getAtSide(getRotation().DL(), posX(), posY()).provideSignal(getRotation().DL());
		boolean b = owner().getAtSide(getRotation().DR(), posX(), posY()).provideSignal(getRotation().DR());
		result = run(a, b);
	}
}
