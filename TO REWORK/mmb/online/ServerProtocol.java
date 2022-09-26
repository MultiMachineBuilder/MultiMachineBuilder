/**
 * 
 */
package mmb.online;

import java.io.InputStream;

/**
 * @author oskar
 *
 */
public interface ServerProtocol {
	public InputStream download(String URL);
	public String name();
}
