/**
 * 
 */
package mmb.engine.gl;

import org.apache.commons.io.IOUtils;
import org.joml.Matrix2f;
import org.joml.Matrix2fc;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector4fc;
import com.rainerhahnekamp.sneakythrow.Sneaky;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL30.*;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.FloatList;
import mmb.NN;
import mmb.engine.debug.Debugger;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.texture.Textures;
import mmb.engine.texture.Textures.Texture;

/**
 * @author oskar
 *
 */
public class SimpleCtx extends RenderCtx {
	/**
	 * 
	 */
	public SimpleCtx() {
		this(new Matrix2f(), new Vector2f());
	}
	
	/**
	 * Creates a context with transformation
	 * @param tf
	 * @param offset
	 */
	public SimpleCtx(Matrix2fc tf, Vector2fc offset) {
		this.offset = new Vector2f(offset);
		this.mat = new Matrix2f(tf);
	}
	
	@NN private final Vector2f offset;
	@NN private final Matrix2f mat;
	
	/**
	 * Format:
	 * R color
	 * G color
	 * B color
	 * A color
	 * U position
	 * V position
	 * X position
	 * Y position
	 */
	@NN private final FloatList tris = new FloatArrayList();
	@NN private final FloatList quads = new FloatArrayList();
	@NN private final FloatList trisPlain = new FloatArrayList();
	@NN private final FloatList quadsPlain = new FloatArrayList();
	@NN private final FloatList circles = new FloatArrayList();
	@NN private final FloatList circlesFill = new FloatArrayList();

	@Override
	public void transform(Matrix2fc mat, Vector2fc offset) {
		this.offset.set(offset);
		this.mat.set(mat);
		matrixChanged = true;
	}

	private static final Debugger debug = new Debugger("TEST CTX");
	
	private Matrix4f glmatrix = new Matrix4f();
	private boolean matrixChanged = true;
	
	@Override
	public void render() {
		//Update matrices
		if(matrixChanged) { //optimization: re-upload matrix only if changed
			glmatrix.set(
					mat.m00, mat.m10,  0, 0,
					mat.m01, mat.m11,  0, 0,
					0,       0,        1, 0,
					offset.x,offset.y, 0, 1);
			matrixChanged = false;
			float[] matrixdata = glmatrix.get(new float[16]);
			glLoadMatrixf(matrixdata);
		}
		
		
		glEnable(GL_TEXTURE_2D);
		//Textured tris
		glBegin(GL_TRIANGLES);
		FloatIterator triiter = tris.iterator();
		while(triiter.hasNext()) {
			float z = triiter.nextFloat();
			vertex(triiter, z);
			vertex(triiter, z);
			vertex(triiter, z);
		}
		glEnd();
		
		//Textured quads
		glBegin(GL_QUADS);
		FloatIterator quaditer = quads.iterator();
		while(quaditer.hasNext()) {
			float z = quaditer.nextFloat();
			vertex(quaditer, z);
			vertex(quaditer, z);
			vertex(quaditer, z);
			vertex(quaditer, z);
		}
		glEnd();
		
		glDisable(GL_TEXTURE_2D);
		//Plain tris
		glBegin(GL_TRIANGLES);
		FloatIterator triiter2 = trisPlain.iterator();
		while(triiter2.hasNext()) {
			float z = triiter.nextFloat();
			vertexPlain(triiter2, z);
			vertexPlain(triiter2, z);
			vertexPlain(triiter2, z);
		}
		glEnd();
		
		//Plain quads
		glBegin(GL_QUADS);
		FloatIterator quaditer2 = quadsPlain.iterator();
		while(quaditer2.hasNext()) {
			float z = quaditer2.nextFloat();
			vertexPlain(quaditer2, z);
			vertexPlain(quaditer2, z);
			vertexPlain(quaditer2, z);
			vertexPlain(quaditer2, z);
		}
		glEnd();
		
		//Circles (fill)
		final int ACCURACY = GlobalSettings.circleAccuracy.getInt();
		FloatIterator circliter = circlesFill.iterator();
		while(circliter.hasNext()) {
			float z = circliter.nextFloat();
			glBegin(GL_TRIANGLE_FAN);
			glColor4f(circliter.nextFloat(), circliter.nextFloat(), circliter.nextFloat(), circliter.nextFloat());
			float radius = circliter.nextFloat();
			float x = circliter.nextFloat();
			float y = circliter.nextFloat();
			float step = (float) (2*Math.PI/ACCURACY);
			float angle=0;
			for(int i = 0; i < ACCURACY; i++, angle += step) {
				float sin = (float)Math.sin(angle);
				float cos = (float)Math.cos(angle);
				glVertex3f(x+sin*radius, y+cos*radius, z);
			}
			glEnd();
		}
		
		//Circles (line)
		FloatIterator circliter1 = circles.iterator();
		while(circliter1.hasNext()) {
			float z = circliter1.nextFloat();
			glBegin(GL_LINE_LOOP);
			glColor4f(circliter1.nextFloat(), circliter1.nextFloat(), circliter1.nextFloat(), circliter1.nextFloat());
			float radius = circliter1.nextFloat();
			float x = circliter1.nextFloat();
			float y = circliter1.nextFloat();
			float step = (float) (2*Math.PI/ACCURACY);
			float angle=0;
			for(int i = 0; i < ACCURACY; i++, angle += step) {
				float sin = (float)Math.sin(angle);
				float cos = (float)Math.cos(angle);
				glVertex3f(x+sin*radius, y+cos*radius, z);
			}
			glEnd();
		}
		
		//Clear old data
		tris.clear();
		quads.clear();
		trisPlain.clear();
		quadsPlain.clear();
		circles.clear();
		circlesFill.clear();
	}
	private static void vertexPlain(FloatIterator iter, float zindex) {
		glColor4f(iter.nextFloat(), iter.nextFloat(), iter.nextFloat(), iter.nextFloat());
		glVertex3f(iter.nextFloat(), iter.nextFloat(), zindex);
	}
	private static void vertex(FloatIterator iter, float zindex) {
		glColor4f(iter.nextFloat(), iter.nextFloat(), iter.nextFloat(), iter.nextFloat());
		glTexCoord2f(iter.nextFloat(), iter.nextFloat());
		glVertex3f(iter.nextFloat(), iter.nextFloat(), zindex);
	}
	
	@Override
	public Transform2 getRelativeTransform(Transform2 tf) {
		return new Transform2(offset, mat);
	}

	//The resources
	private int texID;
	@Override
	public void setup() {
		//Create the textures
		glEnable(GL_TEXTURE_2D);
		Vector2i texSize = Textures.atlasSize(new Vector2i());
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, texSize.x, texSize.y, 0, GL_RGBA, GL_UNSIGNED_BYTE, Textures.getAtlas());
        
        //Pass the texture atlas to shader
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texID);
        
        //Enable transparency
        glEnable(GL_ALPHA_TEST);
        glAlphaFunc(GL_GREATER, 0.5f);

        //Enable depth test
        glEnable(GL_DEPTH_TEST);
	}
	@Override
	public void close(){
		//Disable vertex attributes
		glDisableVertexAttribArray(0);
	    glDisableVertexAttribArray(1);
	    glDisableVertexAttribArray(2);
		//Destroy the texture atlas
		glDisable(GL_TEXTURE_2D);
		glDeleteTextures(texID);
		//Destroy the VBO
	}

	@NN private final Vector2f reuseUV = new Vector2f();
	@Override
	public void renderColoredTexTri(Texture tex,
			Vector4fc col1, Vector2fc uv1, Vector2fc pos1,
			Vector4fc col2, Vector2fc uv2, Vector2fc pos2,
			Vector4fc col3, Vector2fc uv3, Vector2fc pos3) {
		tris.add(zindex);
		FloatMath2Collects.push(col1, tris); FloatMath2Collects.push(tex.uvconvert(uv1, reuseUV), tris); FloatMath2Collects.push(pos1, tris);
		FloatMath2Collects.push(col2, tris); FloatMath2Collects.push(tex.uvconvert(uv2, reuseUV), tris); FloatMath2Collects.push(pos2, tris);
		FloatMath2Collects.push(col3, tris); FloatMath2Collects.push(tex.uvconvert(uv3, reuseUV), tris); FloatMath2Collects.push(pos3, tris);
	}

	@Override
	public void renderColoredTexQuad(Texture tex,
			float r1, float g1, float b1, float a1, float u1, float v1, float x1, float y1,
			float r2, float g2, float b2, float a2, float u2, float v2, float x2, float y2,
			float r3, float g3, float b3, float a3, float u3, float v3, float x3, float y3,
			float r4, float g4, float b4, float a4, float u4, float v4, float x4, float y4) {
		quads.add(zindex);
		quads.add(r1); quads.add(g1); quads.add(b1); quads.add(a1);
		FloatMath2Collects.push(tex.uvconvert(u1, v1, reuseUV), quads);
		quads.add(x1); quads.add(y1);
		
		quads.add(r2); quads.add(g2); quads.add(b2); quads.add(a2);
		FloatMath2Collects.push(tex.uvconvert(u2, v2, reuseUV), quads);
		quads.add(x2); quads.add(y2);
		
		quads.add(r3); quads.add(g3); quads.add(b3); quads.add(a3);
		FloatMath2Collects.push(tex.uvconvert(u3, v3, reuseUV), quads);
		quads.add(x3); quads.add(y3);
		
		quads.add(r4); quads.add(g4); quads.add(b4); quads.add(a4);
		FloatMath2Collects.push(tex.uvconvert(u4, v4, reuseUV), quads);
		quads.add(x4); quads.add(y4);
	}

	@Override
	public void renderColoredTri(
			Vector4fc col1, Vector2fc pos1,
			Vector4fc col2, Vector2fc pos2,
			Vector4fc col3, Vector2fc pos3) {
		trisPlain.add(zindex);
		FloatMath2Collects.push(col1, trisPlain); FloatMath2Collects.push(pos1, trisPlain);
		FloatMath2Collects.push(col2, trisPlain); FloatMath2Collects.push(pos2, trisPlain);
		FloatMath2Collects.push(col3, trisPlain); FloatMath2Collects.push(pos3, trisPlain);
	}

	@Override
	public void renderColoredQuad(Vector4fc col1, Vector2fc pos1,
			Vector4fc col2, Vector2fc pos2,
			Vector4fc col3, Vector2fc pos3,
			Vector4fc col4, Vector2fc pos4) {
		quadsPlain.add(zindex);
		FloatMath2Collects.push(col1, quadsPlain); FloatMath2Collects.push(pos1, quadsPlain);
		FloatMath2Collects.push(col2, quadsPlain); FloatMath2Collects.push(pos2, quadsPlain);
		FloatMath2Collects.push(col3, quadsPlain); FloatMath2Collects.push(pos3, quadsPlain);
		FloatMath2Collects.push(col4, quadsPlain); FloatMath2Collects.push(pos4, quadsPlain);
	}

	@Override
	public void renderColoredQuad(
			float r1, float g1, float b1, float a1, float x1, float y1,
			float r2, float g2, float b2, float a2, float x2, float y2,
			float r3, float g3, float b3, float a3, float x3, float y3,
			float r4, float g4, float b4, float a4, float x4, float y4) {
		quadsPlain.add(zindex);
		quadsPlain.add(r1);quadsPlain.add(g1);quadsPlain.add(b1);quadsPlain.add(a1); quadsPlain.add(x1);quadsPlain.add(y1);
		quadsPlain.add(r2);quadsPlain.add(g2);quadsPlain.add(b2);quadsPlain.add(a2); quadsPlain.add(x2);quadsPlain.add(y2);
		quadsPlain.add(r3);quadsPlain.add(g3);quadsPlain.add(b3);quadsPlain.add(a3); quadsPlain.add(x3);quadsPlain.add(y3);
		quadsPlain.add(r4);quadsPlain.add(g4);quadsPlain.add(b4);quadsPlain.add(a4); quadsPlain.add(x4);quadsPlain.add(y4);
	}

	@Override
	public void renderCircle(float x, float y, float radius, float r, float g, float b, float a) {
		circles.add(zindex);
		circles.add(r); circles.add(g); circles.add(b); circles.add(a);
		circles.add(radius); circles.add(x); circles.add(y);
	}

	@Override
	public void fillCircle(float x, float y, float radius, float r, float g, float b, float a) {
		circlesFill.add(zindex);
		circlesFill.add(r); circlesFill.add(g); circlesFill.add(b); circlesFill.add(a);
		circlesFill.add(radius); circlesFill.add(x); circlesFill.add(y);
	}

	private float zindex = 0;
	@Override
	public void zindex(float z) {
		zindex = z;
	}
	
}