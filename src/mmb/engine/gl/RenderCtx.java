/**
 * 
 */
package mmb.engine.gl;

import org.joml.Matrix2f;
import org.joml.Matrix2fc;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import mmb.NN;
import mmb.engine.texture.Textures.Texture;

/**
 * @author oskar
 * Provides an interface for rendering half-vector 2D graphics
 */
public abstract class RenderCtx implements AutoCloseable{
	@NN private static final Vector4fc WHITE = new Vector4f(1, 0, 0, 0);
	//Z-index
	/**
	 * Sets the z-index, which is used for ordering elements.
	 * @param z the z-index
	 */
	public abstract void zindex(float z);
	
	//=====================================================================Tris
	/**
	 * Renders a single colored triangle
	 * @param col1 the third color
	 * @param pos1 the third position
	 * 
	 * @param col2 the second color
	 * @param pos2 the second position
	 * 
	 * @param col3 the third color
	 * @param pos3 the third position
	 */
	public abstract void renderColoredTri(
			Vector4fc col1, Vector2fc pos1,
			Vector4fc col2, Vector2fc pos2,
			Vector4fc col3, Vector2fc pos3);
	
	/**
	 * Renders a single textured triangle
	 * @param tex texture
	 * 
	 * @param uv1 the third texture coordinate
	 * @param pos1 the third position
	 * 
	 * @param uv2 the second texture coordinate
	 * @param pos2 the second color
	 * 
	 * @param uv3 the third texture coordinate
	 * @param pos3 the third position
	 */
	public void renderTexTri(
			Texture tex,
			Vector2fc uv1, Vector2fc pos1,
			Vector2fc uv2, Vector2fc pos2,
			Vector2fc uv3, Vector2fc pos3) {
		renderColoredTexTri(tex,
				WHITE, uv1, pos1,
				WHITE, uv2, pos2,
				WHITE, uv3, pos3);
	}
	
	/**
	 * Renders a single textured triangle
	 * @param tex texture
	 * 
	 * @param col1 the third color
	 * @param uv1 the third texture coordinate
	 * @param pos1 the third position
	 * 
	 * @param col2 the second color
	 * @param uv2 the second texture coordinate
	 * @param pos2 the second color
	 * 
	 * @param col3 the third color
	 * @param uv3 the third texture coordinate
	 * @param pos3 the third position
	 */
	public abstract void renderColoredTexTri(
			Texture tex,
			Vector4fc col1, Vector2fc uv1, Vector2fc pos1,
			Vector4fc col2, Vector2fc uv2, Vector2fc pos2,
			Vector4fc col3, Vector2fc uv3, Vector2fc pos3);
	
	//=====================================================================Quads
	/**
	 * Renders a single colored triangle
	 * @param col1 the third color
	 * @param pos1 the third position
	 * 
	 * @param col2 the second color
	 * @param pos2 the second position
	 * 
	 * @param col3 the third color
	 * @param pos3 the third position
	 * 
	 * @param col4 the fourth color
	 * @param pos4 the fourth position
	 * 
	 * @apiNote This is a fallback implementation
	 */
	public abstract void renderColoredQuad(
			Vector4fc col1, Vector2fc pos1,
			Vector4fc col2, Vector2fc pos2,
			Vector4fc col3, Vector2fc pos3,
			Vector4fc col4, Vector2fc pos4);
	
	/**
	 * Renders a single colored triangle
	 *@param r1 third red color component
	 * @param g1 third green color component
	 * @param b1 third blue color component
	 * @param a1 third alpha color component
	 * @param x1 third X position coordinate
	 * @param y1 third Y position coordinate
	 * 
	 * @param r2 second red color component
	 * @param g2 second green color component
	 * @param b2 second blue color component
	 * @param a2 second alpha color component
	 * @param x2 second X position coordinate
	 * @param y2 second Y position coordinate
	 * 
	 * @param r3 third red color component
	 * @param g3 third green color component
	 * @param b3 third blue color component
	 * @param a3 third alpha color component
	 * @param x3 third X position coordinate
	 * @param y3 third Y position coordinate
	 * 
	 * @param r4 fourth red color component
	 * @param g4 fourth green color component
	 * @param b4 fourth blue color component
	 * @param a4 fourth alpha color component
	 * @param x4 fourth X position coordinate
	 * @param y4 fourth Y position coordinate
	 * 
	 * @apiNote This is a fallback implementation
	 */
	public abstract void renderColoredQuad(
			float r1, float g1, float b1, float a1, float x1, float y1,
			float r2, float g2, float b2, float a2, float x2, float y2,
			float r3, float g3, float b3, float a3, float x3, float y3,
			float r4, float g4, float b4, float a4, float x4, float y4);
	
	/**
	 * Renders a single textured triangle
	 * @param tex texture
	 * 
	 * @param uv1 the third texture coordinate
	 * @param pos1 the third position
	 * 
	 * @param uv2 the second texture coordinate
	 * @param pos2 the second color
	 * 
	 * @param uv3 the third texture coordinate
	 * @param pos3 the third position
	 * 
	 * @param uv4 the fourth texture coordinate
	 * @param pos4 the fourth position
	 */
	public void renderTexQuad(
			Texture tex,
			Vector2fc uv1, Vector2fc pos1,
			Vector2fc uv2, Vector2fc pos2,
			Vector2fc uv3, Vector2fc pos3,
			Vector2fc uv4, Vector2fc pos4) {
		renderColoredTexQuad(tex,
				WHITE, uv1, pos1,
				WHITE, uv2, pos2,
				WHITE, uv3, pos3,
				WHITE, uv4, pos4);
	}
	
	/**
	 * 
	 * @param tex texture
	 * @param u1 first U texture coordinate
	 * @param v1 first V texture coordinate
	 * @param x1 first X position coordinate
	 * @param y1 first Y position coordinate
	 * 
	 * @param u2 second U texture coordinate
	 * @param v2 second V texture coordinate
	 * @param x2 second X position coordinate
	 * @param y2 second Y position coordinate
	 * 
	 * @param u3 third U texture coordinate
	 * @param v3 third V texture coordinate
	 * @param x3 third X position coordinate
	 * @param y3 third Y position coordinate
	 * 
	 * @param u4 fourth U texture coordinate
	 * @param v4 fourth V texture coordinate
	 * @param x4 fourth X position coordinate
	 * @param y4 fourth Y position coordinate
	 */
	public void renderTexQuad(
			Texture tex,
			float u1, float v1, float x1, float y1,
			float u2, float v2, float x2, float y2,
			float u3, float v3, float x3, float y3,
			float u4, float v4, float x4, float y4) {
		renderColoredTexQuad(tex,
				1,1,1,1, u1,v1, x1,y1,
				1,1,1,1, u2,v2, x2,y2,
				1,1,1,1, u3,v3, x3,y3,
				1,1,1,1, u4,v4, x4,y4);
	}
	
	/**
	 * 
	 * @param tex texture
	 * @param col1 the third color
	 * @param uv1 the third texture coordinate
	 * @param pos1 the third position
	 * 
	 * @param col2 the second color
	 * @param uv2 the second texture coordinate
	 * @param pos2 the second color
	 * 
	 * @param col3 the third color
	 * @param uv3 the third texture coordinate
	 * @param pos3 the third position
	 * 
	 * @param col4 the fourth color
	 * @param uv4 the fourth texture coordinate
	 * @param pos4 the fourth position
	 */
	public void renderColoredTexQuad(Texture tex,
			Vector4fc col1, Vector2fc uv1, Vector2fc pos1,
			Vector4fc col2, Vector2fc uv2, Vector2fc pos2,
			Vector4fc col3, Vector2fc uv3, Vector2fc pos3,
			Vector4fc col4, Vector2fc uv4, Vector2fc pos4) {
		renderColoredTexQuad(
				tex,
				col1.x(),col1.y(),col1.z(),col1.w(), uv1.x(),uv1.y(), pos1.x(),pos1.y(),
				col2.x(),col2.y(),col2.z(),col2.w(), uv2.x(),uv2.y(), pos2.x(),pos2.y(),
				col3.x(),col3.y(),col3.z(),col3.w(), uv3.x(),uv3.y(), pos3.x(),pos3.y(),
				col4.x(),col4.y(),col4.z(),col4.w(), uv4.x(),uv4.y(), pos4.x(),pos4.y());
	}
	
	/**
	 * Renders a single textured triangle
	 * @param tex texture
	 * @param r1 third red color component
	 * @param g1 third green color component
	 * @param b1 third blue color component
	 * @param a1 third alpha color component
	 * @param u1 third U texture coordinate
	 * @param v1 third V texture coordinate
	 * @param x1 third X position coordinate
	 * @param y1 third Y position coordinate
	 * 
	 * @param r2 second red color component
	 * @param g2 second green color component
	 * @param b2 second blue color component
	 * @param a2 second alpha color component
	 * @param u2 second U texture coordinate
	 * @param v2 second V texture coordinate
	 * @param x2 second X position coordinate
	 * @param y2 second Y position coordinate
	 * 
	 * @param r3 third red color component
	 * @param g3 third green color component
	 * @param b3 third blue color component
	 * @param a3 third alpha color component
	 * @param u3 third U texture coordinate
	 * @param v3 third V texture coordinate
	 * @param x3 third X position coordinate
	 * @param y3 third Y position coordinate
	 * 
	 * @param r4 fourth red color component
	 * @param g4 fourth green color component
	 * @param b4 fourth blue color component
	 * @param a4 fourth alpha color component
	 * @param u4 fourth U texture coordinate
	 * @param v4 fourth V texture coordinate
	 * @param x4 fourth X position coordinate
	 * @param y4 fourth Y position coordinate
	 */
	public abstract void renderColoredTexQuad(
			Texture tex,
			float r1, float g1, float b1, float a1, float u1, float v1, float x1, float y1,
			float r2, float g2, float b2, float a2, float u2, float v2, float x2, float y2,
			float r3, float g3, float b3, float a3, float u3, float v3, float x3, float y3,
			float r4, float g4, float b4, float a4, float u4, float v4, float x4, float y4);
	
	public abstract void renderCircle(float x, float y, float radius, float r, float g, float b, float a);
	public abstract void fillCircle(float x, float y, float radius, float r, float g, float b, float a);
	
	//Simplified methods
	public void renderTextureSimple(Texture tex, float x, float y, float w, float h) {
		renderTextureRecolored(tex, x, y, w, h, 1, 1, 1, 1);
	}
	public void renderTextureRecolored(Texture tex, float x, float y, float w, float h, float r, float g, float b, float a) {
		renderColoredTexQuad(tex,
				r, g, b, a, 0, 0, x,   y,
				r, g, b, a, 0, 1, x,   y+h,
				r, g, b, a, 1, 1, x+w, y+h,
				r, g, b, a, 1, 0, x+w, y);
	}
	public void renderRectSimple(float x, float y, float w, float h, float r, float g, float b, float a) {
		renderColoredQuad(
				r, g, b, a, x,   y,
				r, g, b, a, x,   y+h,
				r, g, b, a, x+w, y+h,
				r, g, b, a, x+w, y);
	}
	/*public void renderLineQuad(float x1, float y1, float x2, float y2, float leftEdge, float rightEdge, float r, float g, float b, float a) {
		
	}*/
	public void renderLineQuadTex(float x1, float y1, float x2, float y2,
			float leftEdge, float rightEdge, Texture tex,
			float r, float g, float b, float a) {
		renderLineQuadTex(x1, y1, x2, y2, leftEdge, rightEdge, tex, r, g, b, a, r, g, b, a);
	}
	/**
	 * 
	 * @param x1 beginning X coordinate
	 * @param y1 beginning Y coordinate
	 * @param x2 end X coordinate
	 * @param y2 end X coordinate
	 * @param leftEdge offset towards the right on the left edge
	 * @param rightEdge offset towards the right on the right edge
	 * @param tex texture
	 * @param r1 beginning red
	 * @param g1 beginning green
	 * @param b1 beginning blue
	 * @param a1 beginning alpha
	 * @param r2 end red
	 * @param g2 end green
	 * @param b2 end blue
	 * @param a2 end alpha
	 */
	public void renderLineQuadTex(float x1, float y1, float x2, float y2,
			float leftEdge, float rightEdge, Texture tex,
			float r1, float g1, float b1, float a1,
			float r2, float g2, float b2, float a2) {
		float diffX = x2-x1;
		float diffY = y2-y1;
		float len = Vector2f.length(diffX, diffY);
		float rx = diffY/len;
		float ry = -diffX/len;
		float loffX = rx*leftEdge;
		float loffY = ry*leftEdge;
		float roffX = rx*rightEdge;
		float roffY = ry*rightEdge;
		renderColoredTexQuad(tex,
				r1, g1, b1, a1, 0, 1, x1+loffX, y1+loffY,
				r1, g1, b1, a1, 1, 1, x1+roffX, y1+roffY,
				r2, g2, b2, a2, 1, 0, x2+roffX, y2+roffY,
				r2, g2, b2, a2, 0, 0, x2+loffX, y2+loffY);
	}
	
	//Transformation
 	public void scale(float scale) {
		transform(new Matrix2f().scaling(scale));
	}
	public void scale(float x, float y) {
		transform(new Matrix2f().scaling(x, y));
	}
	public void scale(Vector2fc scale) {
		transform(new Matrix2f().scaling(scale));
	}
	public void move(float x, float y) {
		move(new Vector2f(x, y));
	}
	public void move(Vector2fc offset) {
		transform(new Matrix2f().identity(), offset);
	}
	public void rotate(float rotation) {
		transform(new Matrix2f().rotation(rotation));
	}
	public void rotateMove(float rotation, Vector2fc offset) {
		transform(new Matrix2f().rotation(rotation), offset);
	}
	public void rotateDegs(double rotation) {
		rotate((float) Math.toRadians(rotation));
	}
	public void scaleMove(float offsetX, float offsetY, float scaleX, float scaleY) {
		transform(new Matrix2f().scaling(scaleX, scaleY), new Vector2f(offsetX, offsetY));
	}
	
	/**
	 * @param minIn minimum corner of the input
	 * @param maxIn maximum corner of the input
	 * @param minOut minimum corner of the input
	 * @param maxOut maximum corner of the input
	 * 
	 * See the source for a clear image
	 * transform A-B to C-D
	 * 
	 * 		A----.
	 *      |    |     C----------.
	 *      .----B     |          |
	 *                 |          |
	 *                 .----------D
	 */
	public void remapRect(Vector2fc minIn, Vector2fc maxIn, Vector2fc minOut, Vector2fc maxOut) {
		/*
		 * [(a,b)-(c,d)] to [(e,f)-(g,h)]
		 * x' = e + (x - a) * (g - e) / (c - a); //NOSONAR this is not a valid code
		 * y' = f + (y - b) * (h - f) / (d - b); //NOSONAR this is not a valid code
		 * Offset: (e, f)
		 * Scale:
		 */
		float scaleX = (maxOut.x()-minOut.x())/(maxIn.x()-minIn.x());
		float scaleY = (maxOut.y()-minOut.y())/(maxIn.y()-minIn.y());
		float offsetX = minOut.x()-(minIn.x()*scaleX);
		float offsetY = minOut.y()-(minIn.y()*scaleY);
		scaleMove(offsetX, offsetY, scaleX, scaleY);
	}
	
	/**
	 * 
	 * @param beginA the A vertex on the image
	 * @param thirdA the B vertex on the image
	 * @param secondA the C vertex on the image
	 * 
	 * See the source for a clear image
	 * transform B-A-C to E-D-F
	 * 
	 *     A----------B
	 *    /          /
	 *   /          / 
	 *  C----------.
	 */
	public void demapParallelogram(Vector2fc beginA, Vector2fc thirdA, Vector2fc secondA) {
		transform(ParallelogramTransform.demapParallelogram(beginA, thirdA, secondA, new Transform2()));
	}
	
	/**
	 * 
	 * @param beginA the A vertex on the image
	 * @param thirdA the B vertex on the image
	 * @param secondA the C vertex on the image
	 * @param beginB the D vertex on the image
	 * @param thirdB the E vertex on the image
	 * @param secondB the F vertex on the image
	 * @return new graphics context
	 * 
	 * See the source for a clear image
	 * transform B-A-C to E-D-F
	 * 
	 *     A----------B
	 *    /          /       D
	 *   /          /       / \
	 *  C----------.       /   \
	 *                    /     \
	 *                   E       F
	 *                    \     /
	 *                     \   /
	 *                      \ /
	 *                       .
	 */
	/*public RenderCtx remapParallelogram(
			Vector2fc beginA, Vector2fc firstA, Vector2fc secondA,
			Vector2fc beginB, Vector2fc firstB, Vector2fc secondB) {
	}*/
	
	/**
	 * Transforms using a matrix
	 * @param mat transformation matrix
	 */
	public void transform(Matrix2fc mat) {
		transform(mat, new Vector2f());
	}
	
	/**
	 * Sets the transform of this context
	 * @param mat
	 * @param offset
	 */
	public abstract void transform(Matrix2fc mat, Vector2fc offset);
	
	/**
	 * Transforms this context
	 * @param tf transform to apply
	 */
	public void transform(Transform2 tf) {
		transform(tf.mat, tf.offset);
	}
	/**
	 * Mutates the input object to be a transform used by this context
	 * @param tf
	 * @return
	 */
	public abstract Transform2 getRelativeTransform(Transform2 tf);
	
	/**
	 * Runs the rendering magic.
	 * The context must be already set
	 */
	public abstract void render();
	
	//Resources
	/**
	 * Sets up the rendering context.
	 * This includes:
	 * <ul>
	 * 	<li>loading in shaders</li>
	 * 	<li>loading in texture atlases</li>
	 *  <li>creating VBOs and VAOs</li>
	 * </ul>
	 */
	public abstract void setup();
	/**
	 * Destroys this rendering context
	 */
	@Override
	public abstract void close();
}
