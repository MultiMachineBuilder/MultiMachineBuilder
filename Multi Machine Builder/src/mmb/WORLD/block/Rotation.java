/**
 * 
 */
package mmb.WORLD.block;

import mmb.WORLD.Side;

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
		public Side U() {
			return Side.U;
		}

		@Override
		public Side D() {
			return Side.D;
		}

		@Override
		public Side L() {
			return Side.L;
		}

		@Override
		public Side R() {
			return Side.UR;
		}

		@Override
		public Side DL() {
			return Side.DL;
		}

		@Override
		public Side DR() {
			return Side.DR;
		}

		@Override
		public Side UL() {
			return Side.UL;
		}

		@Override
		public Side UR() {
			return Side.UR;
		}

		@Override
		public Rotation cw() {
			return E;
		}

		@Override
		public Rotation ccw() {
			return W;
		}
	},
	E{
		@Override
		public Side U() {
			return Side.L;
		}

		@Override
		public Side D() {
			return Side.R;
		}

		@Override
		public Side L() {
			return Side.D;
		}

		@Override
		public Side R() {
			return Side.U;
		}

		@Override
		public Side DL() {
			return Side.DR;
		}

		@Override
		public Side DR() {
			return Side.UR;
		}

		@Override
		public Side UL() {
			return Side.DL;
		}

		@Override
		public Side UR() {
			return Side.UL;
		}

		@Override
		public Rotation cw() {
			return S;
		}

		@Override
		public Rotation ccw() {
			return N;
		}
	},
	S{
		@Override
		public Side U() {
			return Side.D;
		}

		@Override
		public Side D() {
			return Side.U;
		}

		@Override
		public Side L() {
			return Side.R;
		}

		@Override
		public Side R() {
			return Side.L;
		}

		@Override
		public Side DL() {
			return Side.UR;
		}

		@Override
		public Side DR() {
			return Side.UL;
		}

		@Override
		public Side UL() {
			return Side.DR;
		}

		@Override
		public Side UR() {
			return Side.DL;
		}

		@Override
		public Rotation cw() {
			return W;
		}

		@Override
		public Rotation ccw() {
			return E;
		}
	},
	W{
		@Override
		public Side U() {
			return Side.R;
		}

		@Override
		public Side D() {
			return Side.L;
		}

		@Override
		public Side L() {
			return Side.U;
		}

		@Override
		public Side R() {
			return Side.D;
		}

		@Override
		public Side DL() {
			return Side.UL;
		}

		@Override
		public Side DR() {
			return Side.DL;
		}

		@Override
		public Side UL() {
			return Side.UR;
		}

		@Override
		public Side UR() {
			return Side.DR;
		}

		@Override
		public Rotation cw() {
			return N;
		}

		@Override
		public Rotation ccw() {
			return S;
		}
	};
	public abstract Side U();
	public abstract Side D();
	public abstract Side L();
	public abstract Side R();
	public abstract Side DL();
	public abstract Side DR();
	public abstract Side UL();
	public abstract Side UR();
	public abstract Rotation cw();
	public abstract Rotation ccw();
}
