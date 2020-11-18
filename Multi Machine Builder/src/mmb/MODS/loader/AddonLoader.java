/**
 * 
 */
package mmb.MODS.loader;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.function.*;
import java.util.jar.*;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import io.github.micwan88.helperclass4j.ByteClassLoader;
import mmb.DATA.contents.GameContents;
import mmb.DATA.contents.sound.Sounds;
import mmb.DATA.contents.texture.Textures;
import mmb.DATA.file.AdvancedFile;
import mmb.MODS.info.AddonCentral;
import mmb.MODS.info.AddonInfo;
import mmb.MODS.info.AddonState;
import mmb.MODS.info.ModMetadata;
import mmb.addon.EmptyAddonCentral;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class AddonLoader {
	private Debugger debug;
	public AddonInfo a;
	String originalPath;
	public Thread runningOn;
	//Variables created in stage 1
	
	private static final String PREFIX_TEXTURE = "textures/";
	private static final String PREFIX_SOUND = "sound/";
	
	void unaval() {
		debug.printl("Oh noes, " + a.name + " is inoperative, no fun with it.");	
	}
	
	
	/**
	 * Can use optional handler, for this purpose see load(Path, Consumer<AddonInfo>)
	 * Load addon from path
	 * @param p path of file
	 * @return
	 */
	public static AddonLoader load(AdvancedFile source) {
		return new AddonLoader(new AddonInfo(), source);
	}
	
	/**
	 * Uses optional handler, for function without handler see load(Path)
	 * Load addon from path
	 * @param p path of file
	 * @param handler optional handler
	 * @return
	 */
	public static AddonLoader load(AdvancedFile source, Consumer<AddonInfo> handler) {
		return new AddonLoader(new AddonInfo(), source, handler);
	}

	/**
	 * @param a
	 * @param originalPath
	 */
	protected AddonLoader(AddonInfo a, AdvancedFile source) {
		super();
		this.a = a;
		this.originalPath = source.name();
		a.file = source;
		runningOn = new Thread(() -> {init(); GameContents.addons.add(a);});
		ModLoader.runningLoaders.add(this);
		runningOn.start();
	}
	
	protected AddonLoader(AddonInfo a, AdvancedFile source, Consumer<AddonInfo> handler) {
		super();
		this.a = a;
		this.originalPath = source.name();
		a.file = source;
		runningOn = new Thread(() -> {init(); handler.accept(a); GameContents.addons.add(a);});
		ModLoader.runningLoaders.add(this);
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
		initor:
		{
			//Trying to open file
			try {
				debug.printl("Opening a modfile: " + a.name);
				
				if(!a.file.exists()) {
					debug.printl(a.file.name()+" does not exist.");
					a.state = AddonState.NOEXIST;
					return;
				}
				if(!a.isOnline()) a.name = AdvancedFile.dirName(a.file.name())[1];
				
				try {
					//Trying to load modfile
					debug.printl("Loading a modfile: " + a.name);
					unzip();
					whenWorking();
				}catch(Throwable e) {
					debug.printl("Couldn't open zip or jar file " + a.name);
					debug.pst(e);
					a.state = AddonState.BROKEN;
					break initor;
				}
				
			}catch(IllegalArgumentException e){
				debug.printl(e.getClass().getCanonicalName());
				debug.printl("Make sure that '' " + a.name + " '' is not mistyped.");
				debug.pst(e);
				a.state = AddonState.NOEXIST;
				break initor;
			}catch(Throwable e) {
				debug.printl(e.getClass().getCanonicalName());
				debug.printl("Couldn't read " + a.name + " for unknown reasons");
				debug.pst(e);
				a.state = AddonState.NOEXIST;
				break initor;
			}
		}
		
		
	}
	
	private void unzip() throws IOException {
		JarInputStream jis = a.file.asJAR();
		a:
		while(true) {
			JarEntry je = jis.getNextJarEntry();
			if(je == null) break a;
			debug.printl("entry: "+je.getName());
			byte[] bytes = IOUtils.toByteArray(jis);
			a.contents.add(je);
			a.files.put(je, bytes);
		}
	}
	
	/**
	 * If file works, continue loading
	 */
	private void whenWorking() {
		try {
			debug.printl("Injecting "+a.file.name());
			a.classes = new StreamClassLoader(a.file.asJAR());
		} catch (IOException e) {
			debug.pstm(e, "Unable to load mod " + a.name);
			a.state = AddonState.DEAD;
		}
		a.files.forEach((JarEntry entry, byte[] data) -> {
			try {
				processFile(entry, data);
			} catch (FileSystemException e) {
				debug.pstm(e, "Failed to process "+entry.getName());
			}
		}); 
	}
	
	private void processFile(JarEntry meta, byte[] data) throws FileSystemException {
		String name = meta.getName();
		String[] tmp = AdvancedFile.baseExtension(name);
		String ext = tmp[1];
		String base = tmp[0];
		debug.printl("Processing "+name);
		if(meta.isDirectory()) {
			debug.printl("Directory: "+name);
		}else {
			debug.printl("File: "+name);
			if(ext.endsWith("class")) {
				interpretClassFile(meta, base);
			}else if(ext.equals("mcmod")) {
				//a.mmbmod = loadMC(ent);
				debug.printl("Found Minecraft mod (found mcmod.info)");
			}else if(base.equals("MMB")) {
				//a.mmbmod = loadMMB(ent);
				debug.printl("Found MMB mod (found MMB.info)");
			}else if(name.startsWith(PREFIX_TEXTURE)){
				try {
					String shorter = name.substring(PREFIX_TEXTURE.length());
					BufferedImage img = ImageIO.read(new ByteArrayInputStream(a.files.get(meta)));
					Textures.load(shorter, img);
					debug.printl("Successfully added texture " + name);
				} catch (IOException e) {
					debug.printl("Couldn't access texture " + name);
					debug.pst(e);
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
	
	@SuppressWarnings("rawtypes")
	private void interpretClassFile(JarEntry ent, String base) throws FileSystemException{
		// This FileObject represents a class. Now, what class does it represent?
		String name = ent.getName();
		debug.printl("entry: "+name);
        String className = name.replace('/', '.');
        className = className.substring(0, className.length() - ".class".length());
        ByteClassLoader bcl = new ByteClassLoader(this.getClass().getClassLoader());
        try {
			
			Class c = bcl.loadClass(className, a.files.get(ent), false);
			String cname = c.getName();
			GameContents.loadedClasses.add(c);
        	Class[] interfaces = c.getInterfaces();
        	for(int i = 0; i < interfaces.length; i++) {
        		Class in = interfaces[i];
        		if(in.isInstance(new EmptyAddonCentral())) {
        			//Central class
        			try {
						AddonCentral central = (AddonCentral)c.newInstance();
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
					} catch(InstantiationException e) {
						debug.printl("Couldn't create the main instance of the mod " + a.name);
						debug.pst(e);
					} catch(NullPointerException e) {
						debug.printl(a.name + " has null pointer. Please make sure that central class return non-null");
						debug.pst(e);
					}
        		}else {
        			debug.printl("Other interface: " + in.getCanonicalName());
        		}
        	}
		} catch (ClassNotFoundException e1) {
			debug.pstm(e1, "Failed to find class "+name);
		} catch (IOException e1) {
			debug.pstm(e1, "Failed to open resource "+name);
		} catch (Exception e) {
			debug.printl("Couldn't process classfile " + className + ". Please check if class file exists and works.");
		}
        if(className.startsWith(".")) {
        	className = className.substring(1);
        }
	}
	

	/**
	 * @param ent
	 * @return
	 */
	@SuppressWarnings("static-method")
	private ModMetadata loadMMB(FileObject ent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param ent
	 * @return
	 */
	@SuppressWarnings("static-method")
	private ModMetadata loadMC(FileObject ent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	void runCentralClass(AddonCentral c) {
		a.mmbmod = c.info();
		a.name = a.mmbmod.name;
	}
	
	private static ModMetadata readKSPAVC(InputStream is) {
		ModMetadata md = new ModMetadata();
		return md;
		
	}
}
