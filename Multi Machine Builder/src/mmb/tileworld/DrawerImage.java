/**
 * 
 */
package mmb.tileworld;

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
	 * @see mmb.tileworld.BlockDrawer#draw(int, int, java.awt.Graphics)
	 */
	@Override
	public void draw(int x, int y, Graphics g) {
		g.drawImage(img, x, y, null);
	}

}
