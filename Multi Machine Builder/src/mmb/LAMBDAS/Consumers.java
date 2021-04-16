/**
 * 
 */
package mmb.LAMBDAS;

import java.util.function.Consumer;

/**
 * @author oskar
 *
 */
public class Consumers {
	private static final Consumer<?> nothing = v -> {};
	@SuppressWarnings("unchecked")
	public static <T> Consumer<T> doNothing(){
		return (Consumer<T>) nothing;
	}
}
