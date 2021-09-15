/**
 * 
 */
package mmb.WORLD.blocks.actuators;

import java.awt.Point;

import mmb.WORLD.Rotation;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.worlds.MapProxy;

/**
 * @author oskar
 * A skeletal implementation for a gate which reads two signals from DL and DR corners, and outputs them to U side
 */
public abstract class AbstractActuatorBase extends SkeletalBlockEntityRotary{
	/**
	 * 
	 * @param p position of change
	 * @param ent block to modify
	 * @param proxy map proxy to use
	 */
	protected abstract void run(Point p, BlockEntry ent, MapProxy proxy);
	@Override
	public void onTick(MapProxy map) {
		boolean a = owner().getAtSide(side.D(), posX(), posY()).provideSignal(side.U());
		Point pt = side.U().offset(posX(), posY());
		if(a) run(pt, owner().get(pt), map);
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
