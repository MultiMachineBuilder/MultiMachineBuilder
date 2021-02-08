/**
 * 
 */
package mmb.DATA;

import java.util.function.Consumer;
import java.util.function.Supplier;

import io.vavr.Value;

/**
 * @author oskar
 * @param <T> data type
*/
 
public interface GetterSetter<T> extends Supplier<T>, Consumer<T>{
	public void set(T value);

	@Override
	default void accept(T value) {
		set(value);
	}
}
