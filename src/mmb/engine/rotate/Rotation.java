/**
 * 
 */
package mmb.engine.rotate;

import java.util.EventListener;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import mmb.engine.gl.RenderCtx;
import mmb.engine.texture.Textures.Texture;

/**
 * @author oskar
 *
 */
public enum Rotation {
	/**
	 * The block faces north
	 */
	N {
		@Override public Side U() {return Side.U;}
		@Override public Side D() {return Side.D;}
		@Override public Side L() {return Side.L;}
		@Override public Side R() {return Side.R;}
		
		@Override public Side DL() {return Side.DL;}
		@Override public Side DR() {return Side.DR;}
		@Override public Side UL() {return Side.UL;}
		@Override public Side UR() {return Side.UR;}
		
		@Override public Rotation cw() {return E;}
		@Override public Rotation ccw() {return W;}
		@Override public Rotation opposite() {return S;}
		@Override public Rotation bwd() {return N;}
		
		@Override public ChiralRotation left() {return ChiralRotation.Nl;}
		@Override public ChiralRotation right() {return ChiralRotation.Nr;}
		
		@Override public Rotation flipH() {return N;}
		@Override public Rotation flipV() {return S;}
		@Override public Rotation flipNW() {return E;}
		@Override public Rotation flipNE() {return W;}
	},
	E{
		@Override public Side U() {return Side.R;}
		@Override public Side D() {return Side.L;}
		@Override public Side L() {return Side.U;}
		@Override public Side R() {return Side.D;}
		
		@Override public Side DL() {return Side.UL;}
		@Override public Side DR() {return Side.DL;}
		@Override public Side UL() {return Side.UR;}
		@Override public Side UR() {return Side.DR;}
		
		@Override public Rotation cw() {return S;}
		@Override public Rotation ccw() {return N;}
		@Override public Rotation opposite() {return W;}
		@Override public Rotation bwd() {return W;}
		
		@Override public ChiralRotation left() {return ChiralRotation.El;}
		@Override public ChiralRotation right() {return ChiralRotation.Er;}
		
		@Override public Rotation flipH() {return W;}
		@Override public Rotation flipV() {return E;}
		@Override public Rotation flipNW() {return N;}
		@Override public Rotation flipNE() {return S;}
		
	},
	S{
		@Override public Side U() {return Side.D;}
		@Override public Side D() {return Side.U;}
		@Override public Side L() {return Side.R;}
		@Override public Side R() {return Side.L;}
		
		@Override public Side DL() {return Side.UR;}
		@Override public Side DR() {return Side.UL;}
		@Override public Side UL() {return Side.DR;}
		@Override public Side UR() {return Side.DL;}
		
		@Override public Rotation cw() {return W;}
		@Override public Rotation ccw() {return E;}
		@Override public Rotation opposite() {return N;}
		@Override public Rotation bwd() {return S;}
		
		@Override public ChiralRotation left() {return ChiralRotation.Sl;}
		@Override public ChiralRotation right() {return ChiralRotation.Sr;}
		
		@Override public Rotation flipH() {return S;}
		@Override public Rotation flipV() {return N;}
		@Override public Rotation flipNW() {return W;}
		@Override public Rotation flipNE() {return E;}
	},
	W{
		@Override public Side U() {return Side.L;}
		@Override public Side D() {return Side.R;}
		@Override public Side L() {return Side.D;}
		@Override public Side R() {return Side.U;}
		
		@Override public Side DL() {return Side.DR;}
		@Override public Side DR() {return Side.UR;}
		@Override public Side UL() {return Side.DL;}
		@Override public Side UR() {return Side.UL;}
		
		@Override public Rotation cw() {return N;}
		@Override public Rotation ccw() {return S;}
		@Override public Rotation opposite() {return E;}
		@Override public Rotation bwd() {return W;}
		
		@Override public ChiralRotation left() {return ChiralRotation.Wl;}
		@Override public ChiralRotation right() {return ChiralRotation.Wr;}
		
		@Override public Rotation flipH() {return E;}
		@Override public Rotation flipV() {return W;}
		@Override public Rotation flipNW() {return S;}
		@Override public Rotation flipNE() {return N;}
	};
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
	@Nonnull public abstract Rotation cw();
	/**@return Rotation counter clockwise*/
	@Nonnull public abstract Rotation ccw();
	/**@return Rotation opposite*/
	@Nonnull public abstract Rotation opposite();
	/**@return Rotation backwards*/
	@Nonnull public abstract Rotation bwd();
	
	/**@return Left-handed chiral rotation*/
	@Nonnull public abstract ChiralRotation left();
	/**@return Right-handed chiral rotation*/
	@Nonnull public abstract ChiralRotation right();
	
	/**@return Mirrored on | plane */
	@Nonnull public abstract Rotation flipH();
	/**@return Mirrored on ― plane*/
	@Nonnull public abstract Rotation flipV();
	/**@return Mirrored on / plane*/
	@Nonnull public abstract Rotation flipNW();
	/**@return Mirrored on \ plane*/
	@Nonnull public abstract Rotation flipNE();
	public void renderGL(Texture tex, float x, float y, float w, float h, float r, float g, float b, float a, RenderCtx graphics) {
		ChiralRotation.of(this, Chirality.R).renderGL(tex, x, y, w, h, r, g, b, a, graphics);
	}
	public void renderGL(Texture tex, float x, float y, float w, float h, RenderCtx graphics) {
		renderGL(tex, x, y, w, h, 1, 1, 1, 1, graphics);
	}
	
	/**
	 * @param chirality chirality of returned orientation
	 * @return Rotation with specified chirality
	 */
	@Nonnull public ChiralRotation chiral(Chirality chirality) {
		return chirality == Chirality.L ? left() : right();
	}
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

	public static interface RotationListener extends EventListener, Consumer<Rotation>{
		//unused
	}
}
