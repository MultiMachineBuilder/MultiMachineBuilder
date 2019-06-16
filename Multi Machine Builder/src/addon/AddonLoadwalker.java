package addon;

import java.io.File;

import files.ListFiles;

public class AddonLoadwalker {
	File directory;
	
	
	/**
	 * @param directory
	 */
	public AddonLoadwalker(File directory) {
		super();
		this.directory = directory;
	}


	{
		//File section
		File[] modfiles = ListFiles.findFiles(directory);
		for(int i = 0; i < modfiles.length; i++) { //Copy over found modfiles
			Addon.registerAddon(new Addon(modfiles[i]));
		}
		File[] modpacks = ListFiles.findDirectories(directory);
		
	}
	public String getName() {
		return directory.getPath();
	}
}
