/**
 * 
 */
package mmb.DATA.file;

import java.net.MalformedURLException;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class FileGetter {
	public static String home;
	private static final Debugger debug = new Debugger("FILES");

	
	public static AdvancedFile getFile(String path) throws MalformedURLException {
		if(path.startsWith("http://") || path.startsWith("https://")) {
			return new OnlineFile(path);
		}
		return new LocalFile(path);
	}	
}
