/**
 * 
 */
package mmb.WORLD.tileworld;

import java.awt.Graphics;
import java.awt.Point;

/**
 * @author oskar
 *
 */
public interface BlockDrawer {
	void draw(int x, int y, Graphics g);
	default void draw(Point p, Graphics g) {
		draw(p.x, p.y, g);
	}
	@Deprecated default public void draw(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, Graphics g) {}
	@Deprecated default public void draw(Point a, Point b, Point c, Point d, Graphics g) {
		draw(a.x, a.y, b.x, b.y, c.x, c.y, d.x, d.y, g);
	}
}
