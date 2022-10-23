/**
 * 
 */
package mmb.beans;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author oskar
 * Represents an object which can be saved
 */
public interface Saver {
	/**
	 * @return the saved data
	 */
	public @Nullable JsonNode save();
	/**
	 * Loads the following data object.
	 * @param data
	 */
	public void load(@Nullable JsonNode data);
}
