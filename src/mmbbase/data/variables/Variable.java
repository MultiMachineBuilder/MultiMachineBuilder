/**
 * 
 */
package mmb.data.variables;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

/**
 * @author oskar
 * @param <T> type of variable
 *
 */
public interface Variable<T> extends Property<T>{
	/**
	 * 
	 * @param value
	 */
	public void set(T value);
	
	public static @Nonnull <U> Variable<U> delegate(Supplier<U> getter, Consumer<U> setter){
		return new Variable<U>() {

			@Override
			public U get() {
				return getter.get();
			}

			@Override
			public void set(U value) {
				setter.accept(value);
			}
		};
	}
	
	public static @Nonnull <U> Variable<U> ofArraySlot(int slot, U[] array){
		return delegate(() -> array[slot], val -> array[slot] = val);
	}
	
	public static @Nonnull <U> Variable<U> ofListSlot(int slot, List<U> array){
		return delegate(() -> array.get(slot), val -> array.set(slot, val));
	}
}
