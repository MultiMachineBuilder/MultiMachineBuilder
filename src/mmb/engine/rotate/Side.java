/**
 * 
 */
package mmb.engine.rotate;

import java.awt.Point;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public enum Side {
	U(0.5, 0, 0, -1),
	UR(1, 0, 1, -1),
	R(1, 0.5, 1, 0),
	DR(1, 1, 1, 1),
	D(0.5, 1, 0, 1),
	DL(0, 1, -1, 1),
	L(0, 0.5, -1, 0),
	UL(0, 0, -1, -1);
	
	public static final byte r = 1, l = 2, d = 4, u = 8;

	/**
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	@Nonnull public Point offset(int x, int y) {
		return new Point(x+blockOffsetX, y+blockOffsetY);
	}
	/**
	 * @return opposite side
	 */
	@Nonnull public Side negate() {
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
	@Nonnull public Side ccw() {
		return Rotation.W.apply(this);
	}
	@Nonnull public Side cw() {
		return Rotation.E.apply(this);
	}
	/**
	 * @param pt
	 * @return
	 */
	@Nonnull public Point offset(Point pt) {
		return offset(pt.x, pt.y);
	}
	
	Side(double sox, double soy, int box, int boy){
		sideOffsetX = sox;
		blockOffsetX = box;
		sideOffsetY = soy;
		blockOffsetY = boy;
	}
	
	public final double sideOffsetX;
	public final double sideOffsetY;
	public final int blockOffsetX;
	public final int blockOffsetY;
}
