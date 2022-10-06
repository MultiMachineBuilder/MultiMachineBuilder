/**
 * 
 */
package mmb.WORLD.visuals;

import java.awt.Color;
import java.awt.Graphics;

import javax.annotation.Nullable;

import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;

import mmb.MENU.world.window.WorldFrame;

/**
 * @author oskar
 *
 */
public final class VisPoint implements Visual {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VisPoint other = (VisPoint) obj;
		if (c == null) {
			if (other.c != null)
				return false;
		} else if (!c.equals(other.c))
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VisPoint [x=" + x + ", y=" + y + ", c=" + c + "]";
	}
}
