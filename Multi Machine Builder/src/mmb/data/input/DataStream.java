/**
 * 
 */
package mmb.data.input;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author oskar
 *
 */
public class DataStream{
	public String name;
	public InputStream stream;
	
	public DataStream(String location) throws MalformedURLException, IOException {
		name = location;
		stream = new URL(location).openStream();
	}
}
