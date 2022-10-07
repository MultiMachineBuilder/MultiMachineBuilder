/**
 * 
 */
package mmb.gl;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.vavr.Tuple2;
import mmb.data.contents.Textures.Texture;
import mmb.debug.Debugger;

/**
 * @author oskar
 * A class to help make texture atlases
 * Based on <a href="https://blackpawn.com/texts/lightmaps/">https://blackpawn.com/texts/lightmaps/</a>
 * @param <T> the type of texture ID
 */
public class TextureAtlas{
	public static int MAX_SIZE = 8192;
	public TextureAtlasNode atlas;
	public BufferedImage image;
	private static final Debugger debug = new Debugger("TEXTURE ATLAS");
	/**
	 * Creates a texture atlas
	 * @param type
	 * @param w
	 * @param h
	 */
	public TextureAtlas(int type, int w, int h) {
		image = new BufferedImage(w, h, type);
		atlas = new TextureAtlasNode();
		atlas.rect.setBounds(0, 0, w, h);
	}
	@Nonnull public Texture insert(BufferedImage img, String id) {
		while(true) {
			TextureAtlasNode result = atlas.insert(img, id);
			if(result == null) {
				//Insertion failed
				if(image.getWidth() >= MAX_SIZE && image.getHeight() >= MAX_SIZE) throw new TextureError("Ran out of the texture size limit!");
				if(image.getWidth() > image.getHeight()) {
					int w = image.getWidth();
					int h = image.getHeight()*2;
					debug.printl("Size: "+w+"*"+h);
					
					//Scale up the image vertically
					BufferedImage img0 = new BufferedImage(w, h, image.getType());
					
					//Copy data
					Graphics g = img0.getGraphics();
					g.drawImage(image, 0, 0, null);
					g.dispose();
					
					//Replace images
					image = img0;
					TextureAtlasNode overall = new TextureAtlasNode();
					TextureAtlasNode upper = atlas;
					TextureAtlasNode lower = new TextureAtlasNode();
					overall.rect.width = upper.rect.width;
					overall.rect.height = upper.rect.height * 2;
					atlas = overall;
					lower.rect.width = upper.rect.width;
					lower.rect.height = upper.rect.height;
					lower.rect.y = upper.rect.height;
					overall.a = upper;
					overall.b = lower;
				}else {
					int w = image.getWidth()*2;
					int h = image.getHeight();
					debug.printl("Size: "+w+"*"+h);
					
					//Scale up the image horizontally
					BufferedImage img0 = new BufferedImage(w, h, image.getType());
					//Copy data
					Graphics g = img0.getGraphics();
					g.drawImage(image, 0, 0, null);
					g.dispose();
					
					//Replace images
					image = img0;
					TextureAtlasNode overall = new TextureAtlasNode();
					TextureAtlasNode left = atlas;
					TextureAtlasNode right = new TextureAtlasNode();
					overall.rect.width = left.rect.width * 2;
					overall.rect.height = left.rect.height;
					atlas = overall;
					right.rect.width = left.rect.width;
					right.rect.height = left.rect.height;
					right.rect.x = left.rect.width;
					overall.a = left;
					overall.b = right;
				}
				continue;
			}
			
			Graphics g = image.getGraphics();
			g.drawImage(img, result.rect.x, result.rect.y, null);
			g.dispose();
			
			return new Texture(
				result.rect.x, result.rect.y,
				result.rect.x+result.rect.width, result.rect.y+result.rect.height,
				img, id); //insertion successfull
		}
	}
	public void insert(Map<String, BufferedImage> textures) {
		for(Entry<String, BufferedImage> entry: textures.entrySet()) {
			BufferedImage img = entry.getValue();
			String id = entry.getKey();
			insert(img, id);
		}
	}
	
	/**
	 * @author oskar
	 * @param <T>
	 */
	public static class TextureAtlasNode {
		@Nullable public TextureAtlasNode a;
		@Nullable public TextureAtlasNode b;
		public final Rectangle rect = new Rectangle();
		public @Nullable String textureID;
		public TextureAtlasNode insert(BufferedImage img, String id) {
			TextureAtlasNode aa = a;
			TextureAtlasNode bb = b;
			if(aa != null && bb != null) {
				//not a leaf
				TextureAtlasNode atlas = aa.insert(img, id);
				if(atlas == null) 
					return bb.insert(img, id);
				return atlas;
			}
			//leaf
			if(textureID != null) return null; //if there's already a lightmap here, return
			
			int width = img.getWidth();
			int height = img.getHeight();
			
			//(if we're too small, return)
			if(width > rect.width || height > rect.height)
				return null;
			
			//if we're just right, accept
			if(width == rect.width && height == rect.height) {
				textureID = id;
				return this;
			}
			
			//otherwise, gotta split this node and create some kids
			TextureAtlasNode aaa = new TextureAtlasNode();
			TextureAtlasNode bbb = new TextureAtlasNode();
			a = aaa;
			b = bbb;
			
			//decide which way to split
			
			int dw = rect.width - width;
			int dh = rect.height - height;
			
			if(dw > dh) {
				//horizontal split
			    aaa.rect.setBounds(rect.x, rect.y, 
			            width, rect.height);
			    bbb.rect.setBounds(rect.x+width, rect.y, 
			            rect.width-width, rect.height);
			}else {
				//vertical split
			    aaa.rect.setBounds(rect.x, rect.y, 
			    		rect.width, height);
			    bbb.rect.setBounds(rect.x, rect.y+height, 
			    		rect.width, rect.height-height);
			}
			return aaa.insert(img, id);
		}
	}
}
