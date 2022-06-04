/**
 * 
 */
package mmb.DATA.contents.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Textures {
	private static final Debugger debug = new Debugger("TEXTURES");
	private static final Map<String, BufferedImage> loadedTextures = new HashMap<>();
	public static final Map<String, BufferedImage> textures = Collections.unmodifiableMap(loadedTextures);
	/**
	 * 
	 * @param name texture name, without 'textures/' and with forward slashes only
	 * @param data texture data
	 */
	public static void load(String name, InputStream data){
		try {
			BufferedImage image = ImageIO.read(data);
			if(image == null) {
				debug.printl("Failed to load "+name);
				return;
			}
			load(name, image);
		} catch (IOException e) {
			debug.pstm(e, "Failed to load "+name);
		}
	}
	
	public static void load(String name, BufferedImage data) {
		String name2 = name.replace('\\', '/'); //replace slashes so users can use both '/' and '\'
		debug.printl("Loading texture: "+name2);
		loadedTextures.put(name2, data);
	}
	
	@Nonnull public static BufferedImage get(String name) {
		BufferedImage result = nget(name);
		if(result == null) throw new TextureNotFoundException("Could not find texture "+name);
		return result;
	}
	
	@Nullable public static BufferedImage nget(String name) {
		return loadedTextures.get(name);
	}

}
