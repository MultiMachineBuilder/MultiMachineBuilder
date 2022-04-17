/**
 * 
 */
package mmb.WORLD.visuals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.annotation.Nullable;

import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Rectangle;

import mmb.WORLD.gui.window.WorldFrame;

/**
 * @author oskar
 *
 */
public class VisRect implements Visual {
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
	public VisRect(double x1, double y1, double x2, double y2, @Nullable Color fill, @Nullable Color border) {
		super();
		line = Geometries.rectangle(x1, y1, x2, y2);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.fill = fill;
		this.border = border;
	}

}
