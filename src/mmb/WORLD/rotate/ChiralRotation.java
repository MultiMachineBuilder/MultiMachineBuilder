/**
 * 
 */
package mmb.WORLD.rotate;

import javax.annotation.Nonnull;

import mmb.DATA.contents.Textures.Texture;
import mmb.GRAPHICS.gl.RenderCtx;

/**
 * @author oskar
 *
 */
public enum ChiralRotation {
	Nl(Rotation.N, Chirality.L) {
		@Override public Side U() {return Side.U;}
		@Override public Side D() {return Side.D;}
		@Override public Side L() {return Side.R;}
		@Override public Side R() {return Side.L;}
		@Override public Side DL() {return Side.DR;}
		@Override public Side DR() {return Side.DL;}
		@Override public Side UL() {return Side.UR;}
		@Override public Side UR() {return Side.UL;}
		@Override public ChiralRotation cw() {return El;}
		@Override public ChiralRotation ccw() {return Wl;}
		@Override public ChiralRotation opposite() {return Sl;}
		@Override public ChiralRotation flip() {return Nr;}
		@Override public ChiralRotation negate() {return Nl;}
		@Override
		public void renderGL(Texture tex, float x, float y, float w, float h, float r, float g, float b, float a, RenderCtx graphics) {
			graphics.renderColoredTexQuad(tex,
					r, g, b, a, 1, 0, x,   y,
					r, g, b, a, 1, 1, x,   y+h,
					r, g, b, a, 0, 1, x+w, y+h,
					r, g, b, a, 0, 0, x+w, y); //Nl
		}
	},
	Nr(Rotation.N, Chirality.R) {
		@Override public Side U() {return Side.U;}
		@Override public Side D() {return Side.D;}
		@Override public Side L() {return Side.L;}
		@Override public Side R() {return Side.R;}
		@Override public Side DL() {return Side.DL;}
		@Override public Side DR() {return Side.DR;}
		@Override public Side UL() {return Side.UL;}
		@Override public Side UR() {return Side.UR;}
		@Override public ChiralRotation cw() {return Er;}
		@Override public ChiralRotation ccw() {return Wr;}
		@Override public ChiralRotation opposite() {return Sr;}
		@Override public ChiralRotation flip() {return Nl;}
		@Override public ChiralRotation negate() {return Nr;}
		@Override
		public void renderGL(Texture tex, float x, float y, float w, float h, float r, float g, float b, float a, RenderCtx graphics) {
			graphics.renderColoredTexQuad(tex,
					r, g, b, a, 0, 0, x,   y,
					r, g, b, a, 0, 1, x,   y+h,
					r, g, b, a, 1, 1, x+w, y+h,
					r, g, b, a, 1, 0, x+w, y);
		}
	},
	El(Rotation.E, Chirality.L) {
		@Override public Side U() {return Side.R;}
		@Override public Side D() {return Side.L;}
		@Override public Side L() {return Side.D;}
		@Override public Side R() {return Side.U;}
		@Override public Side DL() {return Side.DL;}
		@Override public Side DR() {return Side.UL;}
		@Override public Side UL() {return Side.DR;}
		@Override public Side UR() {return Side.UR;}
		@Override public ChiralRotation cw() {return Sl;}
		@Override public ChiralRotation ccw() {return Nl;}
		@Override public ChiralRotation opposite() {return Wl;}
		@Override public ChiralRotation flip() {return Er;}
		@Override public ChiralRotation negate() {return El;}
		@Override
		public void renderGL(Texture tex, float x, float y, float w, float h, float r, float g, float b, float a, RenderCtx graphics) {
			graphics.renderColoredTexQuad(tex,
					r, g, b, a, 0, 0, x,   y,
					r, g, b, a, 1, 0, x,   y+h,
					r, g, b, a, 1, 1, x+w, y+h,
					r, g, b, a, 0, 1, x+w, y); //El
		}
	},
	Er(Rotation.E, Chirality.R) {
		@Override public Side U() {return Side.R;}
		@Override public Side D() {return Side.L;}
		@Override public Side L() {return Side.U;}
		@Override public Side R() {return Side.D;}
		@Override public Side DL() {return Side.UL;}
		@Override public Side DR() {return Side.DL;}
		@Override public Side UL() {return Side.UR;}
		@Override public Side UR() {return Side.DR;}
		@Override public ChiralRotation cw() {return Sr;}
		@Override public ChiralRotation ccw() {return Nr;}
		@Override public ChiralRotation opposite() {return Wr;}
		@Override public ChiralRotation flip() {return El;}
		@Override public ChiralRotation negate() {return Wr;}
		@Override
		public void renderGL(Texture tex, float x, float y, float w, float h, float r, float g, float b, float a, RenderCtx graphics) {
			graphics.renderColoredTexQuad(tex,
					r, g, b, a, 0, 1, x,   y,
					r, g, b, a, 1, 1, x,   y+h,
					r, g, b, a, 1, 0, x+w, y+h,
					r, g, b, a, 0, 0, x+w, y); //Nl
		}
	},
	Sl(Rotation.S, Chirality.L) {
		@Override public Side U() {return Side.D;}
		@Override public Side D() {return Side.U;}
		@Override public Side L() {return Side.L;}
		@Override public Side R() {return Side.R;}
		@Override public Side DL() {return Side.UL;}
		@Override public Side DR() {return Side.UR;}
		@Override public Side UL() {return Side.DL;}
		@Override public Side UR() {return Side.DR;}
		@Override public ChiralRotation cw() {return Wl;}
		@Override public ChiralRotation ccw() {return El;}
		@Override public ChiralRotation opposite() {return Nl;}
		@Override public ChiralRotation flip() {return Sr;}
		@Override public ChiralRotation negate() {return Sl;}
		@Override
		public void renderGL(Texture tex, float x, float y, float w, float h, float r, float g, float b, float a, RenderCtx graphics) {
			graphics.renderColoredTexQuad(tex,
					r, g, b, a, 0, 1, x,   y,
					r, g, b, a, 0, 0, x,   y+h,
					r, g, b, a, 1, 0, x+w, y+h,
					r, g, b, a, 1, 1, x+w, y); //Sl
		}
		
	},
	Sr(Rotation.S, Chirality.R) {
		@Override public Side U() {return Side.D;}
		@Override public Side D() {return Side.U;}
		@Override public Side L() {return Side.R;}
		@Override public Side R() {return Side.L;}
		@Override public Side DL() {return Side.UR;}
		@Override public Side DR() {return Side.UL;}
		@Override public Side UL() {return Side.DR;}
		@Override public Side UR() {return Side.DL;}
		@Override public ChiralRotation cw() {return Wr;}
		@Override public ChiralRotation ccw() {return Er;}
		@Override public ChiralRotation opposite() {return Nr;}
		@Override public ChiralRotation flip() {return Sl;}
		@Override public ChiralRotation negate() {return Sr;}
		
		@Override
		public void renderGL(Texture tex, float x, float y, float w, float h, float r, float g, float b, float a, RenderCtx graphics) {
			graphics.renderColoredTexQuad(tex,
					r, g, b, a, 1, 1, x,   y,
					r, g, b, a, 1, 0, x,   y+h,
					r, g, b, a, 0, 0, x+w, y+h,
					r, g, b, a, 0, 1, x+w, y);
		}
	},
	Wl(Rotation.W, Chirality.L) {
		@Override public Side U() {return Side.L;}
		@Override public Side D() {return Side.R;}
		@Override public Side L() {return Side.U;}
		@Override public Side R() {return Side.D;}
		@Override public Side DL() {return Side.UR;}
		@Override public Side DR() {return Side.DR;}
		@Override public Side UL() {return Side.UL;}
		@Override public Side UR() {return Side.DL;}
		@Override public ChiralRotation cw() {return Nl;}
		@Override public ChiralRotation ccw() {return Sl;}
		@Override public ChiralRotation opposite() {return El;}
		@Override public ChiralRotation flip() {return Wr;}
		@Override public ChiralRotation negate() {return Wl;}
		@Override
		public void renderGL(Texture tex, float x, float y, float w, float h, float r, float g, float b, float a, RenderCtx graphics) {
			graphics.renderColoredTexQuad(tex,
					r, g, b, a, 1, 1, x,   y,
					r, g, b, a, 0, 1, x,   y+h,
					r, g, b, a, 0, 0, x+w, y+h,
					r, g, b, a, 1, 0, x+w, y); //Wl
		}
	},
	Wr(Rotation.W, Chirality.R) {
		@Override public Side U() {return Side.L;}
		@Override public Side D() {return Side.R;}
		@Override public Side L() {return Side.D;}
		@Override public Side R() {return Side.U;}
		@Override public Side DL() {return Side.DR;}
		@Override public Side DR() {return Side.UR;}
		@Override public Side UL() {return Side.DL;}
		@Override public Side UR() {return Side.UL;}
		@Override public ChiralRotation cw() {return Nr;}
		@Override public ChiralRotation ccw() {return Sr;}
		@Override public ChiralRotation opposite() {return Er;}
		@Override public ChiralRotation flip() {return Wl;}
		@Override public ChiralRotation negate() {return Er;}
		@Override
		public void renderGL(Texture tex, float x, float y, float w, float h, float r, float g, float b, float a, RenderCtx graphics) {
			graphics.renderColoredTexQuad(tex,
					r, g, b, a, 1, 0, x,   y,
					r, g, b, a, 0, 0, x,   y+h,
					r, g, b, a, 0, 1, x+w, y+h,
					r, g, b, a, 1, 1, x+w, y);
		}
	};
	
	@Nonnull public final Rotation rotation;
	@Nonnull public final Chirality chirality;
	ChiralRotation(Rotation rotation, Chirality chirality) {
		this.rotation = rotation;
		this.chirality = chirality;
	}
	
	/**@return The upper side after this rotation*/
	@Nonnull public abstract Side U();
	/**@return The lower side after this rotation*/
	@Nonnull public abstract Side D();
	/**@return The left side after this rotation*/
	@Nonnull public abstract Side L();
	/**@return The right side after this rotation*/
	@Nonnull public abstract Side R();
	
	/**@return The lower left corner after this rotation*/
	@Nonnull public abstract Side DL();
	/**@return The lower right corner after this rotation*/
	@Nonnull public abstract Side DR();
	/**@return The upper left corner after this rotation*/
	@Nonnull public abstract Side UL();
	/**@return The upper right corner after this rotation*/
	@Nonnull public abstract Side UR();
	
	/**@return Rotation clockwise*/
	@Nonnull public abstract ChiralRotation cw();
	/**@return Rotation counter clockwise*/
	@Nonnull public abstract ChiralRotation ccw();
	/**@return Rotation opposite*/
	@Nonnull public abstract ChiralRotation opposite();
	
	/**@return Rotation with reversed chirality*/
	@Nonnull public abstract ChiralRotation flip();
	
	/**
	 * Construes a {@code ChiralRotation} object with given chirality and rotation
	 * @param rotation rotation of returned object
	 * @param chirality chirality of returned object
	 * @return object with given chirality and rotation
	 */
	@Nonnull public static ChiralRotation of(Rotation rotation, Chirality chirality) {
		switch(rotation) {
		case E:
			return chirality == Chirality.L ? El : Er;
		case N:
			return chirality == Chirality.L ? Nl : Nr;
		case S:
			return chirality == Chirality.L ? Sl : Sr;
		case W:
			return chirality == Chirality.L ? Wl : Wr;
		default:
			throw new IllegalArgumentException("Unexprected rotation: "+rotation);
		}
	}
	
	/** @return the chiral rotation flipped on | plane */
	@Nonnull public ChiralRotation flipH() {
		return of(rotation.flipH(), chirality.reverse());
	}
	/** @return the chiral rotation flipped on ― plane */
	@Nonnull public ChiralRotation flipV() {
		return of(rotation.flipV(), chirality.reverse());
	}
	/** @return the chiral rotation flipped on \ plane */
	@Nonnull public ChiralRotation flipNW() {
		return of(rotation.flipNW(), chirality.reverse());
	}
	/** @return the chiral rotation flipped on / plane */
	@Nonnull public ChiralRotation flipNE() {
		return of(rotation.flipNE(), chirality.reverse());
	}
	/** @return the chiral rotation with rotation flipped and chirality reversed*/
	@Nonnull public ChiralRotation flipopposite() {
		return of(rotation.opposite(), chirality.reverse());
	}
	@Nonnull public Side apply(Side s) {
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
			throw new IllegalArgumentException("Ukndown side: "+s);
		}
	}

	/**
	 * @return the chirotation {@code Y}, which for {@code Y.apply(this.apply(X))}, where {@code X} is any valid side, returns {@code X}
	 */
	public abstract ChiralRotation negate();
	
	public abstract void renderGL(Texture tex, float x, float y, float w, float h, float r, float g, float b, float a, RenderCtx graphics);
	public void renderGL(Texture tex, float x, float y, float w, float h, RenderCtx graphics) {
		renderGL(tex, x, y, w, h, 1, 1, 1, 1, graphics);
	}
}
