package gamefiles;

import java.nio.file.DirectoryStream;
import java.util.zip.ZipEntry;

import debug.Debugger;

public class GameResource {
	public static GameResource[] resources;
	
	public String name;
	/**
	 * Loads resource from zip file
	 * @param ze
	 */
	public GameResource(ZipEntry ze) {
		if(ze.isDirectory()) {
			Debugger.printl("Directory: " + ze.getName());
		}else{
			Debugger.print("File: " + ze.getName());
			ze.
		}
	}
	/**
	 * Unloads resource
	 */
	public void finalize() {
		Debugger.printl("Unloading " + name);
		
	}
	static public void shutdown() {
		Debugger.print("Unloading all resources";
		for(int i = 0; i < resources.length; i++) {
			resources[i].finalize();
		}
	}
}
