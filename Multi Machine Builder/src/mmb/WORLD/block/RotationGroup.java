/**
 * 
 */
package mmb.WORLD.block;

/**
 * @author oskar
 * A rotation group describes how blocks are rotated with CTRL+LMB/RMB
 */
public enum RotationGroup {
	/**
	 * Rotation on 4 corners (NE, ES, SW, WN)
	 */
	CORNERS{
		@Override
		public Rotation cw(Rotation rot) {
			switch(rot) {
			case E:
			case NE:
				return Rotation.ES;
			case N:
			case WN:
				return Rotation.NE;
			case S:
			case ES:
				return Rotation.WS;
			case W:
			case WS:
				return Rotation.WN;
			default:
				return Rotation.NS;
			}
		}

		@Override
		public Rotation ccw(Rotation rot) {
			switch(rot) {
			case N:
			case NE:
				return Rotation.WN;
			case E:
			case ES:
				return Rotation.NE;
			case S:
			case WS:
				return Rotation.ES;
			case W:
			case WN:
				return Rotation.WS;
			default:
				return Rotation.WN;
			}
		}

		@Override
		public Rotation defaultOrientation() {
			return Rotation.NE;
		}
	},
	/**
	 * Edge-based rotation
	 */
	EDGES{
		@Override
		public Rotation cw(Rotation rot) {
			switch(rot) {
			case N:
			case NE:
				return Rotation.E;
			case E:
			case ES:
				return Rotation.S;
			case S:
			case WS:
				return Rotation.W;
			case W:
			case WN:
				return Rotation.S;
			default:
				return Rotation.N;
			}
		}

		@Override
		public Rotation ccw(Rotation rot) {
			switch(rot) {
			case E:
			case NE:
				return Rotation.N;
			case N:
			case WN:
				return Rotation.E;
			case S:
			case ES:
				return Rotation.S;
			case W:
			case WS:
				return Rotation.W;
			default:
				return Rotation.N;
			}
		}

		@Override
		public Rotation defaultOrientation() {
			return Rotation.N;
		}
	},
	/**
	 * Rotation in 8 directions
	 */
	OCTAGON{
		@Override
		public Rotation cw(Rotation rot) {
			switch(rot) {
			case E:
				return Rotation.ES;
			case ES:
				return Rotation.S;
			case S:
				return Rotation.WS;
			case WS:
				return Rotation.W;
			case W:
				return Rotation.WN;
			case WN:
				return Rotation.N;
			case N:
				return Rotation.NE;
			case NE:
				return Rotation.W;
			default:
				return Rotation.NE;
			}
		}

		@Override
		public Rotation ccw(Rotation rot) {
			switch(rot) {
			case E:
				return Rotation.NE;
			case ES:
				return Rotation.E;
			case S:
				return Rotation.ES;
			case WS:
				return Rotation.S;
			case W:
				return Rotation.WS;
			case WN:
				return Rotation.W;
			case N:
				return Rotation.WN;
			case NE:
				return Rotation.N;
			default:
				return Rotation.WN;
			}
		}

		@Override
		public Rotation defaultOrientation() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	/**
	 * Vertical and horizontal
	 */
	BAR{
		@Override
		public Rotation cw(Rotation rot) {
			switch(rot) {
			case NS:
				return Rotation.WE;
			default:
				return Rotation.NS;
			
			}
		}

		@Override
		public Rotation ccw(Rotation rot) {
			switch(rot) {
			case NS:
				return Rotation.WE;
			default:
				return Rotation.NS;
			
			}
		}

		@Override
		public Rotation defaultOrientation() {
			return Rotation.NS;
		}
	};
	abstract public Rotation cw(Rotation rot);
	abstract public Rotation ccw(Rotation rot);
	abstract public Rotation defaultOrientation();
}
