/**
 * 
 */
package mmb.WORLD.texture;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;
import javax.swing.Icon;

import mmb.WORLD.block.BlockEntry;

/**
 * @author oskar
 *
 */
public interface BlockDrawer {
	public void draw(BlockEntry ent, int x, int y, Graphics g);
	public default void draw(BlockEntry ent, Point p, Graphics g) {
		draw(ent, p.x, p.y, g);
	}
	/**
	 * 
	 * @param img new BlockDrawer's desired image
	 * @return a BlockDrawer for given BufferedImage
	 */
	@Nonnull public static BlockDrawer ofImage(BufferedImage img) {
		return new TextureDrawer(img);
	}
	/**
	 * 
	 * @param c new BlockDrawer's desired color
	 * @return a BlockDrawer of given color
	 */
	@Nonnull public static BlockDrawer ofColor(Color c) {
		return new ColorDrawer(c);
	}
	/**
	 * Convert this drawer to preview icon
	 * @return an {@link Icon}, which represents a preview of this drawer
	 */
	@Nonnull public Icon toIcon();

}
