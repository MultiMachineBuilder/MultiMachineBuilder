/**
 * 
 */
package mmb.WORLD.tileworld;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * @author oskar
 *
 */
public class DrawerImage implements BlockDrawer {
	public BufferedImage img;
	/**
	 * 
	 */
	public DrawerImage(BufferedImage img) {
		this.img = img;
	}

	/* (non-Javadoc)
	 * @see mmb.WORLD.tileworld.BlockDrawer#draw(int, int, java.awt.Graphics)
	 */
	@Override
	public void draw(int x, int y, Graphics g) {
		g.drawImage(img, x, y, null);
	}

	@Override
	public void draw(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, Graphics g) {
		//g.drawIm
	}

}
