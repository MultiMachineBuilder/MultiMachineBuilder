/**
 * 
 */
package mmb.data.reactive;

import java.util.function.Consumer;

import mmb.data.variables.Property;

/**
 * A variable or a property which can be listened
 * @author oskar
 * @param <T> type of data
 */
public interface ListenableProperty<T> extends Property<T>{
	/**
	 * Listens to new values
	 * @param listener listener to add
	 * @return was new consume added?
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public boolean listenadd(Consumer<? super T> listener);
	/**
	 * Unlistens from new values
	 * @param listener listener to add
	 * @return was consumer removed?
	 * @see java.util.Set#remove(java.lang.Object)
	 */
	public boolean unlistenadd(Consumer<? super T> listener);
	/**
	 * Listens to old values
	 * @param listener listener to add
	 * @return was new consume added?
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public boolean listenrem(Consumer<? super T> listener);
	/**
	 * Unlistens from old values
	 * @param listener listener to add
	 * @return was consumer removed?
	 * @see java.util.Set#remove(java.lang.Object)
	 */
	public boolean unlistenrem(Consumer<? super T> listener);
}
