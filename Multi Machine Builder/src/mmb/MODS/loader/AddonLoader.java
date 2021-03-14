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
import mmb.MODS.info.*;
import mmb.debug.*;

/**
 * @author oskar
 *
 */
public class AddonLoader {
	private Debugger debug;
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
	
	void unaval() {
		debug.printl("Oh noes, " + a.name + " is inoperative, no fun with it.");	
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
	public static AddonLoader load(AdvancedFile source, Consumer<AddonInfo> handler) {
		return new AddonLoader(source, handler);
	}

	/**
	 * @param a
	 * @param originalPath
	 */
	protected AddonLoader(AdvancedFile source) {
		super();
		this.originalPath = source.name();
		a.file = source;
		runningOn = new Thread(() -> {init(); GameContents.addons.add(a);});
		ModLoader.loaders.add(this);
		runningOn.start();
	}
	
	protected AddonLoader(AdvancedFile source, Consumer<AddonInfo> handler) {
		super();
		this.originalPath = source.name();
		a.file = source;
		runningOn = new Thread(() -> {init(); handler.accept(a); GameContents.addons.add(a);});
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
		}catch(Exception e) {
			debug.printl(e.getClass().getCanonicalName());
			if(e instanceof IllegalArgumentException) debug.printl("Make sure that \"" + a.name + "\" is not mistyped.");
			else if(e instanceof IOException || e instanceof FileNotFoundException) {
				if(a.isOnline()) debug.printl("Failed to download the file " + a.file.name());
				else debug.printl("Failed to read the file " + a.file.name());
			}
			else debug.printl("Couldn't read " + a.name + " for unknown reasons");
			
			
			debug.pst(e);
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
			whenWorking();
			if(!a.hasValidData) a.state = AddonState.EMPTY;
		}catch(Exception e) {
			debug.printl("Couldn't open zip or jar file " + a.name);
			debug.pst(e);
			a.state = AddonState.BROKEN;
		}
	}
	
	/**
	 * If file works, continue loading
	 */
	private void whenWorking() {
		debug.printl("Injecting "+a.file.name());
		a.files.forEach(this::processFile);
		runCentrals();
	}
	
	private void processFile(JarEntry meta, byte[] data){
		String name = meta.getName();
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
					BufferedImage img = ImageIO.read(new ByteArrayInputStream(a.files.get(meta)));
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
		// This FileObject represents a class. Now, what class does it represent?
		
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
		} catch (ClassFormatError e) {
			debug.pstm(e, "Invalid class file: "+name);
			a.state = AddonState.DEAD;
		} catch(NoClassDefFoundError e) {
			debug.pstm(e, "Missing dependencies in "+name);
			a.state = AddonState.DEAD;
		}catch (IOException e) {
			debug.pstm(e, "Failed to open resource "+name);
		} catch (Exception e) {
			debug.pstm(e, "Couldn't process classfile " + className + ". Please check if class file exists and works.");
		}
	}
	private List<Class<? extends AddonCentral>> cclasses = new ArrayList<>();
	private void runCentrals() {
		for(Class<? extends AddonCentral> c : cclasses) {
			String cname = c.getName();
			try {
				AddonCentral central = c.newInstance();
				a.central = central;
				runCentralClass(central);
			} catch (IllegalAccessException e) {
				debug.printl("The constructor for" + cname +  " is non-public or absent. To developer: change");
				debug.printl("...");
				debug.printl("private/protected/package " + cname + "() {" );
				debug.printl("...");
				debug.printl("}");
				debug.printl("...");
				debug.printl("into");
				debug.printl("...");
				debug.printl("public " + cname + "() {" );
				debug.printl("...");
				debug.printl("}");
				debug.printl("...");
				debug.pst(e);
			} catch(Exception e) {
				debug.printl("Couldn't create the main instance of the mod " + a.name);
				debug.pst(e);
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void tryAddCentral(Class<?> c) {
		boolean runnable = false;
		Class<?>[] interfaces = c.getInterfaces();
    	for(int i = 0; i < interfaces.length; i++) {
    		Class<?> in = interfaces[i];
    		if(AddonCentral.class.isAssignableFrom(in)) runnable = true; //if class implements AddonCentral, run it
    	}
    	if(runnable) //The given class is a mod central class //NOSONAR
			cclasses.add((Class<? extends AddonCentral>) c); 
    	a.hasClasses = true;
    }
    	
	
	void runCentralClass(AddonCentral c) {
		a.mmbmod = c.info();
		a.name = a.mmbmod.name;
	}
}
