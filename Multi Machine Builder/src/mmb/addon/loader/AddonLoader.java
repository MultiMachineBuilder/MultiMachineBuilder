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

import com.google.gson.*;

import mmb.addon.EmptyAddonCentral;
import mmb.addon.data.AddonInfo;
import mmb.addon.data.AddonState;
import mmb.addon.data.ModMetadata;
import mmb.addon.file.StreamUtil;
import mmb.addon.module.AddonCentral;
import mmb.data.contents.GameContents;
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
	
	
	/**
	 * Determine if file can be loaded
	 */
	void load0() {
		String[] x = originalPath.split("//");
		String y = x[x.length - 1];
		String[] z = y.split("\\\\");
		String debugName = z[z.length - 1];
		debug = new Debugger("MOD - "+ debugName);
		a.name = originalPath;
		a.path = originalPath;
		initor:
		{
			//Trying to open file
			try {
				debug.printl("Opening a modfile: " + a.name);
				if(originalPath.startsWith("http")) {
					a.file = StreamUtil.stream2file(new URL(originalPath).openStream());
					a.online = true;
				}else {
					debug.printl("Offline file");
					a.file = new File(originalPath);
				}
				
				try {
					//Trying to load modfile
					debug.printl("Loading a modfile: " + a.name);
					a.jfile = new JarFile(a.file);
					load1();
				}catch(FileNotFoundException e) {
					debug.printl("File " + a.name + " is unavaliable.");
					debug.pst(e);
					a.state = AddonState.NOEXIST;
					break initor;
				}catch(Throwable e) {
					debug.printl("Couldn't open zip or jar file " + a.name);
					debug.pst(e);
					a.state = AddonState.BROKEN;
					break initor;
				}
			}catch(Throwable e) {
				debug.printl("File " + a.name + " is unavaliable.");
				debug.pst(e);
				a.state = AddonState.NOEXIST;
				break initor;
			}
		}
	}
	
	void unaval() {
		debug.printl("Oh noes, " + a.name + " is inoperative, no fun with it.");	
	}
	/**
	 * If file works, continue loading
	 */
	void load1() {
		try {
			ClassPathHacker.addFile(originalPath);
		} catch (IOException e) {
			debug.printl("Unable to load mod " + a.name);
			a.state = AddonState.DEAD;
			debug.pst(e);
		}
		
		Enumeration<JarEntry> contents = a.jfile.entries();
		
		while(contents.hasMoreElements()) {
			loadFile(contents.nextElement());
		}
		
				
	}
	
	void loadFile(JarEntry ent) {
		String name = ent.getName();
		boolean directory = ent.isDirectory();
		if(directory) {
			debug.printl("Directory: "+name);
		}else {
			debug.printl("File: "+name);
			if(name.endsWith(".class")) {
				// This ZipEntry represents a class. Now, what class does it represent?
		        String className = ent.getName().replace('/', '.');
		        className = className.substring(0, className.length() - ".class".length());
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
		        
			}else if(name.contains("mcmod")) {
				a.mmbmod = loadMC(ent);
				debug.printl("Found Minecraft mod (found mcmod.info)");
			}else if(name.contains("MMB")) {
				a.mmbmod = loadMMB(ent);
				debug.printl("Found MMB mod (found MMB.info)");
			}else if(name.endsWith(".png") || name.endsWith(".jpg")){
				try {
					BufferedImage img = ImageIO.read(a.jfile.getInputStream(ent));
					GameContents.textures.putIfAbsent(name, img);
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

	/**
	 * @param ent
	 * @return
	 */
	private ModMetadata loadMMB(JarEntry ent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param ent
	 * @return
	 */
	private ModMetadata loadMC(JarEntry ent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Can use optional handler, for this purpose see load(Path, Consumer<AddonInfo>)
	 * Load addon from path
	 * @param p path of file
	 * @return
	 */
	public static AddonLoader load(String p) {
		return new AddonLoader(new AddonInfo(), p);
	}
	
	/**
	 * Uses optional handler, for function without handler see load(Path)
	 * Load addon from path
	 * @param p path of file
	 * @param handler optional handler
	 * @return
	 */
	public static AddonLoader load(String p, Consumer<AddonInfo> handler) {
		return new AddonLoader(new AddonInfo(), p, handler);
	}

	/**
	 * @param a
	 * @param originalPath
	 */
	protected AddonLoader(AddonInfo a, String originalPath) {
		super();
		this.a = a;
		this.originalPath = originalPath;
		runningOn = new Thread(() -> {load0(); GameContents.addons.add(a);});
		ModLoader.runningLoaders.add(this);
		runningOn.start();
	}
	
	protected AddonLoader(AddonInfo a, String originalPath, Consumer<AddonInfo> handler) {
		super();
		this.a = a;
		this.originalPath = originalPath;
		runningOn = new Thread(() -> {load0(); handler.accept(a); GameContents.addons.add(a);});
		ModLoader.runningLoaders.add(this);
		runningOn.start();
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
