/**
 * 
 */
package mmb.WORLD.visuals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Line;

import mmb.WORLD.gui.window.WorldFrame;

/**
 * @author oskar
 *
 */
public class VisLine implements Visual {
	public final double x1, y1, x2, y2;
	public final Line line;
	public final Color c;
	@Override
	public Line border() {
		return line;
	}

	@Override
	public void render(Graphics g, WorldFrame frame) {
		Point a = frame.worldPositionOnScreen(x1, y1);
		Point b = frame.worldPositionOnScreen(x2, y2);
		g.setColor(c);
		g.drawLine(a.x, a.y, b.x, b.y);
	}

	/**
	 * @param x1 first X coordinate
	 * @param y1 first Y coordinate
	 * @param x2 second X coordinate
	 * @param y2 second Y coordinate
	 * @param c color
	 */
	public VisLine(double x1, double y1, double x2, double y2, Color c) {
		super();
		line = Geometries.line(x1, y1, x2, y2);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.c = c;
	}

}
