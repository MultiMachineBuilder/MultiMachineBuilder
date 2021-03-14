/**
 * 
 */
package mmb.FILES;

import java.io.File;
import java.net.MalformedURLException;

import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class FileUtil {
	public static String home;
	private static final Debugger debug = new Debugger("FILES");

	
	public static AdvancedFile getFile(String path) throws MalformedURLException {
		//TODO requires fixed path
		if(path.contains("://")) return new OnlineFile(path);
		return new LocalFile(path);
	}


	/** Find all directories located in the given directory
	 * @param root target container directory
	 * @return array of directories
	 */
	public static File[] findDirectories(File root) { 
	    return root.listFiles(File::isDirectory);
	}


	/** Find all files located in the given directory
	 * @param root target container directory
	 * @return array of files
	 */
	public static File[] findFiles(File root) {
		return root.listFiles(File::isFile);
	}	
}
