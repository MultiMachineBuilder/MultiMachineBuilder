/**
 * 
 */
package mmb.beans;

import java.awt.Point;

/**
 * An object which has a position.
 * Used with block entities
 * @author oskar
 */
public interface Positioned {
	/** @return X coordinate */
	public int posX();
	/** @return Y coordinate */
	public int posY();
	/** @return position */
	public default Point pos() {
		return new Point(posX(), posY());
	}
	/** Set the X coordinate
	 * @param x X coordinate
	 */
	public void setX(int x);
	/** Set the Y coordinate
	 * @param y Y coordinate
	 */
	public void setY(int y);
}
