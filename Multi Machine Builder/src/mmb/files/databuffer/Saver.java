/**
 * 
 */
package mmb.files.databuffer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author oskar
 *
 */
public interface Saver<T> {
	public void save(DataOutputStream dos, T data) throws IOException;
	public T read(DataInputStream dis) throws IOException;
	
	
	
}
