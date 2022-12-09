/**
 * 
 */
package mmbbase.beans;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Represents an object which can be saved
 * @author oskar
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
