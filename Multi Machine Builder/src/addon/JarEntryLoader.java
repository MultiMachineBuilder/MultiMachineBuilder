package addon;

import java.nio.file.Path;
import java.util.jar.JarEntry;

import debug.Debugger;
import gamefiles.GameFile;

public class JarEntryLoader {
 JarEntry resource;
 Addon from;
 Path srcPath;
 JarEntryLoader extraLoaders;
 GameFile[] files;
 
 
/**
 * @param resource
 * @param from
 * @param srcPath
 */
public JarEntryLoader(JarEntry resource, Addon from, Path srcPath) {
	super();
	this.resource = resource;
	this.from = from;
	this.srcPath = srcPath;
}



/**
 * @param resource
 * @param from
 */

{
	String name = resource.getName();
	if(resource.isDirectory()) {
		//Handle directory
		Debugger.printl("Directory: " + srcPath.toString() + "/" + name);
		
	}else {
		//Handle file
		
		Debugger.printl("File: " + srcPath.toString() + "/" + name);
		resource.
	}
}
}
