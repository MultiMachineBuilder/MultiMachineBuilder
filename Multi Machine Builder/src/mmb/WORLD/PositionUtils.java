/**
 * 
 */
package mmb.WORLD;

import java.awt.Point;

/**
 * @author oskar
 *
 */
public class PositionUtils {

	public static Point getPosAtSide(Point p, Side s) {
		int x = p.x, y = p.y;
		switch(s) {
		case D:
			return new Point(x, y-1);
		case DL:
			return new Point(x-1, y+1);
		case DR:
			return new Point(x+1, y+1);
		case L:
			return new Point(x-1, y);
		case R:
			return new Point(x+1, y);
		case U:
			return new Point(x, y+1);
		case UL:
			return new Point(x-1, y-1);
		case UR:
			return new Point(x+1, y-1);
		default:
			return p;
		}
	}

}
