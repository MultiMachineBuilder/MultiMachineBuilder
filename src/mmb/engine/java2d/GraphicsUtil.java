/**
 * 
 */
package mmbeng.java2d;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author oskar
 *
 */
public class GraphicsUtil {
	private GraphicsUtil() {}
	
	public static void crossedBox(int x, int y, int w, int h, Color c, Graphics g) {
		g.setColor(c);
		g.drawRect(x, y, w, h);
		g.drawLine(x, y, x+w, y+h);
		g.drawLine(x+w, y, x, y+h);
	}
	public static void filledCrossedBox(int x, int y, int w, int h, Color fill, Color line, Graphics g) {
		g.setColor(fill);
		g.fillRect(x, y, w, h);
		crossedBox(x, y, w, h, line, g);
	}
}
