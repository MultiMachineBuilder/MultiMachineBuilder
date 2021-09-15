/**
 * 
 */
package mmb.WORLD.worlds.world;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.BEANS.Saver;
import mmb.BEANS.Titled;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 *
 */
public interface WorldDataLayer extends Saver<JsonNode>, Identifiable<String>, Titled {
	/**
	 * Run after given {@link World} loads
	 * @param map block map
	 */
	public void afterMapLoaded(World map);
}
