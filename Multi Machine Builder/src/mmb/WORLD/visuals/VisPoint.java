/**
 * 
 */
package mmb.WORLD.visuals;

import java.awt.Color;
import java.awt.Graphics;

import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;

import mmb.WORLD.gui.window.WorldFrame;

/**
 * @author oskar
 *
 */
public class VisPoint implements Visual {
	public final Point pt;
	public final double x, y;
	public final Color c;
	public VisPoint(double x, double y, Color c) {
		super();
		pt = Geometries.point(x, y);
		this.x = x;
		this.y = y;
		this.c = c;
	}

	@Override
	public Point border() {
		return pt;
	}

	@Override
	public void render(Graphics g, WorldFrame frame) {
		g.setColor(c);
		java.awt.Point p = frame.worldPositionOnScreen(x, y);
		g.drawLine(p.x, p.y, p.x, p.y);
	}
}
