/**
 * 
 */
package mmb.WORLD.visuals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.annotation.Nullable;

import com.github.davidmoten.rtree.geometry.Circle;
import com.github.davidmoten.rtree.geometry.Geometries;
import mmb.WORLD.gui.window.WorldFrame;

/**
 * @author oskar
 *
 */
public class VisCircle implements Visual {
	public final double x, y, r;
	public final Circle line;
	public final Color fill;
	public final Color border;
	@Override
	public Circle border() {
		return line;
	}

	@Override
	public void render(Graphics g, WorldFrame frame) {
		Point a = frame.worldPositionOnScreen(x-r, y-r);
		Point b = frame.worldPositionOnScreen(x+r, y+r);
		if(fill != null) {
			g.setColor(fill);
			g.fillOval(a.x, a.y, b.x-a.x, b.y-a.y);
		}
		if(border != null) {
			g.setColor(border);
			g.drawOval(a.x, a.y, b.x-a.x, b.y-a.y);
		}
	}

	/**
	 * @param x1 first X coordinate
	 * @param y1 first Y coordinate
	 * @param r radius
	 * @param fill fill color, set to null to disable fill
	 * @param border border color, set to null to disable border
	 */
	public VisCircle(double x1, double y1, double r, @Nullable Color fill, @Nullable Color border) {
		super();
		line = Geometries.circle(x1, y1, r);
		this.x = x1;
		this.y = y1;
		this.r = r;
		this.fill = fill;
		this.border = border;
	}

}
