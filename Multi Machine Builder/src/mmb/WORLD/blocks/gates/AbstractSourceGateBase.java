/**
 * 
 */
package mmb.WORLD.blocks.gates;

import java.awt.Graphics;

import mmb.WORLD.Side;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World.BlockMap;

/**
 * @author oskar
 * A skeletal implementation for a gate which reads two signals from DL and DR corners, and outputs them to U side
 */
public abstract class AbstractSourceGateBase extends SkeletalBlockEntityRotary{
	@Override
	public void render(int x, int y, Graphics g) {
		getImage().get(side).draw(x, y, g);
	}
	protected boolean result;
	/**
	 * @param x
	 * @param y
	 * @param owner2
	 */
	protected AbstractSourceGateBase(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
		// TODO Auto-generated constructor stub
	}
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
