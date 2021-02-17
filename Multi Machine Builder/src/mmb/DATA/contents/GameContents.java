/**
 * 
 */
package mmb.DATA.contents;

import java.awt.image.BufferedImage;
import java.util.*;

import mmb.FILES.LocalFile;
import mmb.MODS.info.AddonInfo;
/**
 * @author oskar
 *
 */

public class GameContents {
	//public static Hashtable<String, PartSpec> parts = new Hashtable<String, PartSpec>();
	public static List<AddonInfo> addons = new ArrayList<AddonInfo>();
	@SuppressWarnings("rawtypes")
	public static List<Class> loadedClasses = new ArrayList<Class>();
	
	
	public static final LocalFile dirSaves = new LocalFile("maps/"),
			dirMods = new LocalFile("mods/");
	
	public void unmoddedContents() {
		//Unmodded content
		
		
	}
	
	
	
	void RnD(){
		
	}

}
