/**
 * 
 */
package mmb.engine.rotate;

import mmb.annotations.NN;
import mmb.engine.gl.RenderCtx;
import mmb.engine.texture.Textures.Texture;

/**
 * Combines chirality and rotation
 * @author oskar
 *
 */
public enum ChiralRotation {
	/** North rotation with anticlockwise chirality*/
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
	/** North rotation with clockwise chirality*/
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
	/** East rotation with anticlockwise chirality */
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
	/** East rotation with clockwise chirality */
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
	/** South rotation with anticlockwise chirality*/
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
	/** South rotation with clockwise chirality */
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
	/** West rotation with anticlockwise chirality */
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
	/** West rotation with clockwise chirality */
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
	
	/** The rotation of this chirotation */
	@NN public final Rotation rotation;
	/** The chirality of this chirotation */
	@NN public final Chirality chirality;
	ChiralRotation(Rotation rotation, Chirality chirality) {
		this.rotation = rotation;
		this.chirality = chirality;
	}
	
	/**@return The upper side after this rotation*/
	@NN public abstract Side U();
	/**@return The lower side after this rotation*/
	@NN public abstract Side D();
	/**@return The left side after this rotation*/
	@NN public abstract Side L();
	/**@return The right side after this rotation*/
	@NN public abstract Side R();
	
	/**@return The lower left corner after this rotation*/
	@NN public abstract Side DL();
	/**@return The lower right corner after this rotation*/
	@NN public abstract Side DR();
	/**@return The upper left corner after this rotation*/
	@NN public abstract Side UL();
	/**@return The upper right corner after this rotation*/
	@NN public abstract Side UR();
	
	/**@return Rotation clockwise*/
	@NN public abstract ChiralRotation cw();
	/**@return Rotation counter clockwise*/
	@NN public abstract ChiralRotation ccw();
	/**@return Rotation opposite*/
	@NN public abstract ChiralRotation opposite();
	
	/**@return Rotation with reversed chirality*/
	@NN public abstract ChiralRotation flip();
	
	/**
	 * Construes a {@code ChiralRotation} object with given chirality and rotation
	 * @param rotation rotation of returned object
	 * @param chirality chirality of returned object
	 * @return object with given chirality and rotation
	 */
	@NN public static ChiralRotation of(Rotation rotation, Chirality chirality) {
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
	@NN public ChiralRotation flipH() {
		return of(rotation.flipH(), chirality.reverse());
	}
	/** @return the chiral rotation flipped on ― plane */
	@NN public ChiralRotation flipV() {
		return of(rotation.flipV(), chirality.reverse());
	}
	/** @return the chiral rotation flipped on \ plane */
	@NN public ChiralRotation flipNW() {
		return of(rotation.flipNW(), chirality.reverse());
	}
	/** @return the chiral rotation flipped on / plane */
	@NN public ChiralRotation flipNE() {
		return of(rotation.flipNE(), chirality.reverse());
	}
	/** @return the chiral rotation with rotation flipped and chirality reversed*/
	@NN public ChiralRotation flipopposite() {
		return of(rotation.opposite(), chirality.reverse());
	}
	/**
	 * Applies the chirotation to the side
	 * @param s side to apply to
	 * @return transformed side
	 */
	@NN public Side apply(Side s) {
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
	
	/**
	 * Renders a colored OpenGL texture chirotated with specified chirotation
	 * @param tex texture
	 * @param x left X coordinate
	 * @param y upper Y coordinate
	 * @param w width
	 * @param h height
	 * @param r red color component
	 * @param g green color component
	 * @param b blue color component
	 * @param a alpha color component
	 * @param graphics graphics context
	 */
	public abstract void renderGL(Texture tex, float x, float y, float w, float h, float r, float g, float b, float a, RenderCtx graphics);
	/**
	 * Renders a colored OpenGL texture chirotated with specified chirotation
	 * @param tex texture
	 * @param x left X coordinate
	 * @param y upper Y coordinate
	 * @param w width
	 * @param h height
	 * @param graphics graphics context
	 */
	public void renderGL(Texture tex, float x, float y, float w, float h, RenderCtx graphics) {
		renderGL(tex, x, y, w, h, 1, 1, 1, 1, graphics);
	}
}
