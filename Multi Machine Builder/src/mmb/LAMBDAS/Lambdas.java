/**
 * 
 */
package mmb.LAMBDAS;

import java.util.function.Consumer;

/**
 * @author oskar
 *
 */
public class Lambdas {
	private Lambdas() {}
	public static <T> Runnable loadValue(Consumer<T> action, T value) {
		return () -> action.accept(value);
	}
}
