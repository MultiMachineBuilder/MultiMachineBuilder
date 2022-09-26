/**
 * 
 */
package mmb.GEOM;

import org.joml.Vector2d;

/**
 * @author oskar
 * This interface defines constraints, which constrain an object
 */
public interface Constraint2 {
	/**
	 * Returns a closest point on this constraint
	 * @param inX X coordinate of the point to check
	 * @param inY Y coordinate of the point to check
	 * @param out the output to write to
	 * @return a given vector with nearest point on the constraint
	 */
	public Vector2d closest(double inX, double inY, Vector2d out);
}
