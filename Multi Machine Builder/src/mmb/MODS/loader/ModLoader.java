/**
 *
 */
package mmb.MODS.loader;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;
import java.util.function.BiConsumer;

import mmb.debug.Debugger;
import mmb.files.data.files.ListFiles;
import mmb.ui.window.Loading;
import mmb.DATA.contents.GameContents;
import mmb.DATA.contents.texture.Textures;
import mmb.DATA.file.AdvancedFile;
import mmb.DATA.file.FileGetter;
import mmb.MODS.info.AddonInfo;
import mmb.MODS.info.AddonState;
import mmb.WORLD.tileworld.block.Blocks;
import mmb.WORLD.tileworld.tool.Tools;

import static mmb.ui.window.Loading.*;

/**
 * @author oskar
 *
 */
public class ModLoader {
	@SuppressWarnings("javadoc")
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
	@SuppressWarnings("javadoc")
	public static List<AddonLoader> runningLoaders = new ArrayList<AddonLoader>();
	@SuppressWarnings("javadoc")
	public static List<Thread> runningFirstRunThreads = new ArrayList<Thread>();
	@SuppressWarnings("javadoc")
	public static List<Thread> runningContentAddThreads = new ArrayList<Thread>();
	@SuppressWarnings("javadoc")
	public static List<Thread> runningIntegrationThreads = new ArrayList<Thread>();
	/**
	 * The number of installed mods currently found
	 */
	public static int modCount = 0;
	private static List<String> toLoad = new ArrayList<String>();
	/**
	 * External content which will be loaded
	 */
	public static String[] external = new String[] {};

	@SuppressWarnings("javadoc")
	public static void waitAllFirstRuns() {
		runningFirstRunThreads.forEach((Thread t) -> {try {
			t.join();
		} catch (InterruptedException e) {
			debug.pstm(e, "Stopping loading prematurely");
		}
		});
	}

	@SuppressWarnings("javadoc")
	public static void waitAllContentRuns() {
		runningContentAddThreads.forEach((Thread t) -> {try {
			t.join();
		} catch (InterruptedException e) {
			debug.pstm(e, "Stopping loading prematurely");
		}
		});
	}

	@SuppressWarnings("javadoc")
	public static void waitAllIntegrationRuns() {
		runningIntegrationThreads.forEach((Thread t) -> {try {
			t.join();
		} catch (InterruptedException e) {
			debug.pstm(e, "Stopping loading prematurely");
		}
		});
	}
	@SuppressWarnings("javadoc")
	public static void waitAllLoaders() {
		runningLoaders.forEach((AddonLoader t) -> {try {
			t.runningOn.join();
		} catch (InterruptedException e) {
			debug.pstm(e, "Stopping loading prematurely");
		}
		});
	}

	private static void walkTextures(File f) {
		if(f.isFile()) {
			String absPath = f.getAbsolutePath();
			String tname = absPath.substring(Textures.texturesPath.length()+1);
			//debug.printl("Loading texture: " +tname);
			try {
				Textures.load(tname, new FileInputStream(f));
			} catch (FileNotFoundException e) {
				debug.pstm(e, "THIS MESSAGE INDICATES MALFUNCTION OF JAVA OR FILE SYSTEM"); //this should not happen
				debug.pstm(e, "Could not find texture "+tname+", despite its presence being indicated");
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
	public static void modloading(){
		new File(new File("textures/").getAbsoluteFile().getParent()).getAbsolutePath();
		Loading.state1("Loading textures");
		walkTextures(new File("textures/"));
		Loading.state1("Loading blocks");
		new Blocks();
		Loading.state1("Loading tools");
		Tools.create();
		try {
			external = new String(Files.readAllBytes(Paths.get("ext.txt"))).split("\n");
		} catch (IOException e1) {
			debug.pstm(e1, "Unable to load external mods:");
		}
		//Notify user
		state1("Initial load");
		debug.printl("Loading mods");
		debug.printl("Finding all files to load");

		//Load normal textures from cla


		// Find modfiles to load;
		try {
			File f = new File("mods/");
			if (!f.exists() || !f.isDirectory()) {
   				f.mkdirs();
			}
			walkDirectory(new File("mods/")); //to b
		} catch (IOException e) {
			debug.pstm(e, "Couldn't load mods, the list may be incomplete");
		}
		modCount += external.length;
		for(int i = 0; i < external.length; i++) {
			toLoad.add(external[i]);
		}
		state1("Found "+ modCount + " mod files");
		debug.printl("Found "+ modCount + " mod files");
		toLoad.forEach((String p) -> {
			state2("Loading file: " + p);
			try {
				AddonLoader.load(FileGetter.getFile(p));
			} catch (MalformedURLException e) {
				debug.pstm(e, "Unable to load "+p);
			}
		});
		//Wait until all files load
		waitAllLoaders();

		//First runs. Similar process for all three stages
		GameContents.addons.forEach((AddonInfo ai) -> {
			if(ai == null) {
				debug.printl("encountered null mod");
				return;
			}
			if(ai.state == AddonState.ENABLE) {
				Thread thr = new Thread(() -> {
					if(ai.central == null) {
						debug.printl(ai.name + " is not a mod and will not be run");
						ai.state = ai.hasClasses?AddonState.API:AddonState.MEDIA;
					}else {
						debug.printl("Start 1st stage for " + ai.name);
						ai.central.firstOpen();
						debug.printl("End 1st stage for " + ai.name);
					}

				});
				runningFirstRunThreads.add(thr);
				thr.start();
			}
		});
		waitAllFirstRuns();

		//Content runs
		GameContents.addons.forEach((AddonInfo ai) -> {
			if(ai.state == AddonState.ENABLE) {
				Thread thr = new Thread(() -> {
					debug.printl("Start 2nd stage for " + ai.name);
					ai.central.makeContent();
					debug.printl("End 2nd stage for " + ai.name);
				});
				runningContentAddThreads.add(thr);
				thr.start();
			}
		});
		waitAllContentRuns();


		//Integration runs
		GameContents.addons.forEach((AddonInfo ai) -> {
			if(ai.state == AddonState.ENABLE) {
				Thread thr = new Thread(() -> {
					debug.printl("Start 3rd stage for " + ai.name);
					ai.central.makeContent();
					debug.printl("End 3rd stage for " + ai.name);
				});
				runningIntegrationThreads.add(thr);
				thr.start();
			}
		});
		waitAllIntegrationRuns();

		summarizeMods();
		debug.printl("HOORAY, IT'S OVER!");
	}

	@Deprecated
	/**
	 *
	 * @param folder
	 * @throws IOException
	 */
	static void walkDirectory(File folder) throws IOException {
		if(folder.isDirectory()) {
			debug.printl("Directory: " + folder.getCanonicalPath());
		}else {
			debug.printl("File: " + folder.getCanonicalPath());
		}

		File[] modfiles = ListFiles.findFiles(folder);
		for(int i = 0; i < modfiles.length; i++) { //Copy over found modfiles
			try {
				debug.printl("File: " + modfiles[i].getCanonicalPath());
				toLoad.add(modfiles[i].getCanonicalPath());
				modCount++;
			} catch (Exception e) {
				debug.pstm(e, "Couldn't load file "+ modfiles[i].getCanonicalPath());
			}
		}
		File[] modpacks = ListFiles.findDirectories(folder);
		for(int i = 0; i < modpacks.length; i++) {
			walkDirectory(modpacks[i]);
		}
	}
	/**
	 *
	 * @param folder
	 * @param action
	 */
	public static void walkDirectory(File folder, BiConsumer<String,File> action) {
		List<File> files = new ArrayList<File>();
		walkDirectory(folder, files);
		files.forEach((file) -> {
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
	public static void walkDirectory(File folder, List<File> results) {
		try {
			if(folder.isDirectory()) {
				debug.printl("Directory: " + folder.getCanonicalPath());
				File[] modpacks = ListFiles.findDirectories(folder);
				for(int i = 0; i < modpacks.length; i++) {
					walkDirectory(modpacks[i], results);
				}
			}else {
				debug.printl("File: " + folder.getCanonicalPath());
				results.add(folder);
			}
		}catch(Exception e) {
			debug.pstm(e, "THIS MESSAGE INDICATES MALFUNCTION OF FILE PATH SYSTEM OR JAVA. Couldn't get path of the file");
		}

	}

	static void summarizeMods() {
		GameContents.addons.forEach((AddonInfo ai) -> {summarizeMod(ai);});
	}

	static void summarizeMod(AddonInfo ai) {
		debug.printl("=============================================MOD INFORMATION FOR " + ai.name + "=============================================");
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

			default:
				break;
			}
		} catch (Exception e) {
			debug.pstm(e, "Unable to get metadata for " + ai.name);
		}
	}
}
