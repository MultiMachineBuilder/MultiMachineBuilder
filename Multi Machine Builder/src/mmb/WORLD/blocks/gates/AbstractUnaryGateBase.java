/**
 * 
 */
package mmb.WORLD.blocks.gates;

import mmb.BEANS.Rotable;
import mmb.WORLD.Rotation;
import mmb.WORLD.Side;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.BlockMap;

/**
 * @author oskar
 * A skeletal implementation for a gate which reads two signals from DL and DR corners, and outputs them to U side
 */
public abstract class AbstractUnaryGateBase extends SkeletalBlockEntityRotary{
	protected boolean result;
	/**
	 * @param x
	 * @param y
	 * @param owner2
	 */
	protected AbstractUnaryGateBase(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
		// TODO Auto-generated constructor stub
	}
	protected abstract boolean run(boolean a);
	@Override
	public boolean provideSignal(Side s) {
		return (s == side.U()) && result;
	}
	@Override
	public void onTick(MapProxy map) {
		boolean a = owner.getAtSide(side.D(), x, y).provideSignal(side.U());
		result = run(a);
	}
}
