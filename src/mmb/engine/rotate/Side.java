/**
 * 
 */
package mmb.engine.rotate;

import java.awt.Point;

import mmb.NN;

/**
 * Describes a side or a corner of a block
 * @author oskar
 */
public enum Side {
	/** Top/front side*/
	U(0.5, 0, 0, -1),
	/** Upper/front - right corner*/
	UR(1, 0, 1, -1),
	/** Right side */
	R(1, 0.5, 1, 0),
	/** Lower/back - right corner */
	DR(1, 1, 1, 1),
	/** Lower/back side */
	D(0.5, 1, 0, 1),
	/** Lower/back - left corner */
	DL(0, 1, -1, 1),
	/** Left side */
	L(0, 0.5, -1, 0),
	/** Top/front - left corner*/
	UL(0, 0, -1, -1);
	
	public static final byte r = 1, l = 2, d = 4, u = 8;

	/**
	 * Offsets a point by a block offset
	 * @param pt point to offset
	 * @return a new offset point
	 */
	@NN public Point offset(Point pt) {
		return offset(pt.x, pt.y);
	}
	/**
	 * Offsets a point by a block offset
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return the offset point
	 */
	@NN public Point offset(int x, int y) {
		return new Point(x+blockOffsetX, y+blockOffsetY);
	}
	/**
	 * @return opposite side
	 */
	@NN public Side negate() {
		switch(this) {
		case D:
			return U;
		case DL:
			return UR;
		case DR:
			return UL;
		case L:
			return R;
		case R:
			return L;
		case U:
			return D;
		case UL:
			return DR;
		case UR:
			return DL;
		default:
			throw new InternalError("Somehow an unknown side appearred");
		}
	}
	/**
	 * Rotates this side counter-clockwise
	 * @return transformed side
	 */
	@NN public Side ccw() {
		return Rotation.W.apply(this);
	}
	/**
	 * Rotates this side clockwise
	 * @return transformed side
	 */
	@NN public Side cw() {
		return Rotation.E.apply(this);
	}
	
	Side(double sox, double soy, int box, int boy){
		sideOffsetX = sox;
		blockOffsetX = box;
		sideOffsetY = soy;
		blockOffsetY = boy;
	}
	
	/** Horizontal side offset (from center) */
	public final double sideOffsetX;
	/** Vertical side offset (from center) */
	public final double sideOffsetY;
	/** Horizontal block offset (from base coordinates) */
	public final int blockOffsetX;
	/** Vertical block offset (from base coordinates) */
	public final int blockOffsetY;

	@NN
	public Side transform(ChiralRotation chirotation) {
		return chirotation.apply(this);
	}
}
