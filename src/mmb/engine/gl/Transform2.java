/**
 * 
 */
package mmb.engine.gl;

import org.joml.Matrix2f;
import org.joml.Matrix2fc;
import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * @author oskar
 *
 */
public class Transform2 {
	/**
	 * Creates an identity transform
	 */
	public Transform2() {}
	
	/**
	 * Creates an offset transform
	 * @param offset offset from origin
	 */
	public Transform2(Vector2fc offset) {
		this.offset.set(offset);
	}
	
	public Transform2(Matrix2fc mat) {
		this.mat.set(mat);
	}
	
	public Transform2(Vector2fc offset, Matrix2fc mat) {
		this.offset.set(offset);
		this.mat.set(mat);
	}
	
	public final Matrix2f mat = new Matrix2f();
	public final Vector2f offset = new Vector2f();
	
	/**
	 * @param tf destination
	 * @return destination transform
	 */
	public Transform2 inverse(Transform2 tf) {
		mat.invert(tf.mat);
		tf.mat.transform(offset, tf.offset);
		return tf;
	}
}
