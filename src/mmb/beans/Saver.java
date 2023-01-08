/**
 * 
 */
package mmb.beans;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.Nil;

/**
 * Represents an object which can be saved
 * @author oskar
 */
public interface Saver {
	/**
	 * @return the saved data
	 */
	public @Nil JsonNode save();
	/**
	 * Loads the following data object.
	 * @param data
	 */
	public void load(@Nil JsonNode data);
}
