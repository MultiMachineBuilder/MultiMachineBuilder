/**
 * 
 */
package mmb.LAMBDAS;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public class Consumers {
	private Consumers() {}
	@Nonnull private static final Consumer<?> nothing = v -> {};
	@SuppressWarnings("unchecked")
	@Nonnull public static <T> Consumer<T> doNothing(){
		return (Consumer<T>) nothing;
	}
}
