/**
 * 
 */
package mmb.GEOM;

import org.joml.Vector2d;

/**
 * @author oskar
 *
 */
public class OrientedRect {
	public double lengthX = 0;
	public double lengthY = 0;
	public double centerX = 0;
	public double centerY = 0;
	public double angle = 0;
	/**
	 * @param a DR vertex output
	 * @param b DL vertex output
	 * @param c UL vertex output
	 * @param d UR vertex output
	 */
	public void vertices(Vector2d a, Vector2d b, Vector2d c, Vector2d d) {
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		
		a.x = centerX + cos*lengthX + sin*lengthY;
		a.y = centerY + cos*lengthY - sin*lengthX;
		
		b.x = centerX - cos*lengthX + sin*lengthY;
		b.y = centerY - cos*lengthY - sin*lengthX;
		
		c.x = centerX - cos*lengthX - sin*lengthY;
		c.y = centerY - cos*lengthY + sin*lengthX;
		
		d.x = centerX + cos*lengthX - sin*lengthY;
		d.y = centerY + cos*lengthY + sin*lengthX;
	}
}
