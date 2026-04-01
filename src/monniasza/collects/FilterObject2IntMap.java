package monniasza.collects;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;

public class FilterObject2IntMap<K> implements Object2IntMap<K> {
	private final Predicate<K> filter;
	private final Class<K> type;
	private final Object2IntMap<K> underlying;

	public FilterObject2IntMap(Predicate<K> filter, Class<K> type, Object2IntMap<K> underlying) {
		super();
		Objects.requireNonNull(filter, "filter is null");
		Objects.requireNonNull(type, "type is null");
		Objects.requireNonNull(underlying, "underlying is null");
		this.filter = filter;
		this.type = type;
		this.underlying = underlying;
	}

	private boolean test(Object key) {
		if(!type.isInstance(key)) return false;
		return filter.test((K) key);
	}
	
	@Override
	public int getInt(Object key) {
		if()
		return 0;
	}

	@Override
	public boolean isEmpty() {
		for(K key: keySet()) {
			
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends Integer> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void defaultReturnValue(int rv) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int defaultReturnValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ObjectSet<Entry<K>> object2IntEntrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectSet<K> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntCollection values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(int value) {
		// TODO Auto-generated method stub
		return false;
	}
}
