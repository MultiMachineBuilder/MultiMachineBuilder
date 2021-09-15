/**
 * 
 */
package mmb.WORLD.texture;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Icon;

import mmb.WORLD.block.BlockEntry;

/**
 * @author oskar
 *
 */
public interface BlockDrawer {
	/**
	 * @param ent block to be drawn, optional
	 * @param x left X coordinate on the frame
	 * @param y upper Y coordinate on the frame
	 * @param g graphics context
	 * @param sideSize how big is each side
	 */
	public void draw(@Nullable BlockEntry ent, int x, int y, Graphics g, int sideSize);
	/**
	 * @param ent block to be drawn, optional
	 * @param p upper left corner on the frame
	 * @param g graphics context
	 * @param sideSize
	 */
	public default void draw(@Nullable BlockEntry ent, Point p, Graphics g, int sideSize) {
		draw(ent, p.x, p.y, g, sideSize);
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
	
	/**
	 * Creates an  {@link Icon} representation of this {@code BlockDrawer}.
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
			public void paintIcon(Component c, @SuppressWarnings("null") @Nonnull Graphics g, int x, int y) {
				draw(null, x, y, g, 32);
			}
		};
	}

}
