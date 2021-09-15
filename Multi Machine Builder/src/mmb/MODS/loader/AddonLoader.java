/**
 * 
 */
package mmb.MODS.loader;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.*;
import java.util.jar.*;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import io.github.micwan88.helperclass4j.ByteClassLoader;
import mmb.DATA.contents.*;
import mmb.DATA.contents.sound.Sounds;
import mmb.DATA.contents.texture.Textures;
import mmb.FILES.AdvancedFile;
import mmb.LAMBDAS.Consumers;
import mmb.MODS.info.*;
import mmb.debug.*;

/**
 * @author oskar
 *
 */
public class AddonLoader {
	private Debugger debug;
	/**
	 * This mod loader's loaded mod
	 */
	public final AddonInfo a = new AddonInfo();
	String originalPath;
	private Thread runningOn;
	/**
	 * Waits until mod finishes loading
	 * @throws InterruptedException when loading stops abruptly
	 */
	public void untilLoad() throws InterruptedException{
		runningOn.join();
	}
	//Variables created in stage 1
	
	private static final String PREFIX_TEXTURE = "textures/";
	private static final String PREFIX_SOUND = "sound/";
	
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
	public static AddonLoader load(AdvancedFile source, Consumer<AddonInfo> handler) {
		return new AddonLoader(source, handler);
	}

	/**
	 * @param a
	 * @param originalPath
	 */
	protected AddonLoader(AdvancedFile source) {
		this(source, Consumers.doNothing());
	}
	
	protected AddonLoader(AdvancedFile source, Consumer<AddonInfo> handler) {
		super();
		this.originalPath = source.name();
		a.file = source;
		runningOn = new Thread(() -> {init(); GameContents.addons.add(a); handler.accept(a);});
		ModLoader.loaders.add(this);
		runningOn.start();
	}
	
	/**
	 * Determine if file can be loaded
	 */
	private void init() {
		String debugName = AdvancedFile.dirName(a.file.name())[1];
		debug = new Debugger("MOD - "+ debugName);
		a.name = originalPath;
		a.path = originalPath;
		
		//Trying to open file
		if(a.file.isDirectory()) {
			debug.printl("The mod is a directory, aborting");
			return;
		}
		try(InputStream in = a.file.getInputStream()) {
			debug.printl("Opening a modfile: " + a.name);
			if(!a.file.exists()) {
				debug.printl(a.file.name()+" does not exist.");
				a.state = AddonState.NOEXIST;
				return;
			}
			if(!a.isOnline()) a.name = AdvancedFile.dirName(a.file.name())[1];
			unzip(in);
		}catch(IllegalArgumentException e) {
			debug.pstm(e, "Make sure that \"" + a.name + "\" is not mistyped.");
			a.state = AddonState.NOEXIST;
		}catch(IOException e) {
			if(a.isOnline()) debug.pstm(e, "Failed to download the file " + a.file.name());
			else debug.pstm(e, "Failed to read the file " + a.file.name());
			a.state = AddonState.NOEXIST;
		}catch(Exception e) {
			debug.pstm(e, "Couldn't read " + a.name + " for unknown reasons");
			a.state = AddonState.NOEXIST;
		}
	}
	
	private void unzip(InputStream in) {
		//Unzip the file
		try(JarInputStream jis = new JarInputStream(in)) {
			//Trying to load modfile
			debug.printl("Loading a modfile: " + a.name);
			while(true) {
				JarEntry je = jis.getNextJarEntry();
				if(je == null) break;
				debug.printl("entry: "+je.getName());
				byte[] bytes = IOUtils.toByteArray(jis);
				a.contents.add(je);
				a.files.put(je, bytes);
			}
			//If file works, continue loading
			debug.printl("Injecting "+a.file.name());
			a.files.forEach(this::processFile);
			for(Class<? extends AddonCentral> c : cclasses) {
				String cname = c.getName();
				try {
					AddonCentral central = c.newInstance();
					a.central = central;
					runCentralClass(central);
				} catch (IllegalAccessException e) {
					debug.pstm(e, "The constructor for" + cname +  " is non-public or absent. Make the constructor public");
				} catch(Exception e) {
					debug.pstm(e, "Couldn't create the main instance of the mod " + a.name);
				}
			}
			
			//If file is empty, mark it
			if(!a.hasValidData) a.state = AddonState.EMPTY;
		}catch(Exception e) {
			debug.printl("Couldn't open zip or jar file " + a.name);
			debug.pst(e);
			a.state = AddonState.BROKEN;
		}
	}
	
	private void processFile(JarEntry meta, byte[] data){
		String name = meta.getName();
		@SuppressWarnings("null")
		String[] tmp = AdvancedFile.baseExtension(name);
		String ext = tmp[1];
		debug.printl("Processing "+name);
		if(meta.isDirectory()) {
			debug.printl("Directory: "+name);
		}else {
			a.hasValidData = true;
			debug.printl("File: "+name);
			if(ext.endsWith("class")) {
				interpretClassFile(meta, data);
			}else if(ext.equals("mcmod")) {
				debug.printl("Found Minecraft mod (found mcmod.info)");
			}else if(name.startsWith(PREFIX_TEXTURE)){
				try {
					String shorter = name.substring(PREFIX_TEXTURE.length());
					BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
					Textures.load(shorter, img);
					debug.printl("Successfully added texture " + name);
				} catch (Exception e) {
					debug.printl("Couldn't open texture " + name);
					debug.pst(e);
				}
			}else if(name.startsWith(PREFIX_SOUND)) {
				debug.printl("Sound file");
				String shorter = name.substring(PREFIX_SOUND.length());
				Sounds.load(new ByteArrayInputStream(a.files.get(meta)), shorter);
			}
		}
	}
	private static ByteClassLoader bcl = new ByteClassLoader(AddonLoader.class.getClassLoader());
	@SuppressWarnings("null")
	private void interpretClassFile(JarEntry ent, byte[] data){
		// This JarEntry represents a class. Now, what class does it represent?
		String name = ent.getName();
		debug.printl("entry: "+name);
        String className = name.replace('/', '.');
        className = className.substring(0, className.length() - ".class".length());
        debug.printl("Class: "+className);
        try {
			@SuppressWarnings("rawtypes")
			Class c = bcl.loadClass(className, data, false);
			tryAddCentral(c);
			GameContents.loadedClasses.add(c);
		} catch (ClassNotFoundException e) {
			debug.pstm(e, "Failed to find class "+name);
			a.state = AddonState.DEAD;
		} catch (ClassFormatError e) {
			debug.pstm(e, "Invalid class file: "+name);
			a.state = AddonState.DEAD;
		} catch(NoClassDefFoundError e) {
			debug.pstm(e, "Missing dependencies in "+name);
			a.state = AddonState.DEAD;
		}catch (IOException e) {
			debug.pstm(e, "Failed to open resource "+name);
			a.state = AddonState.BROKEN;
		} catch (Exception e) {
			debug.pstm(e, "Couldn't process classfile " + className + ". Please check if class file exists and works.");
			a.state = AddonState.DEAD;
		}
	}
	private List<Class<? extends AddonCentral>> cclasses = new ArrayList<>();
	@SuppressWarnings("unchecked")
	private void tryAddCentral(Class<?> c) {
		boolean runnable = AddonCentral.class.isAssignableFrom(c);//if class implements AddonCentral, run it
    	if(runnable) { //The given class is a mod central class
    		if(!cclasses.isEmpty()) debug.printl("This file contains mutiple mods");
    		cclasses.add((Class<? extends AddonCentral>) c); 
    	}
    		
    	a.hasClasses = true;
    }
    	
	
	void runCentralClass(AddonCentral c) {
		a.mmbmod = c.info();
		a.name = a.mmbmod.name;
	}
}
