/**
 * 
 */
package mmb.tileworld;

import java.awt.Graphics;
import java.awt.Point;

/**
 * @author oskar
 *
 */
public interface BlockDrawer {
	public void draw(int x, int y, Graphics g);
	default public void draw(Point p, Graphics g) {
		draw(p.x, p.y, g);
	}
}
