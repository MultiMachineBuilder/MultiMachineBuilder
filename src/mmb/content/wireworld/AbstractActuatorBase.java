/**
 * 
 */
package mmb.content.wireworld;

import java.awt.Point;

import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntry;
import mmb.engine.worlds.MapProxy;

/**
 * @author oskar
 * A skeletal implementation for a gate which reads two signals from DL and DR corners, and outputs them to U side
 */
public abstract class AbstractActuatorBase extends BlockEntityRotary{
	/**
	 * Invoked when actuator runs
	 * @param p position of change
	 * @param ent block to modify
	 * @param proxy map proxy to use
	 */
	protected abstract void run(Point p, BlockEntry ent, MapProxy proxy);
	@Override
	public void onTick(MapProxy map) {
		boolean a = owner().getAtSide(getRotation().D(), posX(), posY()).provideSignal(getRotation().U());
		Point pt = getRotation().U().offset(posX(), posY());
		if(a) run(pt, owner().get(pt), map);
	}
}
