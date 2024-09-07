/**
 * 
 */
package mmb.engine.mods;

import java.io.*;
import java.util.function.*;

import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.io.IOUtils;

import io.github.micwan88.helperclass4j.ByteClassLoader;
import mmb.NN;
import mmb.engine.MMBUtils;
import mmb.engine.debug.*;
import mmb.engine.files.AdvancedFile;

/**
 * A class which loads a single file
 * @author oskar
 */
public class ModLoader {
	/** The file to load*/
	public final AdvancedFile file;
	/** This mod loader's loaded modfile */
	@NN public final Modfile modfile;
	/** The path reference to the mod */
	@NN public final String originalPath;
	/** The display name of this mod loader */
	@NN public final String name;
	@NN private Thread runningOn;
	/**
	 * Waits until mod finishes loading
	 * @throws InterruptedException when loading stops abruptly
	 */
	public void untilLoad() throws InterruptedException{
		runningOn.join();
	}
	/**
	 * Can use optional handler, for this purpose see load(Path, Consumer<AddonInfo>)
	 * Load addon from path
	 * @param source mod file
	 * @return new loader
	 */
	public static ModLoader load(AdvancedFile source) {
		return new ModLoader(source);
	}
	
	/**
	 * Uses optional handler, for function without handler see load(Path)
	 * Load addon from path
	 * @param source mod file
	 * @param handler optional handler
	 * @return new loader
	 */
	public static ModLoader load(AdvancedFile source, Consumer<Modfile> handler) {
		return new ModLoader(source, handler);
	}

	/**
	 * Creates a mod-loader
	 * @param a
	 * @param originalPath
	 */
	protected ModLoader(AdvancedFile source) {
		this(source, MMBUtils.doNothing());
	}
	
	protected ModLoader(AdvancedFile source, Consumer<Modfile> handler) {
		super();
		this.originalPath = source.name();
		this.file = source;
		modfile = new Modfile(originalPath, file);
		String name0 = originalPath;
		if(!modfile.isOnline()) name0 = AdvancedFile.dirName(file.name())[1];
		name = name0;
		runningOn = new Thread(() -> {init(); Mods.files.add(modfile); handler.accept(modfile);});
		runningOn.start();
	}
	
	/** Loads the file */
	private void init() {
		//Part -1: initialize
		String debugName = AdvancedFile.dirName(file.name())[1];
		Debugger debug = new Debugger("MOD - "+ debugName);
		
		//Part 0: pre-download checks
		if(file.isDirectory()) {
			debug.printl("The mod is a directory, aborting");
			modfile.state = ModfileState.DIRECTORY;
			return;
		}
		
		//Part 1: download
		byte[] data = null;
		try(InputStream in = file.getInputStream()) {
			debug.printl("Opening a modfile: " + file.name());
			if(!file.exists()) {
				debug.printl(file.name()+" does not exist.");
				modfile.state = ModfileState.NOEXIST;
				return;
			}
			data = IOUtils.toByteArray(in); //take in byte array and load data
		}catch(IllegalArgumentException e) {
			if("Cannot read more than 2 147 483 647 into a byte array".equals(e.getMessage())) {
				debug.stacktraceError(e, "The mod is too big to load");
				modfile.state = ModfileState.BLOATED;
			}else {
				debug.stacktraceError(e, "Make sure that \"" + name + "\" is not mistyped.");
				modfile.state = ModfileState.NOEXIST;
			}
		}catch(IOException e) {
			if(modfile.isOnline()) debug.stacktraceError(e, "Failed to download the file " + file.name());
			else debug.stacktraceError(e, "Failed to read the file " + file.name());
			modfile.state = ModfileState.NOEXIST;
		}catch(Exception e) {
			debug.stacktraceError(e, "Couldn't read " + name + " for unknown reasons");
			modfile.state = ModfileState.NOEXIST;
		}catch(OutOfMemoryError e) {
			debug.stacktraceError(e, "The mod is too big to load");
			modfile.state = ModfileState.BLOATED;
		}
		
		//Part 1a: check for empty byte arrays
		if(data == null || data.length == 0) {
			modfile.state = ModfileState.EMPTY;
			return;
		}
		
		//Part 2: classpath load the file
		try{
			bcl.loadJarDataInBytes(data);
		}catch (ClassFormatError e) {
			debug.stacktraceError(e, "Invalid class file");
			modfile.state = ModfileState.DEAD;
		} catch(NoClassDefFoundError e) {
			debug.stacktraceError(e, "Missing dependencies");
			modfile.state = ModfileState.DEAD;
		} catch (IOException e) {
			debug.printl("Couldn't open zip or jar file " + file.name());
			debug.stacktrace(e);
			modfile.state = ModfileState.BROKEN;
		} catch (Exception e) {
			debug.stacktraceError(e, "Couldn't process classfile(s). Please check if class file exists and works.");
			modfile.state = ModfileState.DEAD;
		}
		
		if(modfile.state != ModfileState.MEDIA) return; //The file is broken
		
		//Part 3: find contents
		try(JarArchiveInputStream jis = new JarArchiveInputStream(new ByteArrayInputStream(data))) {
			while(true) {
				JarArchiveEntry je = jis.getNextJarEntry();
				if(je == null) break; //No more entries, break
				if(modfile.state != ModfileState.DEAD &&  je.getName().endsWith(".class")) modfile.state = ModfileState.API;
				debug.printl("entry: "+je.getName());
				if(je.getName().endsWith(".class"))
					modfile.addClassName(je.getName());
				modfile.hasValidData = true;
			}
		}catch(Exception e) {
			debug.printl("Couldn't open zip or jar file " + file.name());
			debug.stacktrace(e);
			modfile.state = ModfileState.BROKEN;
			return;
		}
		
		//Part 3A: mark empty mods
		if(!modfile.hasValidData) {
			modfile.state = ModfileState.EMPTY;
		}
	}
	
	/** The class loader used to load mods */
	public static final ByteClassLoader bcl = new ByteClassLoader(ClassLoader.getSystemClassLoader());
}
