/**
 * 
 */
package mmb.content.ppipe;

import org.joml.Vector2d;

/**
 * @author oskar
 * A straight line path
 */
public class Path {
	public double beginX;
	public double beginY;
	public double endX;
	public double endY;
	public double length;
	/**
	 * @param progress
	 * @param out
	 */
	public void interpolate(double progress, Vector2d out) {
		double t = progress/length;
		out.x = (t*endX)+((1-t)*beginX);
		out.y = (t*endY)+((1-t)*beginY);
	}
}
