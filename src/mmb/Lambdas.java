/**
 * 
 */
package mmb;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public class Lambdas {
	private Lambdas() {}
	@Nonnull private static final Consumer<?> nothing = v -> {};
	/**
	 * @param <T> type of the input
	 * @return a consumer which does nothing.
	 * @apiNote The returned consumers are the same object
	 */
	@SuppressWarnings("unchecked")
	@Nonnull public static <T> Consumer<T> doNothing(){
		return (Consumer<T>) nothing;
	}
	
	/**
	 * Creates a {@link Runnable} which invokes the {@code action} with {@code value}
	 * @param <T> the data type
	 * @param action consumer to insert into
	 * @param value value to insert
	 * @return the runnable object
	 */
	public static <T> Runnable loadValue(Consumer<T> action, T value) {
		return () -> action.accept(value);
	}
	
	/**
	 * @param <T> type of values
	 * @param sup
	 * @return a lazily evaluated supplier
	 */
	public static <T> Supplier<T> lazy(Supplier<T> sup){
		return new Supplier<T>() {
			boolean isValid;
			T value;
			@Override
			public T get() {
				if(!isValid) {
					value = sup.get();
					isValid = true;
				}
				return value;
			}
			
		};
	}
}
