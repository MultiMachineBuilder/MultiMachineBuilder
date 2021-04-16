/**
 * 
 */
package mmb.WORLD.gui;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

import mmb.ERRORS.UndeclarableThrower;

/**
 * @author oskar
 *
 */
public interface Variable<T> {
	/**
	 * @return the value of the variable
	 */
	public T get();
	/**
	 * 
	 * @param value
	 */
	public void set(T value);
	
	public static <U> Variable<U> delegate(Supplier<U> getter, Consumer<U> setter){
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
	
	public static <U> Variable<U> delegate(Field field, Object obj){
		return new Variable<U>(){

			@SuppressWarnings("unchecked")
			@Override
			public U get() {
				try {
					return (U) field.get(obj);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					UndeclarableThrower.shoot(e);
					return null; //unreachable, but required
				}
			}

			@Override
			public void set(U value) {
				try {
					field.set(obj, value);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					UndeclarableThrower.shoot(e);
				}
			}
			
		};
	}
}
