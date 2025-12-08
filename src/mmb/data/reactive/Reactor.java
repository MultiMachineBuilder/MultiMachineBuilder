/**
 * 
 */
package mmb.data.reactive;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import mmb.annotations.NN;
import mmb.content.modular.gui.SafeCloseable;

/**
 * A reactor is a function-based listenable property
 * @author oskar
 * @param <Tin> type of the original property
 * @param <Tout> type of this reactor
 */
public class Reactor<Tin, Tout> implements ListenableProperty<Tout>, SafeCloseable{
	/** The underlying listenable property */
	@NN public final ListenableProperty<Tin> original;
	/** Function to transform original values with */
	@NN public final Function<Tin, Tout> fn;
	private Tout cache;
	
	/**
	 * Creates a reactor
	 * @param original the underlying listenable property
	 * @param fn function to transform original values with
	 */
	public Reactor(ListenableProperty<Tin> original, Function<Tin, Tout> fn) {
		this.original = original;
		this.fn = fn;
		setup();
	}
	
	@Override
	public Tout get() {
		return cache;
	}

	@Override
	public void close(){
		original.unlistenadd(listener);
	}
	/** Prepares this reactor to reuse after a closure */
	public void setup() {
		cache = fn.apply(original.get());
		original.listenadd(listener);
	}
	
	@NN private final Consumer<Tin> listener = this::listener;
	private void listener(Tin value) {
		for(Consumer<? super Tout> c: oldlisteners) c.accept(cache);
		cache = fn.apply(value);
		for(Consumer<? super Tout> c: listeners) c.accept(cache);
	}

	//New values
	private List<Consumer<? super Tout>> listeners = new ArrayList<>();
	@Override
	public boolean listenadd(Consumer<? super Tout> listener1) {
		return listeners.add(listener1);
	}
	@Override
	public boolean unlistenadd(Consumer<? super Tout> listener1) {
		return listeners.remove(listener1);
	}
	
	//Old values
	private List<Consumer<? super Tout>> oldlisteners = new ArrayList<>();
	@Override
	public boolean listenrem(Consumer<? super Tout> listener1) {
		return oldlisteners.add(listener1);
	}
	@Override
	public boolean unlistenrem(Consumer<? super Tout> listener1) {
		return oldlisteners.remove(listener1);
	}	
}
