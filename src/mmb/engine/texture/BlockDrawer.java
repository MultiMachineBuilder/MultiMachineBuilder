/**
 * 
 */
package mmb.engine.texture;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.Icon;

import mmb.NN;
import mmb.Nil;
import mmb.engine.block.BlockEntry;

/**
 * Renders block and item textures
 * @author oskar
 */
public interface BlockDrawer {
	/**
	 * Draws this texture
	 * @param ent block to be drawn, optional
	 * @param x left X coordinate on the frame
	 * @param y upper Y coordinate on the frame
	 * @param g graphics context
	 * @param w width
	 * @param h height
	 */
	public void draw(@Nil BlockEntry ent, int x, int y, Graphics g, int w, int h);
	/**
	 * Draws this texture
	 * @param ent block to be drawn, optional
	 * @param p upper left corner on the frame
	 * @param g graphics context
	 * @param w width
	 * @param h height
	 */
	public default void draw(@Nil BlockEntry ent, Point p, Graphics g, int w, int h) {
		draw(ent, p.x, p.y, g, w, h);
	}
	/**
	 * @param ent block to be drawn, optional
	 * @param x left X coordinate on the frame
	 * @param y upper Y coordinate on the frame
	 * @param g graphics context
	 * @param sideSize how big is each side
	 */
	public default void draw(@Nil BlockEntry ent, int x, int y, Graphics g, int sideSize) {
		draw(ent, x, y, g, sideSize, sideSize);
	}
	/**
	 * @param ent block to be drawn, optional
	 * @param p upper left corner on the frame
	 * @param g graphics context
	 * @param sideSize
	 */
	public default void draw(@Nil BlockEntry ent, Point p, Graphics g, int sideSize) {
		draw(ent, p.x, p.y, g, sideSize, sideSize);
	}
	/**
	 * @param img new BlockDrawer's desired image
	 * @return a BlockDrawer for given BufferedImage
	 */
	@NN public static BlockDrawer ofImage(BufferedImage img) {
		return new TextureDrawer(img);
	}
	/**
	 * 
	 * @param c new BlockDrawer's desired color
	 * @return a BlockDrawer of given color
	 */
	@NN public static BlockDrawer ofColor(Color c) {
		return new ColorDrawer(c);
	}
	/**
	 * Convert this drawer to preview icon
	 * @return an {@link Icon}, which represents a preview of this drawer
	 */
	@NN public Icon toIcon();
	/**
	 * @return a low level of detail color in sRGB
	 */
	public int LOD();
	
	/**
	 * Creates an {@link Icon} representation of this {@code BlockDrawer}.
	 * If this drawer is mutable, any changes in this block drawer are represented in the icon.
	 * @return an icon representation of this block drawer
	 */
	public default Icon iconRenderer() {
		return new Icon() {

			@Override
			public int getIconHeight() {
				return 32;
			}

			@Override
			public int getIconWidth() {
				return 32;
			}

			@Override
			public void paintIcon(@Nil Component c, @SuppressWarnings("null") Graphics g, int x, int y) {
				draw(null, x, y, g, 32);
			}
		};
	}
}
