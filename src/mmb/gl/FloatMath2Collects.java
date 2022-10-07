/**
 * 
 */
package mmb.gl;

import javax.annotation.Nonnull;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.floats.FloatIterator;

/**
 * @author oskar
 *
 */
public class FloatMath2Collects {
	//Write methods
	public static void push(Vector2fc vec, FloatCollection collect) {
		collect.add(vec.x());
		collect.add(vec.y());
	}
	public static void push(Vector3fc vec, FloatCollection collect) {
		collect.add(vec.x()); 
		collect.add(vec.y());
		collect.add(vec.z());
	}
	public static void push(Vector4fc vec, FloatCollection collect) {
		collect.add(vec.x()); 
		collect.add(vec.y());
		collect.add(vec.z());
		collect.add(vec.w());
	}
	
	//Read methods
	@Nonnull public static Vector2f pull(Vector2f vec, FloatIterator iter) {
		vec.x = iter.nextFloat();
		vec.y = iter.nextFloat();
		return vec;
	}
	@Nonnull public static Vector3f pull(Vector3f vec, FloatIterator iter) {
		vec.x = iter.nextFloat();
		vec.y = iter.nextFloat();
		vec.z = iter.nextFloat();
		return vec;
	}
	@Nonnull public static Vector4f pull(Vector4f vec, FloatIterator iter) {
		vec.x = iter.nextFloat();
		vec.y = iter.nextFloat();
		vec.z = iter.nextFloat();
		vec.w = iter.nextFloat();
		return vec;
	}
}
