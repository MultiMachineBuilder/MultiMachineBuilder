package addon;

import java.util.List;
import java.io.Console;
import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import debug.Avaliability;
import debug.Debugger;

public class Addon {
	String name;
	Path where; 
	JarFile loaded;
	Avaliability isAvaliable;
	JarFile modJF;
	ModSpecs specs;
	JarEntry[] contents;
	JarEntryLoader[] loaders;
	File modFile;
	
	static ArrayList<Addon> addons = new ArrayList<Addon>();
	static ArrayList<AddonLoadwalker> wallkers = new ArrayList<AddonLoadwalker>();
	static AddonLoadwalker lw;
	
	public Addon() {
		// TODO Auto-generated constructor stub
	}
	
	public Addon(Path placement) {
		where = placement;
		modFile = where.toFile();
	}
	
	public Addon(File file){
		modFile = file;
		where = Paths.get(file.getAbsolutePath());
	}
	
	{ //Loading after construction
		//Trying to open file
		try {
			modFile = where.toFile();
			try {
				modJF = new JarFile(modFile);
			}catch(Exception e) {
				Debugger.printl("Couldn't open zip or jar file" + where.toString() + ".");
				Debugger.pst(e);
				isAvaliable.corrupt(e);
			}
			
		}catch(Exception e) {
			Debugger.printl("File" + where.toString() + "is unavaliable.");
			Debugger.pst(e);
			isAvaliable.noexist(e);
		}
		
		if(isAvaliable.isAvaliable.isAvaliable()) {
			//Mod is available
			Enumeration<JarEntry> cnts = getContents();
			ArrayList<JarEntry> toContents = new ArrayList<JarEntry>();
			while(cnts.hasMoreElements()) {
				JarEntry next = cnts.nextElement();
				toContents.add(next);
			}
			contents = (JarEntry[]) toContents.toArray();
			
		}else {
			//Mod is unavailable
			Debugger.printl("Oh noes, " + where.toString() + "is inoperative, no fun with it.");
		}
	}
	
	
	public static void initMods() {
		//List mods
		Debugger.printl("Opening mods directory");
		File modsDirectory = new File("mods");
		
		if(!modsDirectory.exists()) {
			Debugger.printl("mods directory was missing, created it");
			modsDirectory.mkdirs();
		}
		
		Debugger.printl("Activating loadwalker");
		lw = new AddonLoadwalker(modsDirectory);
		Debugger.printl("Activated loadwalker succesfully");
	}
	
	//Getters
	
	public boolean isAvaliable() {
		return isAvaliable.isAvaliable.isAvaliable();
	}
	public Enumeration<JarEntry> getContents() {
		return modJF.entries();
	}
	public ZipEntry getZipEntry(String name) {
		return modJF.getEntry(name);
	}
	public JarEntry getJarEntry(String name) {
		return modJF.getJarEntry(name);
	}
	public ZipEntry getZipFile(String name) {
		return modJF.getEntry(name);
	}
	
	public void whenLoaderFinishes(JarEntryLoader loader) {
		Debugger.printl(loader.resource.getName() + "loaded, have fun.");
	}
	
	
	
	public void finalize() {
		Debugger.printl("Shutting down " + name);
	}
	static public void shutdown() {
		Debugger.printl("Shutting Addon module");
	}
	static public void registerAddon(Addon na) {
		addons.add(na);
	}
    static public void whenWalkerFinishes() {
		
	}
}
