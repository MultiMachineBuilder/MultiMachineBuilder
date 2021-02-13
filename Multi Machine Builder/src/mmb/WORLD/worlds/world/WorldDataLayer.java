/**
 * 
 */
package mmb.WORLD.worlds.world;

import com.fasterxml.jackson.databind.JsonNode;
import mmb.Identifiable;
import mmb.BEANS.Loader;
import mmb.BEANS.Saver;

/**
 * @author oskar
 *
 */
public interface WorldDataLayer extends Loader<JsonNode>, Saver<JsonNode>, Identifiable<String> {
	/**
	 * This method is run after the data layer is loaded
	 */
	public void afterWorldLoaded(World d);
}
