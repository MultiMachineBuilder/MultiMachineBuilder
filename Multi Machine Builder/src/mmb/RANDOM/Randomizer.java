/**
 * 
 */
package mmb.RANDOM;

import java.util.Random;

import javax.annotation.Nonnull;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * @author oskar
 *
 */
public class Randomizer {
	@Nonnull private static final Random rnd = new Random();
	@Nonnull public static Vector2f random2f() {
		return new Vector2f(rnd.nextFloat(), rnd.nextFloat());
	}
	@Nonnull public static Vector3f random3f() {
		return new Vector3f(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat());
	}
	@Nonnull public static Vector4f random4f() {
		return new Vector4f(rnd.nextFloat(), rnd.nextFloat(),  rnd.nextFloat(), rnd.nextFloat());
	}
	@Nonnull public static Vector4f random4fp(float end) {
		return new Vector4f(rnd.nextFloat(), rnd.nextFloat(),  rnd.nextFloat(), end);
	}
}
