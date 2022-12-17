/**
 * 
 */
package mmb.engine.visuals;

import java.awt.Graphics;
import java.awt.Point;

import javax.annotation.Nullable;

import com.github.davidmoten.rtree2.geometry.Geometries;
import com.github.davidmoten.rtree2.geometry.Rectangle;

import mmb.engine.texture.BlockDrawer;
import mmbbase.menu.world.window.WorldFrame;

/**
 * @author oskar
 *
 */
public final class VisImgRect implements Visual {
	public final double x1, y1, x2, y2;
	public final Rectangle line;
	public final BlockDrawer img;
	@Override
	public Rectangle border() {
		return line;
	}

	@Override
	public void render(Graphics g, WorldFrame frame) {
		Point a = frame.worldPositionOnScreen(x1, y1);
		Point b = frame.worldPositionOnScreen(x2, y2);
		img.draw(null, a.x, a.y, g, b.x-a.x, b.y-a.y);
	}

	/**
	 * @param x1 first X coordinate
	 * @param y1 first Y coordinate
	 * @param x2 second X coordinate
	 * @param y2 second Y coordinate
	 * @param img image
	 */
	public VisImgRect(double x1, double y1, double x2, double y2, BlockDrawer img) {
		super();
		line = Geometries.rectangle(x1, y1, x2, y2);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.img = img;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((img == null) ? 0 : img.hashCode());
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
	public boolean equals(@Nullable Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VisImgRect other = (VisImgRect) obj;
		if (img == null) {
			if (other.img != null)
				return false;
		} else if (!img.equals(other.img))
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

	@Override
	public String toString() {
		return "VisImgRect [x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + ", img=" + img + "]";
	}

}
