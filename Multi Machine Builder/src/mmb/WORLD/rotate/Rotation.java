/**
 * 
 */
package mmb.WORLD.rotate;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public enum Rotation {
	/**
	 * The block faces north
	 */
	N {
		@Override
		public Side U() {return Side.U;}
		@Override
		public Side D() {return Side.D;}
		@Override
		public Side L() {return Side.L;}
		@Override
		public Side R() {return Side.R;}
		@Override
		public Side DL() {return Side.DL;}
		@Override
		public Side DR() {return Side.DR;}
		@Override
		public Side UL() {return Side.UL;}
		@Override
		public Side UR() {return Side.UR;}
		@Override
		public Rotation cw() {return E;}
		@Override
		public Rotation ccw() {return W;}
	},
	E{
		@Override
		public Side U() {return Side.R;}
		@Override
		public Side D() {return Side.L;}
		@Override
		public Side L() {return Side.U;}
		@Override
		public Side R() {return Side.D;}
		@Override
		public Side DL() {return Side.UL;}
		@Override
		public Side DR() {return Side.DL;}
		@Override
		public Side UL() {return Side.UR;}
		@Override
		public Side UR() {return Side.DR;}
		@Override
		public Rotation cw() {return S;}
		@Override
		public Rotation ccw() {return N;}
	},
	S{
		@Override
		public Side U() {return Side.D;}
		@Override
		public Side D() {return Side.U;}
		@Override
		public Side L() {return Side.R;}
		@Override
		public Side R() {return Side.L;}
		@Override
		public Side DL() {return Side.UR;}
		@Override
		public Side DR() {return Side.UL;}
		@Override
		public Side UL() {return Side.DR;}
		@Override
		public Side UR() {return Side.DL;}
		@Override
		public Rotation cw() {return W;}
		@Override
		public Rotation ccw() {return E;}
	},
	W{
		@Override
		public Side U() {return Side.L;}
		@Override
		public Side D() {return Side.R;}
		@Override
		public Side L() {return Side.D;}
		@Override
		public Side R() {return Side.U;}
		@Override
		public Side DL() {return Side.DR;}
		@Override
		public Side DR() {return Side.UR;}
		@Override
		public Side UL() {return Side.DL;}
		@Override
		public Side UR() {return Side.UL;}
		@Override
		public Rotation cw() {return N;}
		@Override
		public Rotation ccw() {return S;}
	};
	@Nonnull public abstract Side U();
	@Nonnull public abstract Side D();
	@Nonnull public abstract Side L();
	@Nonnull public abstract Side R();
	@Nonnull public abstract Side DL();
	@Nonnull public abstract Side DR();
	@Nonnull public abstract Side UL();
	@Nonnull public abstract Side UR();
	@Nonnull public abstract Rotation cw();
	@Nonnull public abstract Rotation ccw();
	
	/**
	 * Applies a rotation to the side
	 * @param s source side
	 * @return rotated side
	 */
	public @Nonnull Side apply(Side s) {
		switch(s) {
		case D:
			return D();
		case DL:
			return DL();
		case DR:
			return DR();
		case L:
			return L();
		case R:
			return R();
		case U:
			return U();
		case UL:
			return UL();
		case UR:
			return UR();
		default:
			throw new NullPointerException("side is null");
		}
	}
}
