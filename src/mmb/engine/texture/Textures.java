/**
 * 
 */
package mmb.engine.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector4f;

import mmb.Main;
import mmb.NN;
import mmb.Nil;
import mmb.engine.NotFoundException;
import mmb.engine.debug.Debugger;
import mmb.engine.mods.ModLoader;
import monniasza.collects.Identifiable;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * The texture library uised by the MultiMachineBuilder
 * @author oskar
 */
public class Textures {
	private Textures() {}
	private static final Debugger debug = new Debugger("TEXTURES");
	
	//Raw data
	private static final SelfSet<String, Texture> loadedTextures0 = HashSelfSet.createNonnull(Texture.class);
	private static final TextureAtlas atlas = new TextureAtlas();
	
	/**
	 * Loads a texture from raw data (PNG, JPEG or GIF)
	 * @param name texture name, without 'textures/' and with forward slashes only
	 * @param data texture data
	 * @return the loaded texture, or null if texture is unsupported or absent
	 * @throws IOException when texture fails to load
	 */
	public static Texture load(String name, @Nil InputStream data) throws IOException{
		if(data == null) return null;
		try {
			BufferedImage image = ImageIO.read(data);
			if(image == null) {
				debug.printl("Failed to load "+name);
				return null;
			}
			return load(name, image);
		} catch (IOException e) {
			debug.stacktraceError(e, "Failed to load "+name);
			throw e;
		}
	}
	/**
	 * Loads a texture from a {@link BufferedImage}
	 * @param name texture name, without 'textures/' and with forward slashes only
	 * @param data texture data
	 * @return the loaded texture, or null if texture is unsupported or absent
	 */
	public static Texture load(String name, BufferedImage data) {
		String name2 = name.replace('\\', '/'); //replace slashes so users can use both '/' and '\'
		debug.printl("Loading texture: "+name2);
		Texture tex = atlas.insert(data, name2);
		loadedTextures0.add(tex);
		return tex;
	}
	
	/**
	 * Gets the texture as {@link BufferedImage}.
	 * To get as a {@link Texture}, use {@link #get1(String)} instead
	 * @param name texture name
	 * @return texture
	 * @throws NotFoundException if texture is not found
	 */
	@NN public static BufferedImage get(String name) {
		return get1(name).img;
	}
	/**
	 * Gets the texture as {@link Texture}.
	 * To get as a {@link Texture}, use {@link #get(String)} instead
	 * @param name texture name
	 * @return texture
	 * @throws NotFoundException if texture is not found
	 */
	@NN public static Texture get1(String name) {
		Texture result = loadedTextures0.get(name);
		if(result == null) {
			//Cache miss, load classpath
			try(InputStream in = ModLoader.bcl.getResourceAsStream("textures/"+name)) {
				if(in == null) throw new NotFoundException("Could not find texture "+name);
				result = load(name, in);
				if(result == null) throw new NotFoundException("Could not load texture "+name);
			} catch (IOException e) {
				throw new NotFoundException("Failed to load texture "+name, e);
			}	
		}
		return result;
	}

	/**
	 * A texture used by MultiMachineBuilder
	 * @author oskar
	 */
	public static class Texture implements Identifiable<String>{
		/** Left pixel X coordinate in texture atlas*/
		public final int minx;
		/** Upper pixel Y coordinate in texture atlas*/
		public final int miny;
		/** Right X coordinate in texture atlas */
		public final int maxx;
		/** Bottom Y coordinate in texture atlas */
		public final int maxy;
		/** The BufferedImage used in AWT-based graphics modules */
		@NN public final BufferedImage img;
		/** Texture path, with file extension, starting at the texture directory */
		@NN public final String id;
		private Vector4f lod;
		/**
		 * Creates a texture node
		 * @discouraged Should be used only in the game engine, not in mods
		 * @param minx left pixel X coordinate in texture atlas
		 * @param miny upper pixel Y coordinate in texture atlas
		 * @param maxx right X coordinate in texture atlas
		 * @param maxy bottom Y coordinate in texture atlas
		 * @param img the BufferedImage used in AWT-based graphics modules
		 * @param id texture path, with file extension, starting at the texture directory
		 */
		public Texture(int minx, int miny, int maxx, int maxy, BufferedImage img, String id) {
			this.minx = minx;
			this.miny = miny;
			this.maxx = maxx;
			this.maxy = maxy;
			this.img = img;
			this.id = id;
		}
		/**
		 * Get the atlas coordinates using texture coordinates
		 * @param src texture coordinates
		 * @param dest write atlas coordinates here
		 * @return dest
		 */
		@NN public Vector2f uvconvert(Vector2fc src, Vector2f dest) {
			return uvconvert(src.x(), src.y(), dest);
		}
		/**
		 * Get the atlas coordinates using texture coordinates
		 * @param x X texture coordinate
		 * @param y Y texture coordinate
		 * @param dest write atlas coordinates here
		 * @return dest
		 */
		@NN public Vector2f uvconvert(float x, float y, Vector2f dest) {
			dest.x = ((maxx-minx)*x + minx + 0.0f)/atlas.image.getWidth();
			dest.y = ((maxy-miny)*y + miny + 0.0f)/atlas.image.getHeight();
			return dest;
		}
		@Override
		@NN public String id() {
			return id;
		}
		/** @return level of detail color */
		public Vector4f LOD(Vector4f vec) {
			if(lod == null) lod = LODs.calcLOD(img, new Vector4f());
			return lod.get(vec);
		}
	}
	
	//OpenGL stuff
	private static boolean isInited;
	/** Test method used to test atlasing capabilities*/
	public static void displayAtlas() {
		if(isInited) return;
		isInited = true;
		
		//Image to byte buffer conversion
		BufferedImage image = atlas.image;
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        final int BYTES_PER_PIXEL = 4; //4 for RGBA, 3 for RGB
        ByteBuffer buffer = ByteBuffer.allocateDirect(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL);
        
        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));    // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));     // Green component
                buffer.put((byte) (pixel & 0xFF));	          // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }
        buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS
        atlasData = buffer;
	}	
	private static ByteBuffer atlasData;
	/** @return raw texture atlas data. Used for OpenGL renderer */
	public static ByteBuffer getAtlas() {
		if(atlasData == null) throw new IllegalStateException("Atlas data is not yet created");
		return atlasData;
	}
	/**
	 * Gets the texture atlas size
	 * @param dest write atlas sze here
	 * @return dest
	 */
	public static Vector2i atlasSize(Vector2i dest) {
		dest.x = atlas.image.getWidth();
		dest.y = atlas.image.getHeight();
		return dest;
	}
}
