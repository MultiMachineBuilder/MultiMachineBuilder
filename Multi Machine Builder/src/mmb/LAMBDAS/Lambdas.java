/**
 * 
 */
package mmb.LAMBDAS;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author oskar
 *
 */
public class Lambdas {
	private Lambdas() {}
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
