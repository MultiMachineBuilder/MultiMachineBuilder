/**
 * 
 */
package mmb.DATA.contents;

import java.util.*;

import mmb.FILES.FileUtil;
import mmb.FILES.LocalFile;
import mmb.MODS.loader.AddonInfo;
/**
 * @author oskar
 * @depreacted Use {@link FileUtil} instead.
 * The arrays here just mirror those in respective classes
 */

public class GameContents {
	//public static Hashtable<String, PartSpec> parts = new Hashtable<String, PartSpec>();
	public static List<AddonInfo> addons = new ArrayList<>();
	public static List<Class<?>> loadedClasses = new ArrayList<>();
	
	
	public static final LocalFile dirSaves = new LocalFile("maps/"),
			dirMods = new LocalFile("mods/");
}
