/**
 * 
 */
package mmb.BEANS;

import mmb.WORLD.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public interface RunOnTick {
	public default void onTick(MapProxy map) {}
}
