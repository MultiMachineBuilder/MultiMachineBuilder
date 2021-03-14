/**
 * 
 */
package mmb.WORLD;

import java.awt.Point;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public enum Side {
	U {
		@Override
		public Point offset(int x, int y) {
			return new Point(x, y-1);
		}
	},
	UR {
		@Override
		public Point offset(int x, int y) {
			return new Point(x+1, y-1);
		}
	},
	R {
		@Override
		public Point offset(int x, int y) {
			return new Point(x+1, y);
		}
	},
	DR {
		@Override
		public Point offset(int x, int y) {
			return new Point(x+1, y+1);
		}
	},
	D {
		@Override
		public Point offset(int x, int y) {
			return new Point(x, y+1);
		}
	},
	DL {
		@Override
		public Point offset(int x, int y) {
			return new Point(x-1, y+1);
		}
	},
	L {
		@Override
		public Point offset(int x, int y) {
			return new Point(x-1, y);
		}
	},
	UL {
		@Override
		public Point offset(int x, int y) {
			return new Point(x-1, y-1);
		}
	};
	
	public static final byte r = 1, l = 2, d = 4, u = 8;

	/**
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	@Nonnull abstract public Point offset(int x, int y);
}
