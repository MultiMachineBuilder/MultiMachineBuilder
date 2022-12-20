/**
 * 
 */
package mmb.engine.visuals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import com.github.davidmoten.rtree2.geometry.Circle;
import com.github.davidmoten.rtree2.geometry.Geometries;

import mmb.Nil;
import mmbbase.menu.world.window.WorldFrame;

/**
 * @author oskar
 *
 */
public final class VisCircle implements Visual {
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
	public VisCircle(double x1, double y1, double r, @Nil Color fill, @Nil Color border) {
		super();
		line = Geometries.circle(x1, y1, r);
		this.x = x1;
		this.y = y1;
		this.r = r;
		this.fill = fill;
		this.border = border;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((border == null) ? 0 : border.hashCode());
		result = prime * result + ((fill == null) ? 0 : fill.hashCode());
		long temp;
		temp = Double.doubleToLongBits(r);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(@Nil Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof VisCircle))
			return false;
		VisCircle other = (VisCircle) obj;
		if (border == null) {
			if (other.border != null)
				return false;
		} else if (!border.equals(other.border))
			return false;
		if (fill == null) {
			if (other.fill != null)
				return false;
		} else if (!fill.equals(other.fill))
			return false;
		if (Double.doubleToLongBits(r) != Double.doubleToLongBits(other.r))
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VisCircle [x=" + x + ", y=" + y + ", r=" + r + ", fill=" + fill + ", border=" + border + "]";
	}

}
