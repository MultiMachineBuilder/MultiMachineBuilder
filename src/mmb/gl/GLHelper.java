/**
 * 
 */
package mmb.gl;

import java.awt.Color;

import org.joml.*;
import org.lwjgl.opengl.GL11;

/**
 * @author oskar
 *
 */
public class GLHelper {
	public static void glColor(Vector4f vec) {
		GL11.glColor4f(vec.x, vec.y, vec.z, vec.w);
	}
	public static void glColor(Vector3f vec) {
		GL11.glColor3f(vec.x, vec.y, vec.z);
	}
	public static void glUV(Vector2f vec) {
		GL11.glTexCoord2f(vec.x, vec.y);
	}
	public static void glVertex(Vector2f vec) {
		GL11.glVertex2f(vec.x, vec.y);
	}
	public static void glVertex(Vector3f vec) {
		GL11.glVertex3f(vec.x, vec.y, vec.z);
	}
	public static void glVertex(Vector4f vec) {
		GL11.glVertex4f(vec.x, vec.y, vec.z, vec.w);
	}
	public static Vector4f color2vec(int r, int g, int b, int a, Vector4f vec) {
		vec.x = r / 255.0f;
		vec.y = g / 255.0f;
		vec.z = b / 255.0f;
		vec.w = a / 255.0f;
		return vec;
	}
	public static Vector4f color2vec(Color c, Vector4f vec) {
		return color2vec(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha(), vec);
	}
	public static Color vec2color(Vector4fc vec) {
		return new Color((int) (vec.x()*255), (int) (vec.y()*255), (int) (vec.z()*255), (int) (vec.w()*255));
	}
}
