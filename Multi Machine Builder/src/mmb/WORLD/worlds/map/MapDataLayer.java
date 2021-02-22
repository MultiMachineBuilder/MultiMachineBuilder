/**
 * 
 */
package mmb.WORLD.worlds.map;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.BEANS.Identifiable;
import mmb.BEANS.Loader;
import mmb.BEANS.Saver;
import mmb.BEANS.Titled;

/**
 * @author oskar
 *
 */
public interface MapDataLayer extends Loader<JsonNode>, Saver<JsonNode>, Identifiable<String>, Titled {
	/**
	 * Run after given {@link BlockMap} loads
	 * @param map block map
	 */
	public void afterMapLoaded(BlockMap map);
}
