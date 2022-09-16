/**
 *
 */
package mmb.MODS.loader;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import mmb.debug.Debugger;
import mmb.Main;
import mmb.DATA.contents.Sounds;
import mmb.DATA.contents.Textures;
import mmb.FILES.FileUtil;
import mmb.LAMBDAS.Lambdas;
import mmb.MENU.FullScreen;
import mmb.MODS.info.AddonCentral;
import mmb.MODS.info.ModInfo;
import mmb.MODS.info.ModMetadata;
import mmb.MODS.info.ModState;
import mmb.MODS.info.Mods;
import mmb.SOUND.MP3Loader;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.blocks.machine.Nuker;
import mmb.WORLD.blocks.machine.line.Furnace;
import mmb.WORLD.blocks.machine.manual.Crafting;
import mmb.WORLD.contentgen.Materials;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.electromachine.BlockTransformer.TransformerData;
import mmb.WORLD.generator.Generators;
import mmb.WORLD.items.ContentsItems;
import mmb.WORLD.items.electronics.Electronics;
import mmb.WORLD.tool.Tools;

/**
 * @author oskar
 *
 */
public final class ModLoader {
	private ModLoader() {}
	private static final Debugger debug = new Debugger("MOD-LOADER");

	/** Loads entire game */
	@SuppressWarnings("null")
	public static void modloading(){
		//Variables
		final File modsDir = new File("mods/");
		final List<AddonLoader> loaders = new ArrayList<>();
		final List<MP3Loader> mp3s = new ArrayList<>();
		
		//Load textures
		Main.state1("Loading textures");
		walkDirectory(new File("textures/"), (s, f) -> {
			debug.printl("Loading a texture "+s);
			try(InputStream i = new FileInputStream(f)) {
				Textures.load(s, i);
			} catch (Exception e) {
				debug.pstm(e, "Failed to load a texture "+s);
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
				debug.pstm(e, "Failed to load a texture pack "+s);
			}
		});
		
		//Load sounds
		Main.state1("Loading sounds");
		walkDirectory(new File("sound/"), (s, f) -> {
			debug.printl("Loading a sound "+s);
			try(InputStream i = new FileInputStream(f)) {
				Sounds.load(i, s);
			} catch (Exception e) {
				debug.pstm(e, "Failed to load a sound "+s);
			}
		});
		debug.printl("Sounds: "+Sounds.sounds.keySet());
		
		//Load base materials
		Materials.init();
		VoltageTier[] volts = VoltageTier.values();
		for(VoltageTier volt: volts) {
			debug.printl(volt.name+", structural: "+volt.construction+", electrical: "+volt.electrical);
		}
		
		//Load blocks
		Main.state1("Loading blocks");
		ContentsBlocks.init();
		//Load items
		Main.state1("Loading items");
		ContentsItems.init();
		Electronics.init();
		//Load machines
		Main.state1("Loading machines");
		Crafting.init();
		Nuker.init();
		Generators.init();
		Tools.init();
		FullScreen.initialize();
		TransformerData.init();
		
		//Get external mods to load
		Main.state1("Looking for mods");
		Set<String> external = new HashSet<>();
		try {
			String data = new String(Files.readAllBytes(Paths.get("ext.txt")));
			external.addAll(Arrays.asList(data.split("\n")));
			external.remove(""); //Remove the empty line, which always appears if user does not have external mods
		} catch (IOException e1) {
			debug.pstm(e1, "Unable to load list of external mods");
		}
		
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
		
		//Add mod files for loading
		Main.state1("Found "+ toLoad.size() + " mod files");
		for(String p: toLoad) {
			Main.state2("Loading file: " + p);
			try {
				AddonLoader loader = AddonLoader.load(FileUtil.getFile(p));
				loaders.add(loader);
			} catch (MalformedURLException e) {
				debug.pstm(e, "The external mod has incorrect URL: "+p);
			}
		}
		
		//Wait until all mods load
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
				
		//Find potential mod classes
		Main.state1("Mods - Preparation 0");
		Set<Class<? extends AddonCentral>> classes = new HashSet<>();
		ClassLoader cl = AddonLoader.bcl;
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
				AddonCentral mod = modcls.getConstructor().newInstance(); //textures not yet loaded
				ModMetadata data = mod.info();
				ModInfo info = new ModInfo(mod, data);
				Mods.mods.add(info);
			} catch (Exception e) {
				debug.pstm(e, "Failed to load a mod class "+modcls.getTypeName());
			}
		}
		
		//First runs. Similar process for all three stages
		Main.state1("Mods - Phase 1");
		runAll(Mods.mods, ai -> {
			try{
				debug.printl("Start 1st stage for " + ai.meta.name);
				ai.instance.firstOpen();
				debug.printl("End 1st stage for " + ai.meta.name);
			}catch(VirtualMachineError e){
				Main.crash(e);
			// deepcode ignore DontCatch: guarantee that game fully loads
			}catch(Throwable e){
				debug.pstm(e, "Failed to run a mod "+ ai.meta.name);
				ai.state = ModState.DEAD;
			}
		}, Main::crash);
		
		//Content runs
		Main.state1("Mods - Phase 2");
		runAll(Mods.mods, mod -> {
			try{
				debug.printl("Start 2nd stage for " + mod.meta.name);
				mod.instance.makeContent();
				debug.printl("End 2nd stage for " + mod.meta.name);
			}catch(VirtualMachineError e){
				Main.crash(e);
			// deepcode ignore DontCatch: guarantee that game fully loads, VM errors used to crash the game earlier
			}catch(Throwable e){
				debug.pstm(e, "Failed to run a mod "+ mod.meta.name);
				mod.state = ModState.DEAD;
			}
		}, Main::crash);
		
		//Integration runs
		Main.state1("Mods - Phase 3");
		runAll(Mods.mods, mod -> {
			try{
			debug.printl("Start 3rd stage for " + mod.meta.name);
			mod.instance.makeContent();
			debug.printl("End 3rd stage for " + mod.meta.name);
			}catch(VirtualMachineError e){
				Main.crash(e);
			}catch(Throwable e){
				debug.pstm(e, "Failed to run a mod "+ mod.meta.name);
				mod.state = ModState.DEAD;
			}
		}, Main::crash);
		
		//clean up
		loaders.clear();
		mp3s.clear();
		
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
				debug.pstm(e, "Unable to get metadata for " + ai1.meta.name);
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
			debug.pstm(e, "THIS MESSAGE INDICATES MALFUNCTION OF FILE PATH SYSTEM OR JAVA. Couldn't get path of the file");
		}
	}
	
	/**
	 * Walk the directory by adding all files to the list
	 * @param folder root
	 * @param results output
	 */
	@SuppressWarnings("null")
	public static void walkDirectory(File folder, List<File> results) {
		walkDirectory(folder, (s, f) -> results.add(f));
	}

	@SuppressWarnings("null")
	public static <T> void runAll(Iterable<T> collect, Consumer<T> cons, Consumer<InterruptedException> onInterrupt){
		List<Thread> threads = new ArrayList<>();
		for(T item: collect) {
			threads.add(new Thread(Lambdas.loadValue(cons, item)));
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
