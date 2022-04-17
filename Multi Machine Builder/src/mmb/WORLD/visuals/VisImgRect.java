/**
 * 
 */
package mmb.WORLD.visuals;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Rectangle;

import mmb.WORLD.gui.window.WorldFrame;

/**
 * @author oskar
 *
 */
public class VisImgRect implements Visual {
	public final double x1, y1, x2, y2;
	public final Rectangle line;
	public final Image img;
	@Override
	public Rectangle border() {
		return line;
	}

	@Override
	public void render(Graphics g, WorldFrame frame) {
		Point a = frame.worldPositionOnScreen(x1, y1);
		Point b = frame.worldPositionOnScreen(x2, y2);
		g.drawImage(img, a.x, a.y, b.x-a.x, b.y-a.y, null);
	}

	/**
	 * @param x1 first X coordinate
	 * @param y1 first Y coordinate
	 * @param x2 second X coordinate
	 * @param y2 second Y coordinate
	 * @param img image
	 */
	public VisImgRect(double x1, double y1, double x2, double y2, Image img) {
		super();
		line = Geometries.rectangle(x1, y1, x2, y2);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.img = img;
	}

}
