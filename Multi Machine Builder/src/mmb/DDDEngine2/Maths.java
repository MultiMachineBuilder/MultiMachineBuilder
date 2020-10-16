/**
 * 
 */
package mmb.DDDEngine2;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * @author oskar
 *
 */
public class Maths {

	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f(	1,0,0,0,
										0,1,0,0,
										0,0,1,0,
										0,0,0,1);
		matrix.rotateX(rx); matrix.rotateY(ry); matrix.rotateZ(rz);
		matrix.scale(scale);
		matrix.translate(translation);
		return matrix;
	}
	/**
	 * Creates projection matrix, applied to all objects.
	 * @param a aspect ratio
	 * @param fov field of view
	 * @param Znear near plane
	 * @param Zfar far plane
	 * @return projection matrix
	 */
	public static Matrix4f projMatrix(float a, float fov, float Znear, float Zfar) {
		float zm = Zfar - Znear;
		float zp = Zfar + Znear;
		float x = (float) (1/(Math.tan(fov / 2)*a));
		float y = (float) (1/Math.tan(fov/2));
		float z = -zp/zm;
		float w = (-2*Zfar*Znear)/zm;
		return new Matrix4f(x, 0, 0, 0,
							0, y, 0, 0,
							0, 0, z, -1,
							0, 0, w, 0);
	}
	
	public static double shortToRad(short val) {
		return val*Math.PI / 32768;
	}
}
