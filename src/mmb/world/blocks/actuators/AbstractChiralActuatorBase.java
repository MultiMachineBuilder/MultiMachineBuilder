/**
 * 
 */
package mmb.world.blocks.actuators;

import java.awt.Point;

import mmb.world.block.BlockEntityChirotable;
import mmb.world.block.BlockEntry;
import mmb.world.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public abstract class AbstractChiralActuatorBase extends BlockEntityChirotable {
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
