/**
 * 
 */
package mmb.GRAPHICS;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * @author oskar
 *
 */
public class ImageSlicer {
	public int dx, dy;
	public BufferedImage img;
	public ImageSlicer(int dx, int dy, BufferedImage img) {
		super();
		this.dx = dx;
		this.dy = dy;
		this.img = img;
	}
	
	/** Get a sprite at given position */
	public BufferedImage get(Point p) {
		return get(p.x, p.y);
	}
	
	/** Get a sprite at given position */
	public BufferedImage get(int x, int y) {
		return img.getSubimage(x*dx, y*dx, dx, dy);
	}
	
	/**Get a larger sprite, with rectangle selection */
	public BufferedImage get(int x, int y, int w, int h) {
		return img.getSubimage(x*dx, y*dx, w*dx, h*dx);
	}
	
	/**Get a larger sprite, with rectangle selection */
	public BufferedImage get(Point p, Dimension d) {
		return get(p.x, p.y, d.width, d.height);
	}
	
	/**Get a larger sprite, with rectangle selection */
	public BufferedImage get(Rectangle r) {
		return get(r.x, r.y, r.width, r.height);
	}
}
