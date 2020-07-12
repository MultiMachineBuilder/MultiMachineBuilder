/**
 * 
 */
package mmb.addon.data;

import java.io.*;
import java.util.*;
import java.util.jar.*;

import mmb.addon.module.AddonCentral;
import mmb.data.input.DataStream;

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
	public File file = null;
	public boolean online = false;
	
	//If file is not corrupt
	public JarFile jfile = null;
	public List<JarEntry> contents = new ArrayList<JarEntry>();
	
	//About MMB
	public ModMetadata mmbmod = null;
	public AddonCentral central = null;

}
