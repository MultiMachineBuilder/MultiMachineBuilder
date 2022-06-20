/**
 * 
 */
package mmb.MODS.loader;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.function.*;

import javax.annotation.Nonnull;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.io.IOUtils;

import io.github.micwan88.helperclass4j.ByteClassLoader;
import mmb.FILES.AdvancedFile;
import mmb.LAMBDAS.Consumers;
import mmb.MODS.info.*;
import mmb.debug.*;

/**
 * @author oskar
 *
 */
public class AddonLoader {
	public final AdvancedFile file;
	/**
	 * This mod loader's loaded mod
	 */
	@Nonnull public final Modfile modfile;
	@Nonnull public final String originalPath;
	@Nonnull public final String name;
	@Nonnull private Thread runningOn;
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
	public static AddonLoader load(AdvancedFile source) {
		return new AddonLoader(source);
	}
	
	/**
	 * Uses optional handler, for function without handler see load(Path)
	 * Load addon from path
	 * @param source mod file
	 * @param handler optional handler
	 * @return new loader
	 */
	public static AddonLoader load(AdvancedFile source, Consumer<Modfile> handler) {
		return new AddonLoader(source, handler);
	}

	/**
	 * @param a
	 * @param originalPath
	 */
	protected AddonLoader(AdvancedFile source) {
		this(source, Consumers.doNothing());
	}
	
	protected AddonLoader(AdvancedFile source, Consumer<Modfile> handler) {
		super();
		this.originalPath = source.name();
		this.file = source;
		modfile = new Modfile(originalPath, file);
		String name0 = originalPath;
		if(!modfile.isOnline()) name0 = AdvancedFile.dirName(file.name())[1];
		name = name0;
		runningOn = new Thread(() -> {init(); Mods.files.add(modfile); handler.accept(modfile);});
		ModLoader.loaders.add(this);
		runningOn.start();
	}
	
	/**
	 * Determine if file can be loaded
	 */
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
			debug.pstm(e, "Make sure that \"" + name + "\" is not mistyped.");
			modfile.state = ModfileState.NOEXIST;
		}catch(IOException e) {
			if(modfile.isOnline()) debug.pstm(e, "Failed to download the file " + file.name());
			else debug.pstm(e, "Failed to read the file " + file.name());
			modfile.state = ModfileState.NOEXIST;
		}catch(Exception e) {
			debug.pstm(e, "Couldn't read " + name + " for unknown reasons");
			modfile.state = ModfileState.NOEXIST;
		}catch(OutOfMemoryError e) {
			debug.pstm(e, "The mod is too big to load");
			modfile.state = ModfileState.BLOATED;
		}
		
		//Part 2: classpath load the file
		try{
			bcl.loadJarDataInBytes(data);
		}catch (ClassFormatError e) {
			debug.pstm(e, "Invalid class file");
			modfile.state = ModfileState.DEAD;
		} catch(NoClassDefFoundError e) {
			debug.pstm(e, "Missing dependencies");
			modfile.state = ModfileState.DEAD;
		} catch (IOException e) {
			debug.printl("Couldn't open zip or jar file " + file.name());
			debug.pst(e);
			modfile.state = ModfileState.BROKEN;
		} catch (Exception e) {
			debug.pstm(e, "Couldn't process classfile(s). Please check if class file exists and works.");
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
			debug.pst(e);
			modfile.state = ModfileState.BROKEN;
		}
		
		//Part 3A: mark empty mods
		if(!modfile.hasValidData) {
			modfile.state = ModfileState.EMPTY;
		}
	}
	
	/**
	 * The class loader used to load mods
	 */
	public static final ByteClassLoader bcl = new ByteClassLoader(ClassLoader.getSystemClassLoader());
}
