/**
 *
 */
package mmb.MODS.loader;

import static mmb.Loading.*;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;
import java.util.function.BiConsumer;

import mmb.debug.Debugger;
import mmb.Main;
import mmb.DATA.contents.GameContents;
import mmb.DATA.contents.texture.Textures;
import mmb.ERRORS.UndeclarableThrower;
import mmb.FILES.AdvancedFile;
import mmb.FILES.FileUtil;
import mmb.MENU.FullScreen;
import mmb.MODS.info.AddonInfo;
import mmb.MODS.info.AddonState;
import mmb.RUNTIME.LockCounter;
import mmb.SOUND.MP3Loader;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.item.ContentsItems;

/**
 * @author oskar
 *
 */
public class ModLoader {
	private ModLoader() {}
	public static class FilePair {
		public AdvancedFile f;
		public Throwable error;
		public int errorHTTP;
		public FilePair(Throwable t) {
			f = null;
			error = t;
			errorHTTP = 0;
		}
		public FilePair(Throwable t, int http) {
			f = null;
			error = t;
			errorHTTP = http;
		}
		public FilePair(AdvancedFile g) {
			f = g;
			error = null;
			errorHTTP = 0;
		}
	}

	private static final Debugger debug = new Debugger("MOD-LOADER");

	protected static List<AddonLoader> loaders = new ArrayList<>();
	protected static List<Thread> firstRuns = new ArrayList<>();
	protected static List<Thread> contents = new ArrayList<>();
	protected static List<Thread> integrators = new ArrayList<>();
	protected static List<MP3Loader> mp3s = new ArrayList<>();
	/**
	 * The number of installed mods currently found
	 */
	private static int modCount = 0;
	/**
	 * @return the modCount
	 */
	public static int getModCount() {
		return modCount;
	}
	private static List<String> toLoad = new ArrayList<>();
	
	protected static String[] external = new String[0];// External content which will be loaded

	private static final String SLPM = "Stopping loading prematurely";

	@SuppressWarnings("javadoc")
	public static void waitAllLoaders() {
		LockCounter lock = new LockCounter();
		lock.counter.set(modCount);
		loaders.forEach((AddonLoader t) -> {try {
			t.untilLoad();
			lock.counter.decrementAndGet();
		} catch (InterruptedException e) {
			debug.pstm(e, SLPM);
			lock.counter.decrementAndGet();
			throw new PrematureHaltException(SLPM, e);
		}
		});
	}
	/**
	 * Wait until all MP3 files load
	 */
	public static void waitMP3s() {
		mp3s.forEach(mp3 -> {
			try {
				mp3.untilLoad();
			} catch (InterruptedException e) {
				debug.pstm(e, SLPM);
				throw new PrematureHaltException(SLPM, e);
			}
			
		});
	}
	@SuppressWarnings("null")
	private static void walkTextures(File f) {
		if(f.isFile()) {
			String absPath = f.getAbsolutePath();
			String tname = absPath.substring(Textures.texturesPath.length()+1);
			try(InputStream is = new FileInputStream(f)) {
				Textures.load(tname, is);
			} catch (Exception e) {
				debug.pstm(e, "THIS MESSAGE INDICATES MALFUNCTION OF JAVA OR FILE SYSTEM"); //this should not happen
				debug.pstm(e, "Could not find texture "+tname+", despite its presence being indicated");
				UndeclarableThrower.shoot(e);
			}
		}
		if(f.isDirectory()) {
			File[] walk = f.listFiles();
			for(int i = 0; i < walk.length; i++) {
				walkTextures(walk[i]);
			}
		}
	}
	/**
	 * Used by the main class to load mods
	 */	
	public static final File modsDir = new File("mods/");
	
	//Modify modloading
	private static void initGame() {
		state1("Loading textures");
		walkTextures(new File("textures/"));
		state1("Loading blocks");
		new ContentsBlocks(); //just for initialization
		new ContentsItems();
		FullScreen.initialize();
	}
	private static void firstRuns() {
		LockCounter lock = new LockCounter();
		lock.counter.set(GameContents.addons.size()); 
		//First runs. Similar process for all three stages
		GameContents.addons.forEach(ai -> {
			if(ai.state == AddonState.ENABLE) {
				Thread thr = new Thread(() -> {
					if(ai.central == null) {
						debug.printl(ai.name + " is not a mod and will not be run");
						lock.counter.decrementAndGet();
						ai.state = ai.hasClasses?AddonState.API:AddonState.MEDIA;
					}else{
						try{
							debug.printl("Start 1st stage for " + ai.name);
							ai.central.firstOpen();
							debug.printl("End 1st stage for " + ai.name);
							lock.counter.decrementAndGet();
						}catch(VirtualMachineError e){
							Main.crash(e);
							throw e; //unreachable
						// deepcode ignore DontCatch: guarantee that game fully loads						}catch(Throwable e){
							debug.pstm(e, "Failed to run a mod "+ ai.name);
							ai.state = AddonState.DEAD;
							lock.counter.decrementAndGet();
						}
					}
				});
				firstRuns.add(thr);
				thr.start();
			}
			else lock.counter.decrementAndGet();
		});
		lock.join();
	}
	private static void contentRuns() {
		//Content runs
		LockCounter lock = new LockCounter();
		lock.counter.set(GameContents.addons.size()); 
		GameContents.addons.forEach(ai -> {
			if(ai.state == AddonState.ENABLE) {
				Thread thr = new Thread(() -> {
					try{
						debug.printl("Start 2nd stage for " + ai.name);
						ai.central.makeContent();
						debug.printl("End 2nd stage for " + ai.name);
						lock.counter.decrementAndGet();
					}catch(VirtualMachineError e){
						Main.crash(e);
					// deepcode ignore DontCatch: guarantee that game fully loads, VM errors used to crash the game earlier					}catch(Throwable e){
						debug.pstm(e, "Failed to run a mod "+ ai.name);
						
						ai.state = AddonState.DEAD;
						lock.counter.decrementAndGet();
					}
				});
				contents.add(thr);
				thr.start();
			} else lock.counter.decrementAndGet();
		});
		lock.join();
		/*contents.forEach(t -> {try {
			t.join();
		} catch (InterruptedException e) {
			debug.pstm(e, SLPM);
			throw new PrematureHaltException(SLPM, e);
		}
		});*/
	}
	private static void integrationRuns() {
		//Integration runs
		LockCounter lock = new LockCounter();
		lock.counter.set(GameContents.addons.size()); 
		GameContents.addons.forEach(ai -> {
			if(ai.state == AddonState.ENABLE) {
				Thread thr = new Thread(() -> {
					
					try{
						debug.printl("Start 3rd stage for " + ai.name);
						ai.central.makeContent();
						debug.printl("End 3rd stage for " + ai.name);
						lock.counter.decrementAndGet();
					}catch(VirtualMachineError e){
						Main.crash(e);
					}catch(Throwable e){
						debug.pstm(e, "Failed to run a mod "+ ai.name);
						lock.counter.decrementAndGet();
						ai.state = AddonState.DEAD;
					}
				});
				integrators.add(thr);
				thr.start();
			}else lock.counter.decrementAndGet();
		});
		lock.join();
		/*integrators.forEach(t -> {try {
			t.join();
		} catch (InterruptedException e) {
			debug.pstm(e, SLPM);
			throw new PrematureHaltException(SLPM, e);
		}
		});*/
	}
	/**
	 * Loads entire game
	 */
	@SuppressWarnings("null")
	public static void modloading(){
		initGame();
		try {
			external = new String(Files.readAllBytes(Paths.get("ext.txt"))).split("\n");
		} catch (IOException e1) {
			debug.pstm(e1, "Unable to load external mods");
		}
		//Notify user
		state1("Initial load");
		debug.printl("Loading mods");
		debug.printl("Finding all files to load");
		//Add missing 'mods' directory
		if (!modsDir.mkdirs() || !modsDir.isDirectory()) debug.printl("Added missing mods directory");
		//Walk 'mods' directory
		walkDirectory(modsDir, (s, ff) -> {
			try {
				toLoad.add(ff.getAbsolutePath());
				modCount++;
			} catch (Exception e) {
				debug.pstm(e, "Couldn't load file "+ s);
			}
		});
		modCount += external.length;
		toLoad.addAll(Arrays.asList(external));
		//Add mod files for loading
		state1("Found "+ modCount + " mod files");
		debug.printl("Found "+ modCount + " mod files");
		for(String p: toLoad) {
			state2("Loading file: " + p);
			try {
				AddonLoader.load(FileUtil.getFile(p));
			} catch (MalformedURLException e) {
				debug.pstm(e, "The external mod has incorrect URL: "+p);
			}
		}
		waitAllLoaders();//Wait until all files load
		firstRuns(); // first runs
		contentRuns(); // content runs
		integrationRuns(); //integration runs
		
		//clean up
		loaders.clear();
		firstRuns.clear();
		contents.clear();
		integrators.clear();
		mp3s.clear();
		summarizeMods();
		debug.printl("HOORAY, IT'S OVER!");
	}
	
	/**
	 *
	 * @param folder
	 * @param action
	 */
	public static void walkDirectory(File folder, BiConsumer<String,File> action) {
		List<File> files = new ArrayList<>();
		walkDirectory(folder, files);
		files.forEach(file -> {
			String root = folder.getAbsolutePath();
			String sub = file.getAbsolutePath();
			sub = sub.substring(root.length());
			action.accept(sub, file);
		});
	}
	/**
	 * Walk the directory by adding all files to the list
	 * @param folder root
	 * @param results output
	 */
	@SuppressWarnings("null")
	public static void walkDirectory(File folder, List<File> results) {
		try {
			if(folder.isDirectory()) {
				debug.printl("Directory: " + folder.getCanonicalPath());
				for(File modfile: FileUtil.findFiles(folder)) {
					debug.printl("File: " + modfile.getCanonicalPath());
					results.add(modfile);
				}
				for(File modfile: FileUtil.findDirectories(folder)) {
					walkDirectory(modfile, results);
				}
			}else {
				debug.printl("File: " + folder.getCanonicalPath());
				results.add(folder);
			}
		} catch (IOException e) {
			debug.pstm(e, "THIS MESSAGE INDICATES MALFUNCTION OF FILE PATH SYSTEM OR JAVA. Couldn't get path of the file");
		}
	}
	/**
	 * Walk the directory by adding all files and directories to the list
	 * @param folder root
	 * @param results output
	 */
	@SuppressWarnings("null")
	public static void walkFilesDirectory(File folder, List<File> results) {
		try {
			if(folder.isDirectory()) {
				debug.printl("Directory: " + folder.getCanonicalPath());
				File[] modpacks = FileUtil.findDirectories(folder);
				for(int i = 0; i < modpacks.length; i++) {
					walkFilesDirectory(modpacks[i], results);
				}
			}else {
				debug.printl("File: " + folder.getCanonicalPath());
			}
			results.add(folder);
		}catch(Exception e) {
			debug.pstm(e, "THIS MESSAGE INDICATES MALFUNCTION OF FILE PATH SYSTEM OR JAVA. Couldn't get path of the file");
		}
	}

	static void summarizeMods() {
		for(AddonInfo ai: GameContents.addons) {
			debug.printl("================MOD INFORMATION FOR " + ai.name + "================");
			debug.printl("LOCATED AT " + ai.path);

			debug.printl(ai.state.toString());
			try {
				switch(ai.state) {
				case DEAD:
				case DISABLE:
				case ENABLE:
					if(ai.mmbmod == null) {
						debug.printl("Unknown information");
					}else {
						if(ai.mmbmod.release == null) {
							debug.printl("RELEASED AT UNKNOWN DATE");
						}else {
							debug.printl("RELEASED " + ai.mmbmod.release.toString());
						}

						debug.printl("MADE BY " + ai.mmbmod.author);
						debug.printl("DESCRIPTION: " + ai.mmbmod.description);
					}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				debug.pstm(e, "Unable to get metadata for " + ai.name);
			}
		}
	}
}
