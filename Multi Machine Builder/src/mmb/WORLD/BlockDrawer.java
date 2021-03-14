/**
 * 
 */
package mmb.WORLD;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public class BlockDrawer {
	public final BufferedImage img;

	private BlockDrawer(Color c) {
		img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		int rgb = c.getRGB();
		for(int i = 0; i < 32; i++) {
			for(int j = 0; j < 32; j++) {
				img.setRGB(j, i, rgb);
			}
		}
	}

	private BlockDrawer(BufferedImage img2) {
		img = img2;
	}
	public void draw(int x, int y, Graphics g) {
		g.drawImage(img, x, y, null);
	}
	public void draw(Point p, Graphics g) {
		draw(p.x, p.y, g);
	}
	
	private static Map<Color, BlockDrawer> cache = new HashMap<Color, BlockDrawer>();
	
	/**
	 * 
	 * @param img new BlockDrawer's desired image
	 * @return a BlockDrawer for given BufferedImage
	 */
	@Nonnull public static BlockDrawer ofImage(BufferedImage img) {
		Objects.requireNonNull(img, "img is null");
		return new BlockDrawer(img);
	}
	/**
	 * 
	 * @param c new BlockDrawer's desired color
	 * @return a BlockDrawer of given color
	 */
	@Nonnull public static BlockDrawer ofColor(Color c) {
		Objects.requireNonNull(c, "c is null");
		BlockDrawer result = cache.get(c);
		if(result == null) return new BlockDrawer(c);
		return result;
	}
}
