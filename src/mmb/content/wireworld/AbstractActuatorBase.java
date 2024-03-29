/**
 * 
 */
package mmb.content.wireworld;

import java.awt.Point;

import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntry;
import mmb.engine.worlds.MapProxy;

/**
 * An abstract implementation of an actuator, which performs an action to a block situated just in front of it.
 * The action is defined by the {@link #run(Point, BlockEntry, MapProxy)} method, which implements an action.
 * This variant has no chirality
 * @author oskar
 * @see AbstractChiralActuatorBase AbstractChiralActuatorBase - when chirality is needed
 */
public abstract class AbstractActuatorBase extends BlockEntityRotary{
	/**
	 * Invoked when actuator runs
	 * @param p position, where actuator performs an action
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
