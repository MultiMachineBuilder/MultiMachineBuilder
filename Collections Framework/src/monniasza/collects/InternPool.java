/**
 * 
 */
package monniasza.collects;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

import com.google.common.collect.Interner;

/**
 * @author oskar
 * @param <E> the type of interned objects
 * This class is an implementation of {@link Interner} using weak hash map
 */
public class InternPool<E> implements Interner<E> {
	private final Map<E, E> map = new WeakHashMap<>(); //the map is a weak referenced one to allow GC
	private final Function<E, E> identity = v -> v;
	@Override
	public E intern(E sample) {
		return map.computeIfAbsent(sample, identity);
	}
}
