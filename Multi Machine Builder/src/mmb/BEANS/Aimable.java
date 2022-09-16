/**
 * 
 */
package mmb.BEANS;

import java.awt.Point;

/**
 * A block, whose target can be set
 * @author oskar
 */
public interface Aimable {
	/** @return target X coordinate */
	public int aimX();
	/** @return target Y coordinate */
	public int aimY();
	/** @return target position */
	public default Point aim() {
		return new Point(aimX(), aimY());
	}
	/** Set the X target coordinate
	 * @param x X target coordinate
	 */
	public void aimX(int x);
	/** Set the Y target coordinate
	 * @param y Y target coordinate
	 */
	public void aimY(int y);
}
