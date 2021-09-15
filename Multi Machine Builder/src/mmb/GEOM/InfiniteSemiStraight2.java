/**
 * 
 */
package mmb.GEOM;

import org.joml.Vector2d;

/**
 * @author oskar
 *
 */
public class InfiniteSemiStraight2 implements Constraint2 {
	public double beginX;
	public double beginY;
	public double nrmX;
	public double nrmY;
	/**
	 * The beginning vector
	 */
	public final Vector2d begin = new Vector2d();
	/**
	 * The normal vector. It should have a length of 1
	 */
	public final Vector2d normal = new Vector2d(1, 0);
	@Override
	public Vector2d closest(double inX, double inY, Vector2d out) {
		double vx = inX-beginX;
		double vy = inY-beginY;
		double d = (vx*nrmX)+(vy*nrmY);
		if(d < 0) d = 0;
		double resultX = beginX + (nrmX*d);
		double resultY = beginX + (nrmY*d);
		return out.set(resultX, resultY);
	}
}
