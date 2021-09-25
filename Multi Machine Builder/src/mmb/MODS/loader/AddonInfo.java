/**
 * 
 */
package mmb.MODS.loader;

import java.util.*;
import org.apache.commons.compress.archivers.ArchiveEntry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import it.unimi.dsi.fastutil.bytes.ByteList;
import mmb.FILES.AdvancedFile;
import mmb.FILES.OnlineFile;
import mmb.MODS.info.AddonCentral;
import mmb.MODS.info.AddonState;
import mmb.MODS.info.ModMetadata;

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
		//Internal variables
		BiMap<String, ArchiveEntry> entries0 = HashBiMap.create();
		List<ArchiveEntry> contents0 = new ArrayList<>();
		Map<ArchiveEntry, ByteList> files0 = new HashMap<>();
	public boolean hasClasses = false;
	public boolean hasValidData = false;
	
	//About MMB
	public ModMetadata mmbmod = null;
	public AddonCentral central = null;
	
	public boolean isOnline() {
		return file instanceof OnlineFile;
	}
}
