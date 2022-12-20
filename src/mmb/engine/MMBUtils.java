/**
 * 
 */
package mmb.engine;

import java.util.function.Consumer;
import java.util.function.Supplier;

import mmb.NN;

/**
 * @author oskar
 *
 */
public class MMBUtils {
	private MMBUtils() {}
	//Casting
	/**
	 * Switches displayed class types. The types should be variant of the same class type
	 * @param <R> dest type
	 * @param cls class to cast
	 * @return a casted class
	 */
	@SuppressWarnings("unchecked")
	@NN public static <R> Class<R> classcast(Class<?> cls){
		return (Class<R>) cls;
	}
	/**
	 * WARNING: THIS METHOD IS UNSAFE AND MISUSE WILL RESULT IN HARD TO TRACE CASTING BUGS, so the name is very long
	 * @param in input
	 * @param <T> source type
	 * @param <U> destination type
	 * @return the same object (but it might fail)
	 * @throws ClassCastException randomly, when misused in various methods
	 */
	@SuppressWarnings("unchecked")
	public static <T, U> U thisIsAReallyLongNameUnsafeCast(T in) {
		return (U) in;
	}
	/**
	 * WARNING: THIS METHOD IS UNSAFE AND MISUSE WILL RESULT IN HARD TO TRACE CASTING BUGS, so the name is very long
	 * @param in input
	 * @param <T> source type
	 * @param <U> destination type
	 * @return the same object (but it might fail)
	 * @throws ClassCastException randomly, when misused in various methods
	 */
	@SuppressWarnings("unchecked")
	@NN public static <T, U> U thisIsAReallyLongNameUnsafeCastNN(T in) {
		return (U) in;
	}	
	
	//Boolean to value
	/**
	 * Bool-to-int conversion
	 * @param bool value to convert
	 * @return an integer representation of a boolean
	 */
	public static int bool2int(boolean bool) {
		return bool ? 1 : 0;
	}
	
	//Lambda expressions
	@NN private static final Consumer<?> nothing = v -> {};
	/**
	 * @param <T> type of the input
	 * @return a consumer which does nothing.
	 * @apiNote The returned consumers are the same object
	 */
	@SuppressWarnings("unchecked")
	@NN public static <T> Consumer<T> doNothing(){
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
		return new Supplier<>() {
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
	
	//Undeclared throws
	/**s
	 * Throws given exception without a declaration
	 * @param t the exception to throw
	 */
	public static void shoot(Throwable t) {
		MMBUtils.<RuntimeException>shoot0(t);
	}
	@SuppressWarnings("unchecked")
	private static <T extends Throwable> void shoot0(Throwable e) throws T {
		throw (T) e;
	}
}
