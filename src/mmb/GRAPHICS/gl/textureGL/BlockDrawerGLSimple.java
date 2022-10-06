/**
 * 
 */
package mmb.GRAPHICS.gl.textureGL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joml.Vector4f;
import org.joml.Vector4fc;

import mmb.DATA.contents.Textures.Texture;
import mmb.GRAPHICS.gl.RenderCtx;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.rotate.ChiralRotation;

/**
 * @author oskar
 *
 */
public class BlockDrawerGLSimple {
	@Nonnull public final Texture tex;
	@Nonnull public final ChiralRotation rot;
	/**
	 * This texture's color. Components may be greater than 1
	 */
	public final float r, g, b, a;
	
	public BlockDrawerGLSimple(Texture tex) {
		this(tex, 1, 1, 1, 1, ChiralRotation.Nr);
	}
	public BlockDrawerGLSimple(Texture tex, Vector4fc vec) {
		this(tex, vec.x(), vec.y(), vec.z(), vec.w(), ChiralRotation.Nr);
	}
	public BlockDrawerGLSimple(Texture tex, float r, float g, float b) {
		this(tex, r, g, b, 1, ChiralRotation.Nr);
	}
	public BlockDrawerGLSimple(Texture tex, float r, float g, float b, float a) {
		this(tex, r, g, b, a, ChiralRotation.Nr);
	}
	public BlockDrawerGLSimple(Texture tex, ChiralRotation rot) {
		this(tex, 1, 1, 1, 1, rot);
	}
	public BlockDrawerGLSimple(Texture tex, Vector4fc vec, ChiralRotation rot) {
		this(tex, vec.x(), vec.y(), vec.z(), vec.w(), rot);
	}
	public BlockDrawerGLSimple(Texture tex, float r, float g, float b, ChiralRotation rot) {
		this.tex = tex;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1;
		this.rot = rot;
	}
	public BlockDrawerGLSimple(Texture tex, float r, float g, float b, float a, ChiralRotation rot) {
		this.tex = tex;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.rot = rot;
	}
	
	/**
	 * Renders this block
	 * @param ent block entry
	 * @param x X position in world
	 * @param y Y position in world
	 * @param gr graphics context
	 * @param w width in meters
	 * @param h height in meters
	 */
	public void drawGL(@Nullable BlockEntry ent, float x, float y, RenderCtx gr, float w, float h) {
		rot.renderGL(tex, x, y, w, h, r, g, b, a, gr);
	}
	/**
	 * Renders this block
	 * @param ent block entry
	 * @param x X position in world
	 * @param y Y position in world
	 * @param gr graphics context
	 * @param w width in meters
	 * @param h height in meters
	 * @param r red color
	 * @param g green color
	 * @param b blue color
	 * @param a alpha color
	 */
	public void drawGL(@Nullable BlockEntry ent, float x, float y, RenderCtx gr, float w, float h, float r, float g, float b, float a) {
		rot.renderGL(tex, x, y, w, h, r, g, b, a, gr);
	}
	public Vector4f color(Vector4f vec) {
		return vec.set(r, g, b, a);
	}
	public BlockDrawerGLSimple replaceColor(Vector4fc color) {
		return new BlockDrawerGLSimple(tex, color, rot);
	}
	public BlockDrawerGLSimple replaceColor(float r, float g, float b) {
		return new BlockDrawerGLSimple(tex, r, g, b, rot);
	}
	public BlockDrawerGLSimple replaceColor(float r, float g, float b, float a) {
		return new BlockDrawerGLSimple(tex, r, g, b, a, rot);
	}
	/**
	 * @param dest destination
	 * @return a low level of detail color in sRGB
	 */
	public Vector4f LOD(Vector4f dest) {
		tex.LOD(dest);
		dest.mul(r, g, b, a);
		return dest;
	}
}
