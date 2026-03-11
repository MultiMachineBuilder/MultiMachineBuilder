/**
 * 
 */
package mmb.beans;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.annotations.Nil;

/**
 * Represents an object which can be saved
 * @author oskar
 */
public interface Saver {
	/**
	 * @return the saved data
	 */
	public @Nil JsonNode save();
}
