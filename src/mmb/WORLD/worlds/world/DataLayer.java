/**
 * 
 */
package mmb.WORLD.worlds.world;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.BEANS.Saver;
import monniasza.collects.Identifiable;

/**
 * A container to help implement data layers
 * @author oskar
 * @param <T> type of enclosing game object
 */
public abstract class DataLayer<T> implements Identifiable<T>, Saver<@Nonnull JsonNode> {
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
}
