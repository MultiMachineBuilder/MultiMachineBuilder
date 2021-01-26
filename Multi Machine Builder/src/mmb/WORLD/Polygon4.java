/**
 * 
 */
package mmb.WORLD;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * @author oskar
 *
 */
public class Polygon4 {
	public Point a, b, c, d;

	public Polygon4(Point a, Point b, Point c, Point d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	private Polygon4(int xa, int ya, int xb, int yb) {
		a = new Point(xa, ya);
		b = new Point(xb, ya);
		c = new Point(xb, yb);
		d = new Point(xa, yb);
	}
	public Polygon4(Point a, Point b) {
		a = new Point(a.x, a.y);
		a = new Point(b.x, a.y);
		a = new Point(b.x, b.y);
		a = new Point(a.x, b.y);
	}
	public Polygon4(Rectangle r) {
		this(r.x, r.y, r.x+r.width, r.y+r.height);
	}
	
	public static Polygon4 rectangleCorners(int x1, int y1, int x2, int y2) {
		return new Polygon4(x1, y1, x2, y2);
	}
	public static Polygon4 rectangleDimensions(int x, int y, int w, int h) {
		return new Polygon4(x, y, x+w, y+h);
	}
}
