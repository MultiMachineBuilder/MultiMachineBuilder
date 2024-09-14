/**
 *
 */
package mmb;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import mmb.content.ContentsBlocks;
import mmb.content.ContentsItems;
import mmb.content.ContentsRecipes;
import mmb.content.agro.Agro;
import mmb.content.craft.ManCrafter;
import mmb.content.drugs.Alcohol;
import mmb.content.electric.machines.BlockTransformer.TransformerData;
import mmb.content.electronics.Electronics;
import mmb.content.modular.chest.ModularChests;
import mmb.content.old.Nuker;
import mmb.content.rawmats.Materials;
import mmb.content.stn.STN;
import mmb.engine.MMBUtils;
import mmb.engine.block.Blocks;
import mmb.engine.debug.Debugger;
import mmb.engine.files.FileUtil;
import mmb.engine.generator.Generators;
import mmb.engine.mods.AddonCentral;
import mmb.engine.mods.ModInfo;
import mmb.engine.mods.ModLoader;
import mmb.engine.mods.ModState;
import mmb.engine.mods.Mods;
import mmb.engine.sound.Sounds;
import mmb.engine.texture.Textures;
import mmb.engine.worlds.DataLayers;
import mmb.menu.FullScreen;
import mmb.menu.wtool.Tools;

/**
 * The main loading class
 * @author oskar
 *
 */
public final class GameLoader {	
	private GameLoader() {}
	private static final Debugger debug = new Debugger("MOD-LOADER");

	//Loading stages
	private static int stage = 0;
	private static final Object stageLock = new Object();
	private static List<Runnable> firstRuns = new ArrayList<>();
	/**
	 * Runs the task on the first run stage
	 * @param run task to run
	 */
	public static void onFirstRun(Runnable run) {
		synchronized(stageLock) {
			if(stage < 1) {
				firstRuns.add(run);
			}else {
				run.run();
			}
		}
	}
	private static List<Runnable> contents = new ArrayList<>();
	/**
	 * Runs the task on the content run stage
	 * @param run task to run
	 */
	public static void onContentRun(Runnable run) {
		synchronized(stageLock) {
			if(stage < 2) {
				contents.add(run);
			}else {
				run.run();
			}
		}
	}
	private static List<Runnable> integRuns = new ArrayList<>();
	/**
	 * Runs the task on the integration stage
	 * @param run task to run
	 */
	public static void onIntegRun(Runnable run) {
		synchronized(stageLock) {
			if(stage < 3) {
				integRuns.add(run);
			}else {
				run.run();
			}
		}
	}
	
	/** Loads entire game */
	@SuppressWarnings("null")
	public static void modloading(){
		//Variables
		final File modsDir = new File("mods/");
		final List<ModLoader> loaders = new ArrayList<>();
		
		//Load textures
		Main.state1("Loading textures");
		walkDirectory(new File("textures/"), (s, f) -> {
			debug.printl("Loading a texture "+s);
			try(InputStream i = new FileInputStream(f)) {
				Textures.load(s, i);
			} catch (Exception e) {
				debug.stacktraceError(e, "Failed to load a texture "+s);
			}
		});
		walkDirectory(new File("texpacks/"), (s, f)->{
			debug.printl("Loading a texture pack "+s);
			try(ZipInputStream stream = new ZipInputStream(new FileInputStream(f))){
				ZipEntry entry;
				while((entry = stream.getNextEntry()) != null) {
					debug.printl("Loading a texture "+entry.getName());
					Textures.load(entry.getName(), stream);
				}
			} catch (Exception e) {
				debug.stacktraceError(e, "Failed to load a texture pack "+s);
			}
		});
		
		//Load sounds
		Main.state1("Loading sounds");
		walkDirectory(new File("sound/"), (s, f) -> {
			debug.printl("Loading a sound "+s);
			try(InputStream i = new FileInputStream(f)) {
				Sounds.load(i, s);
			} catch (Exception e) {
				debug.stacktraceError(e, "Failed to load a sound "+s);
			}
		});
		debug.printl("Sounds: "+Sounds.sounds.keySet());
		
		//Load base
		Agro.init();
		Tools.init();
		//Load blocks
		Main.state1("Loading blocks");
		Blocks.init();
		ContentsBlocks.init();
		//Load base materials
		Materials.init();
		Main.state1("Loading items");
		//Load items
		ContentsItems.init();
		Electronics.init();
		//Load datalayers
		DataLayers.init();
		//Load machines and more
		Alcohol.init();
		Main.state1("Loading machines");
		STN.init();
		ManCrafter.init();
		ContentsRecipes.createRecipes();
		Nuker.init();
		Generators.init();
		
		FullScreen.initialize();
		ModularChests.init();
		TransformerData.init();
		
		//Get external mods to load
		Main.state1("Looking for mods");
		Set<String> external = new HashSet<>();
		try {
			String data = new String(Files.readAllBytes(Paths.get("ext.txt")));
			external.addAll(Arrays.asList(data.split("\n")));
			external.remove(""); //Remove the empty line, which always appears if user does not have external mods
		} catch (IOException e1) {
			debug.stacktraceError(e1, "Unable to load list of external mods");
		}
		
		//Add missing 'mods' directory
		if (!modsDir.mkdirs() || !modsDir.isDirectory()) debug.printl("Added missing mods directory");
		
		//Walk 'mods' directory
		List<String> toLoad = new ArrayList<>();
		walkDirectory(modsDir, (s, ff) -> {
			try {
				toLoad.add(ff.getAbsolutePath());
			} catch (Exception e) {
				debug.stacktraceError(e, "Couldn't load file "+ s);
			}
		});
		toLoad.addAll(external); //Add any external mods
		
		//Add mod files for loading
		Main.state1("Found "+ toLoad.size() + " mod files");
		for(String p: toLoad) {
			Main.state2("Loading file: " + p);
			try {
				ModLoader loader = ModLoader.load(FileUtil.getFile(p));
				loaders.add(loader);
			} catch (MalformedURLException e) {
				debug.stacktraceError(e, "The external mod has incorrect URL: "+p);
			}
		}
		
		//Wait until all mods load
		for(ModLoader t: loaders) {
			try {
				t.untilLoad();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				Main.crash(e);
			}
		}
				
		//Find potential mod classes
		Main.state1("Mods - Preparation 0");
		Set<Class<? extends AddonCentral>> classes = new HashSet<>();
		ClassLoader cl = ModLoader.bcl;
		for(String classname: Mods.classnames) {
			try {
				debug.printl("Mod class: "+classname);
				Class<?> cls = cl.loadClass(classname);
				if (AddonCentral.class.isAssignableFrom(cls)) {
					@SuppressWarnings("unchecked")
					Class<? extends AddonCentral> cls0 = (Class<? extends AddonCentral>)cls;
					classes.add(cls0);
				} 
			} catch (Exception e) {
				Main.crash(e);
			}
		}
		
		Main.state1("Mods - Instantiation 0a");
		for(Class<? extends AddonCentral> modcls: classes) {
			try {
				@NN AddonCentral mod = modcls.getConstructor().newInstance();
				ModInfo info = new ModInfo(mod);
				Mods.mods.add(info);
			} catch (Exception e) {
				debug.stacktraceError(e, "Failed to load a mod class "+modcls.getTypeName());
			}
		}
		
		synchronized (stageLock) {
			stage = 1;
			firstRuns.forEach(Runnable::run);
			firstRuns = null;
			//First runs. Similar process for all three stages
			Main.state1("Mods - Phase 1");
			runAll(Mods.mods, ai -> {
				try {
					debug.printl("Start 1st stage for " + ai.meta.name);
					ai.instance.firstOpen();
					debug.printl("End 1st stage for " + ai.meta.name);
				} catch (VirtualMachineError e) {
					Main.crash(e);
				} catch (Throwable e) {//NOSONAR handle exceptions by the mod, VME already handled
					debug.stacktraceError(e, "Failed to run a mod " + ai.meta.name);
					ai.state = ModState.DEAD;
				}
			}, Main::crash);
		}
		
		synchronized (stageLock) {
			stage = 2;
			contents.forEach(Runnable::run);
			contents = null;
			//Content runs
			Main.state1("Mods - Phase 2");
			runAll(Mods.mods, mod -> {
				try{
					debug.printl("Start 2nd stage for " + mod.meta.name);
					mod.instance.makeContent();
					debug.printl("End 2nd stage for " + mod.meta.name);
				}catch(VirtualMachineError e){
					Main.crash(e);
				}catch(Throwable e){//NOSONAR handle exceptions by the mod, VME already handled
					debug.stacktraceError(e, "Failed to run a mod "+ mod.meta.name);
					mod.state = ModState.DEAD;
				}
			}, Main::crash);
		}
		
		synchronized (stageLock) {
			stage = 3;
			integRuns.forEach(Runnable::run);
			integRuns = null;
			//Integration runs
			Main.state1("Mods - Phase 3");
			runAll(Mods.mods, mod -> {
				try{
				debug.printl("Start 3rd stage for " + mod.meta.name);
				mod.instance.makeContent();
				debug.printl("End 3rd stage for " + mod.meta.name);
				}catch(VirtualMachineError e){
					Main.crash(e);
				}catch(Throwable e){//NOSONAR handle exceptions by the mod, VME already handled
					debug.stacktraceError(e, "Failed to run a mod "+ mod.meta.name);
					mod.state = ModState.DEAD;
				}
			}, Main::crash);
		}
		
		//clean up
		loaders.clear();
		
		//Summarize mods
		for(ModInfo ai1: Mods.mods) {
			debug.printl("===MOD INFORMATION FOR " + ai1.meta.name + "===");
			debug.printl(ai1.state.title);
			try {
				switch(ai1.state) {
				case DEAD:
				case DISABLE:
				case ENABLE:
					if(ai1.meta.release == null) {
						debug.printl("RELEASED AT UNKNOWN DATE");
					}else {
						debug.printl("RELEASED " + ai1.meta.release.toString());
					}
					debug.printl("MADE BY " + ai1.meta.author);
					debug.printl("DESCRIPTION: " + ai1.meta.description);
					break;
				default:
					break;
				}
			} catch (Exception e) {
				debug.stacktraceError(e, "Unable to get metadata for " + ai1.meta.name);
			}
		}
		
		Textures.displayAtlas();
		
		//everything done
		debug.printl("HOORAY, IT'S OVER!");
	}
	
	/**
	 * Walk the directory by invoking the action
	 * @param f root
	 * @param action action to run in form of (file name, file)
	 */
	public static void walkDirectory(File f, BiConsumer<String,File> action) {
		debug.printl("Walking: "+f.getAbsolutePath());
		String abs = f.getAbsolutePath();
		int len = abs.length()+1;
		if(abs.endsWith("/") || abs.endsWith("\\")) len++;
		walkDirectory(len, f, action);
	}
	private static void walkDirectory(int absLen, File f, BiConsumer<String,File> action) {
		try {
			File[] walk = f.listFiles();
			if(walk == null) {
				debug.printl("File: " + f.getCanonicalPath());
				if(absLen > f.getAbsolutePath().length()) {//path length check fail
					debug.printl("Path length check fail: " +absLen+" out of "+f.getAbsolutePath().length());
					return; 
				}
				String tname = f.getAbsolutePath().substring(absLen);
				action.accept(tname, f);
			}else {
				debug.printl("Directory: " + f.getCanonicalPath());
				for(int i = 0; i < walk.length; i++) {
					File file2 = walk[i];
					if(file2 == null) throw new InternalError("Fatal error in File.listFiles()");
					walkDirectory(absLen, file2, action);
				}
			}
			
		} catch (IOException e) {
			debug.stacktraceError(e, "THIS MESSAGE INDICATES MALFUNCTION OF FILE PATH SYSTEM OR JAVA. Couldn't get path of the file");
		}
	}
	
	/**
	 * Walk the directory by adding all files to the list
	 * @param folder root
	 * @param results output
	 */
	public static void walkDirectory(File folder, List<File> results) {
		walkDirectory(folder, (s, f) -> results.add(f));
	}

	/**
	 * Runs all tasks in parallel
	 * @param <T> data type of data
	 * @param collect data source
	 * @param cons task to perform on data
	 * @param onInterrupt handles interrupts
	 */
	public static <T> void runAll(Iterable<T> collect, Consumer<T> cons, Consumer<InterruptedException> onInterrupt){
		List<Thread> threads = new ArrayList<>();
		for(T item: collect) {
			threads.add(new Thread(MMBUtils.loadValue(cons, item)));
		}
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
