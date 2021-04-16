/**
 * 
 */
package mmb.SETTINGS;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author oskar
 * An interface that combines multiple types of settings
 * @param <T> type of top setting node
 */
public interface Setting<T> {
	/**
	 * @param in the input stream, from which data is loaded
	 * @throws IOException when input operation fails
	 */
	public void load(InputStream in) throws IOException;
	/**
	 * @param out the output stream, to which data is saved
	 * @throws IOException when output operation fails
	 */
	public void save(OutputStream out) throws IOException;
	/**
	 * @return a top level setting node
	 */
	public T getData();
}
