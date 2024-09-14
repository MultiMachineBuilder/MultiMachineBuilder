/**
 * 
 */
package mmb.data.variables;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import mmb.data.reactive.ListenableProperty;

/**
 * A variable which notifies listeners of its value changes
 * @author oskar
 * @param <T> type of values
 */
public class ListenableValue<T> extends DataValue<T> implements ListenableProperty<T>{
	/**
	 * Creates a new listenable variable
	 * @param data initial value
	 */
	public ListenableValue(T data) {
		super(data);
	}
	
	@Override
	public void set(T newValue) {
		T old = get();
		for(Consumer<? super T> c: oldlisteners) c.accept(old);
		for(Consumer<? super T> c: listeners) c.accept(newValue);
		super.set(newValue);
	}
	
	//New values
	private List<Consumer<? super T>> listeners = new ArrayList<>();
	@Override
	public boolean listenadd(Consumer<? super T> arg0) {
		return listeners.add(arg0);
	}
	@Override
	public boolean unlistenadd(Consumer<? super T> arg0) {
		return listeners.remove(arg0);
	}
	
	//Old values
	private List<Consumer<? super T>> oldlisteners = new ArrayList<>();
	@Override
	public boolean listenrem(Consumer<? super T> listener) {
		return oldlisteners.add(listener);
	}
	@Override
	public boolean unlistenrem(Consumer<? super T> listener) {
		return oldlisteners.remove(listener);
	}	
}
