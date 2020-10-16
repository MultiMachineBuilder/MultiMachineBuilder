/**
 * 
 */
package mmb.addon.loader;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.jar.*;

import javax.imageio.ImageIO;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import com.google.gson.*;

import mmb.addon.EmptyAddonCentral;
import mmb.addon.data.AddonInfo;
import mmb.addon.data.AddonState;
import mmb.addon.data.ModMetadata;
import mmb.addon.file.StreamUtil;
import mmb.addon.loader.ModLoader.FilePair;
import mmb.addon.module.AddonCentral;
import mmb.debug.Debugger;
import mmb.files.FileGetter;
import mmb.files.data.contents.GameContents;

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
	
	
	void unaval() {
		debug.printl("Oh noes, " + a.name + " is inoperative, no fun with it.");	
	}
	
	
	/**
	 * Can use optional handler, for this purpose see load(Path, Consumer<AddonInfo>)
	 * Load addon from path
	 * @param p path of file
	 * @return
	 */
	public static AddonLoader load(FileObject source) {
		return new AddonLoader(new AddonInfo(), source);
	}
	
	/**
	 * Uses optional handler, for function without handler see load(Path)
	 * Load addon from path
	 * @param p path of file
	 * @param handler optional handler
	 * @return
	 */
	public static AddonLoader load(FileObject source, Consumer<AddonInfo> handler) {
		return new AddonLoader(new AddonInfo(), source, handler);
	}

	/**
	 * @param a
	 * @param originalPath
	 */
	protected AddonLoader(AddonInfo a, FileObject source) {
		super();
		this.a = a;
		this.originalPath = source.getPublicURIString();
		a.file = source;
		runningOn = new Thread(() -> {init(); GameContents.addons.add(a);});
		ModLoader.runningLoaders.add(this);
		runningOn.start();
	}
	
	protected AddonLoader(AddonInfo a, FileObject source, Consumer<AddonInfo> handler) {
		super();
		this.a = a;
		this.originalPath = source.getPublicURIString();
		a.file = source;
		runningOn = new Thread(() -> {init(); handler.accept(a); GameContents.addons.add(a);});
		ModLoader.runningLoaders.add(this);
		runningOn.start();
	}
	
	/**
	 * Determine if file can be loaded
	 */
	private void init() {
		String debugName = a.file.getName().getBaseName();
		debug = new Debugger("MOD - "+ debugName);
		a.name = originalPath;
		a.path = originalPath;
		initor:
		{
			//Trying to open file
			try {
				debug.printl("Opening a modfile: " + a.name);

				if(!a.file.exists()) {
					debug.printl(a.file.getName().getPath()+" does not exist.");
					a.state = AddonState.NOEXIST;
					return;
				}
				if(!a.isOnline()) a.name = a.file.getName().getBaseName();
				
				try {
					//Trying to load modfile
					debug.printl("Loading a modfile: " + a.name);
					a.files = FileGetter.getAll(FileGetter.openZIP(a.file));
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
			}catch(IOException e) {
				debug.printl("File " + a.name + " is unavaliable.");
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
	
	/**
	 * If file works, continue loading
	 */
	private void whenWorking() {
		try {
			debug.printl("Injecting "+a.file.getName().getBaseName());
			ClassPathHacker.addFile(a.file);
		} catch (IOException e) {
			debug.printl("Unable to load mod " + a.name);
			a.state = AddonState.DEAD;
			debug.pst(e);
		}
		a.files.forEach(file -> {
			try {
				processFile(file);
			} catch (FileSystemException e) {
				debug.pstm(e, "Unable to process "+file.getName().getPath());
			}
		});
	}
	
	private void processFile(FileObject ent) throws FileSystemException {
		FileName name = ent.getName();
		String ext = name.getExtension();
		String base = name.getBaseName();
		debug.printl("Processing "+name.getFriendlyURI());
		if(ent.isFolder()) {
			debug.printl("Directory: "+base);
		}else {
			debug.printl("File: "+base);
			if(ext.endsWith("class")) {
				interpretClassFile(ent, base);
			}else if(ext.equals("mcmod")) {
				a.mmbmod = loadMC(ent);
				debug.printl("Found Minecraft mod (found mcmod.info)");
			}else if(base.equals("MMB")) {
				a.mmbmod = loadMMB(ent);
				debug.printl("Found MMB mod (found MMB.info)");
			}else if(ext.equals(".png") || ext.equals(".jpg")){
				try {
					BufferedImage img = ImageIO.read(ent.getContent().getInputStream());
					GameContents.textures.putIfAbsent(name.getPath(), img);
					debug.printl("Successfully added texture " + name);
				} catch (IOException e) {
					debug.printl("Couldn't access texture " + name);
					debug.pst(e);
				} catch (Throwable e) {
					debug.printl("Couldn't open texture " + name);
					debug.pst(e);
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void interpretClassFile(FileObject ent, String base) throws FileSystemException{
		// This FileObject represents a class. Now, what class does it represent?
        String className = ent.getName().getPathDecoded().replace('/', '.');
        className = className.substring(0, className.length() - ".class".length());
        if(className.startsWith(".")) {
        	className = className.substring(1);
        }
		try {
			Class c = Class.forName(className);
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
		} catch (ClassNotFoundException e) {
			debug.printl("Couldn't find class " + className + ". Please check if class file exists and works.");
			debug.pst(e);
		} catch (Exception e) {
			debug.printl("Couldn't process classfile " + className + ". Please check if class file exists and works.");
		}
	}
	

	/**
	 * @param ent
	 * @return
	 */
	private ModMetadata loadMMB(FileObject ent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param ent
	 * @return
	 */
	private ModMetadata loadMC(FileObject ent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	void runCentralClass(AddonCentral c) {
		a.mmbmod = c.info();
		a.name = a.mmbmod.name;
	}
	
	private ModMetadata readKSPAVC(InputStream is) {
		ModMetadata md = new ModMetadata();
		return md;
		
	}
}
