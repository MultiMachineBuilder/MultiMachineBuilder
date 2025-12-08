/**
 * 
 */
package mmb.engine.visuals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import com.github.davidmoten.rtree2.geometry.Geometries;
import com.github.davidmoten.rtree2.geometry.Rectangle;

import mmb.annotations.Nil;
import mmb.menu.world.window.WorldFrame;

/**
 * @author oskar
 *
 */
public final class VisRect implements Visual {
	public final double x1, y1, x2, y2;
	public final Rectangle line;
	public final Color fill;
	public final Color border;
	@Override
	public Rectangle border() {
		return line;
	}

	@Override
	public void render(Graphics g, WorldFrame frame) {
		Point a = frame.worldPositionOnScreen(x1, y1);
		Point b = frame.worldPositionOnScreen(x2, y2);
		if(fill != null) {
			g.setColor(fill);
			g.fillRect(a.x, a.y, b.x-a.x, b.y-a.y);
		}
		if(border != null) {
			g.setColor(border);
			g.drawRect(a.x, a.y, b.x-a.x, b.y-a.y);
		}
	}

	/**
	 * @param x1 first X coordinate
	 * @param y1 first Y coordinate
	 * @param x2 second X coordinate
	 * @param y2 second Y coordinate
	 * @param fill fill color, set to null to disable fill
	 * @param border border color, set to null to disable border
	 */
	public VisRect(double x1, double y1, double x2, double y2, @Nil Color fill, @Nil Color border) {
		super();
		line = Geometries.rectangle(x1, y1, x2, y2);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
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
		temp = Double.doubleToLongBits(x1);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(x2);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y1);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y2);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(@Nil Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VisRect other = (VisRect) obj;
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
		if (Double.doubleToLongBits(x1) != Double.doubleToLongBits(other.x1))
			return false;
		if (Double.doubleToLongBits(x2) != Double.doubleToLongBits(other.x2))
			return false;
		if (Double.doubleToLongBits(y1) != Double.doubleToLongBits(other.y1))
			return false;
		if (Double.doubleToLongBits(y2) != Double.doubleToLongBits(other.y2))
			return false;
		return true;
	}

}
