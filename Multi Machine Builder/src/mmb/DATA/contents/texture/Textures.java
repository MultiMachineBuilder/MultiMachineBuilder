/**
 * 
 */
package mmb.DATA.contents.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Textures {
	private static final Debugger debug = new Debugger("TEXTURES");
	private static final Map<String, BufferedImage> loadedTextures = new Hashtable<String, BufferedImage>();
	public static final String prefix = "textures/";
	public static final File texturesDir = new File(prefix);
	public static final String texturesPath = texturesDir.getAbsolutePath();
	/**
	 * 
	 * @param name texture name, without 'textures/' and with forward slashes only
	 * @param data texture data
	 */
	public static void load(String name, InputStream data){
		try {
			BufferedImage image = ImageIO.read(data);
			load(name, image);
		} catch (IOException e) {
			debug.pstm(e, "Failed to load "+name);
		}
	}
	
	public static void load(String name, BufferedImage data) {
		String name2 = name.replace('\\', '/'); //replace slashes so users can use both '/' and '\'
		debug.printl("Loading texture "+name2);
		loadedTextures.put(name2, data);
	}
	
	public static BufferedImage get(String name) {
		String name2 = name.replace('\\', '/'); //replace slashes so users can use both '/' and '\'
		return loadedTextures.get(name2);
	}

}
