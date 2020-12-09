/**
 * 
 */
package mmb.MENU.toolkit.interfaces;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * @author oskar
 *
 */
public interface Rectangular extends Sized, Positioned {
	/**
	 * Set rectangle as bounds
	 * @param rect
	 */
	default public void setRectangle(Rectangle rect) {
		setSize(rect.getSize());
		setPos(rect.getLocation());
	}
	default public Rectangle getBounds() {
		return new Rectangle(getPos(), getSize());
	}
	default public boolean contains(Point p) {
		return getBounds().contains(p);
	}
}
