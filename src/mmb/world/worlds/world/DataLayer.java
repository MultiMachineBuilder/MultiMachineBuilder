/**
 * 
 */
package mmb.world.worlds.world;

import mmb.beans.Saver;
import monniasza.collects.Identifiable;

/**
 * A container to help implement data layers
 * @author oskar
 * @param <T> type of enclosing game object
 */
public abstract class DataLayer<T> implements Identifiable<T>, Saver {
	/**
	 * Creates a world data alyer
	 * @param world
	 */
	protected DataLayer(T world) {
		this.world = world;
	}
	private final T world;
	@Override
	public final T id() {
		return world;
	}
	/** Invoked on start-up */
	public void startup() {};
	/** Invoked on shutdown */
	public void shutdown() {};
	/** Invoked on every tick */
	public void cycle() {};
}
