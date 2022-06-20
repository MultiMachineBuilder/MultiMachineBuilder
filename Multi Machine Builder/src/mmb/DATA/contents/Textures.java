/**
 * 
 */
package mmb.DATA.contents;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import mmb.MODS.loader.AddonLoader;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Textures {
	private static final Debugger debug = new Debugger("TEXTURES");
	private static final Map<String, BufferedImage> loadedTextures = new HashMap<>();
	/**
	 * 
	 * @param name texture name, without 'textures/' and with forward slashes only
	 * @param data texture data
	 * @return the loaded texture, or null if texture is unsupported or absent
	 * @throws IOException when texture fails to load
	 */
	public static BufferedImage load(String name, @Nullable InputStream data) throws IOException{
		if(data == null) return null;
		try {
			BufferedImage image = ImageIO.read(data);
			if(image == null) {
				debug.printl("Failed to load "+name);
				return null;
			}
			load(name, image);
			return image;
		} catch (IOException e) {
			debug.pstm(e, "Failed to load "+name);
			throw e;
		}
	}
	
	public static void load(String name, BufferedImage data) {
		String name2 = name.replace('\\', '/'); //replace slashes so users can use both '/' and '\'
		debug.printl("Loading texture: "+name2);
		loadedTextures.put(name2, data);
	}
	
	@Nonnull public static BufferedImage get(String name) {
		BufferedImage result = loadedTextures.get(name);
		if(result == null) {
			//Cache miss, load classpath
			try(@SuppressWarnings("null") @Nonnull InputStream in = AddonLoader.bcl.getResourceAsStream("textures/"+name)) {
				result = load(name, in);
				if(result == null) throw new NotFoundException("Could not find texture "+name);
			} catch (IOException e) {
				throw new NotFoundException("Failed to load texture "+name, e);
			}	
		}
		return result;
	}

}
