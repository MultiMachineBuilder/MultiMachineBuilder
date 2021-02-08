/**
 * 
 */
package mmb.MODS.info;

import java.util.*;
import java.util.jar.*;


import mmb.DATA.file.AdvancedFile;
import mmb.DATA.file.OnlineFile;
import mmb.MODS.loader.StreamClassLoader;

/**
 * @author oskar
 *
 */
public class AddonInfo {
	public AddonState state = AddonState.ENABLE;
	
	//In any case
	public String name = "";
	public String path = "";
		
	//If file exists
	public AdvancedFile file = null;
	
	//If file is not corrupt
	public StreamClassLoader classes;
	public List<JarEntry> contents = new ArrayList<>();
	public Map<JarEntry, byte[]> files = new HashMap<>();
	public boolean hasClasses = false;
	public boolean hasValidData = false;
	
	//About MMB
	public ModMetadata mmbmod = null;
	public AddonCentral central = null;
	
	public boolean isOnline() {
		return file instanceof OnlineFile;
	}
}
