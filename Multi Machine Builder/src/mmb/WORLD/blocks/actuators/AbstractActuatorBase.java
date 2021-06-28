/**
 * 
 */
package mmb.WORLD.blocks.actuators;

import java.awt.Point;

import mmb.WORLD.Rotation;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.BlockMap;

/**
 * @author oskar
 * A skeletal implementation for a gate which reads two signals from DL and DR corners, and outputs them to U side
 */
public abstract class AbstractActuatorBase extends SkeletalBlockEntityRotary{
	protected boolean result;
	/**
	 * @param x
	 * @param y
	 * @param owner2
	 */
	protected AbstractActuatorBase(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}
	protected abstract void run(Point p, BlockEntry ent, MapProxy proxy);
	@Override
	public void onTick(MapProxy map) {
		boolean a = owner.getAtSide(side.D(), x, y).provideSignal(side.U());
		Point pt = side.U().offset(x, y);
		if(a) run(pt, owner.get(pt.x, pt.y), map);
	}
	@Override
	public void setRotation(Rotation rotation) {
		side = rotation;
	}
	@Override
	public Rotation getRotation() {
		return side;
	}
}
