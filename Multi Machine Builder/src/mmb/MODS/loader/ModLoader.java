/**
 *
 */
package mmb.MODS.loader;

import static mmb.Loading.*;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import mmb.debug.Debugger;
import mmb.Main;
import mmb.DATA.contents.GameContents;
import mmb.DATA.contents.texture.Textures;
import mmb.ERRORS.UndeclarableThrower;
import mmb.FILES.FileUtil;
import mmb.LAMBDAS.Lambdas;
import mmb.MENU.FullScreen;
import mmb.MODS.info.AddonInfo;
import mmb.MODS.info.AddonState;
import mmb.SOUND.MP3Loader;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.blocks.machine.Furnace;
import mmb.WORLD.blocks.machine.Nuker;
import mmb.WORLD.blocks.pipe.Pipes;
import mmb.WORLD.generator.Generators;
import mmb.WORLD.items.ContentsItems;
import mmb.WORLD.tool.Tools;

/**
 * @author oskar
 *
 */
public final class ModLoader {
	private ModLoader() {}

	private static final Debugger debug = new Debugger("MOD-LOADER");

	static List<AddonLoader> loaders = new ArrayList<>();
	static List<MP3Loader> mp3s = new ArrayList<>();
	private static int modCount;
	/** @return number of mods*/
	public static int modCount() {
		return modCount;
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
	
	/**
	 * Loads entire game
	 */
	@SuppressWarnings("null")
	public static void modloading(){
		Main.state1("Loading textures");
		walkTextures(new File("textures/"));
		
		Main.state1("Loading blocks");
		ContentsBlocks.init();
		Pipes.init();
		
		Main.state1("Loading items");
		ContentsItems.init();
		
		Main.state1("Loading machines");
		Nuker.init();
		Generators.init();
		Furnace.init();
		Tools.init();
		FullScreen.initialize();
		
		//Get external mods to load
		Set<String> external = new HashSet<>();
		try {
			String data = new String(Files.readAllBytes(Paths.get("ext.txt")));
			external.addAll(Arrays.asList(data.split("\n")));
			external.remove(""); //Remove the empty line, which always appears if user does not have external mods
		} catch (IOException e1) {
			debug.pstm(e1, "Unable to load list of external mods");
		}
		
		//Notify user
		Main.state1("Initial load");
		debug.printl("Loading mods");
		debug.printl("Finding all files to load");
		
		//Add missing 'mods' directory
		if (!modsDir.mkdirs() || !modsDir.isDirectory()) debug.printl("Added missing mods directory");
		
		//Walk 'mods' directory
		List<String> toLoad = new ArrayList<>();
		walkDirectory(modsDir, (s, ff) -> {
			try {
				toLoad.add(ff.getAbsolutePath());
			} catch (Exception e) {
				debug.pstm(e, "Couldn't load file "+ s);
			}
		});
		toLoad.addAll(external); //Add any external mods
		modCount = toLoad.size();
		
		//Add mod files for loading
		Main.state1("Found "+ modCount + " mod files");
		debug.printl("Found "+ modCount + " mod files");
		for(String p: toLoad) {
			Main.state2("Loading file: " + p);
			try {
				AddonLoader.load(FileUtil.getFile(p));
			} catch (MalformedURLException e) {
				debug.pstm(e, "The external mod has incorrect URL: "+p);
			}
		}
		
		//Wait until all files load
		for(AddonLoader t: loaders) {
			try {
				t.untilLoad();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				Main.crash(e);
			}
		}
		
		//Wait until MP3s load
		for(MP3Loader mp3: mp3s) {
			try {
				mp3.untilLoad();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				Main.crash(e);
			}
		}
		
		//First runs. Similar process for all three stages
		List<Thread> firstRuns = new ArrayList<>();
		CountDownLatch latch = new CountDownLatch(GameContents.addons.size()-1);
		for(AddonInfo ai: GameContents.addons){
			if(ai.state == AddonState.ENABLE) {
				Thread thr = new Thread(() -> {
					try{
						if(ai.central == null) {
							debug.printl(ai.name + " is not a mod and will not be run");
							ai.state = ai.hasClasses?AddonState.API:AddonState.MEDIA;
						}else{
							debug.printl("Start 1st stage for " + ai.name);
							ai.central.firstOpen();
							debug.printl("End 1st stage for " + ai.name);
						}
					}catch(VirtualMachineError e){
						Main.crash(e);
						throw e; //unreachable
					// deepcode ignore DontCatch: guarantee that game fully loads
					}catch(Throwable e){
						debug.pstm(e, "Failed to run a mod "+ ai.name);
						ai.state = AddonState.DEAD;
					}finally{
						latch.countDown();	
					}	
				});
				firstRuns.add(thr);
				thr.start();
			}else latch.countDown();	
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			Main.crash(e);
		}
		
		//Content runs
		CountDownLatch latch1 = new CountDownLatch(GameContents.addons.size()-1);
		List<Thread> contents = new ArrayList<>();
		GameContents.addons.forEach(ai1 -> {
			if(ai1.state == AddonState.ENABLE) {
				Thread thr = new Thread(() -> {
					try{
						debug.printl("Start 2nd stage for " + ai1.name);
						ai1.central.makeContent();
						debug.printl("End 2nd stage for " + ai1.name);
					}catch(VirtualMachineError e){
						Main.crash(e);
					// deepcode ignore DontCatch: guarantee that game fully loads, VM errors used to crash the game earlier
					}catch(Throwable e){
						debug.pstm(e, "Failed to run a mod "+ ai1.name);
						ai1.state = AddonState.DEAD;
					}finally{
						latch1.countDown();
					}
				});
				contents.add(thr);
				thr.start();
			} else latch1.countDown();
		});
		try {
			latch1.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			Main.crash(e);
		}
		
		//Integration runs
		CountDownLatch latch2 = new CountDownLatch(GameContents.addons.size()-1);
		List<Thread> integrators = new ArrayList<>();
		GameContents.addons.forEach(ai2 -> {
			if(ai2.state == AddonState.ENABLE) {
				Thread thr = new Thread(() -> {
					try{
						debug.printl("Start 3rd stage for " + ai2.name);
						ai2.central.makeContent();
						debug.printl("End 3rd stage for " + ai2.name);
					}catch(VirtualMachineError e){
						Main.crash(e);
					}catch(Throwable e){
						debug.pstm(e, "Failed to run a mod "+ ai2.name);
						ai2.state = AddonState.DEAD;
					}finally{
						latch2.countDown();
					}
				});
				integrators.add(thr);
				thr.start();
			}else latch2.countDown();
		});
		try {
			latch2.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			Main.crash(e);
		}
		
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
	 * Walk the directory by invoking the action
	 * @param folder root
	 * @param action action to run in form of (file name, file)
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
	
	public static <T> void runAll(Collection<T> collect, Consumer<T> cons, Consumer<InterruptedException> onInterrupt){
		@SuppressWarnings("null")
		List<Thread> threads = collect.parallelStream().map(val -> new Thread(Lambdas.loadValue(cons, val))).collect(Collectors.toList());
		for(Thread t: threads) {
			t.start();
		}
		for(Thread t: threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				onInterrupt.accept(e);
			}
		}
	}
}
