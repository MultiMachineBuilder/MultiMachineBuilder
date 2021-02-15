/**
 * 
 */
package mmb.RUNTIME;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author oskar
 * The constructor was made private to enforce use of factory method
 */
public class SubscribeList<T> {
	private Set<Consumer<T>> handlers = new HashSet<>();
	public void add(Consumer<T> action) {
		handlers.add(action);
	}
	public void remove(Consumer<T> action) {
		handlers.remove(action);
	}
	
	public static class Manager<T>{
		public final SubscribeList<T> list = new SubscribeList<>();
		private Manager() {}
		public void run(T value) {
			list.handlers.forEach((h) -> h.accept(value));
		}
	}
	private SubscribeList() {}
	public Manager<T> create() {
		return new Manager<>();
	}
}
