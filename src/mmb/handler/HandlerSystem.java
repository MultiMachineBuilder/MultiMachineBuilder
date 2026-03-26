package mmb.handler;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.WeakHashMap;

import mmb.annotations.Nil;
import mmb.content.modular.cover.Cover;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.worlds.world.World;

/**
 * Manages handlers in a world.
 * Covers are from inner to outer
 */
public final class HandlerSystem {
	public final World world;

	Map<HandlerKey, List<Cover>> map = new WeakHashMap<>();
	private final Map<HandlerKey, HandlerMappedCoverList> handlerMap = new WeakHashMap<>();

	public HandlerSystem(World world) {
		this.world = world;
	}

	/**
	 * Gets a list of covers for given block and side. The list is linked to a location so any changes to given location are in the list and vice versa.
	 * @param key handler key
	 * @return a list of covers for given location
	 */
	public List<Cover> getCoverList(HandlerKey key) {
		Objects.requireNonNull(key, "key is null");
		
		HandlerMappedCoverList mapper = handlerMap.get(key);
		if (mapper != null) return mapper;

		mapper = new HandlerMappedCoverList(this, key);
		handlerMap.put(key, mapper);
		return mapper;
	}

	List<Cover> makeList(HandlerKey key) {
		List<Cover> list = map.get(key);
		if (list != null) return list;

		list = new ArrayList<>();
		map.put(key, list);
		return list;
	}
	
	/**
	 * Gets a handler for a block, wrapped with target's covers and potentially source's covers.
	 * @param target target block
	 * @param source source block or null
	 * @param id handler id
	 * @throws NullPointerException when {@code target} is null
	 * @throws NullPointerException when {@code id} is null
	 * @return a handler for a given target block, wrapped with covers from first the target, then from source
	 */
	@Nil public Object getHandler(HandlerKey target, @Nil HandlerKey source, String id) {
		Objects.requireNonNull(target, "target is null");
		Objects.requireNonNull(id, "id is null");
		
		@Nil Object handler = null;
		
		//Get the raw handler
		BlockEntry block = world.get(target.x(), target.y());
		BlockType type = block.itemType();
		HandlerGetter getter = HandlerSystems.handlerGetters.get(new HandlerAssignment(id, type));
		if(getter != null) handler = getter.getHandler(target, block);
		
		
		//Wrap the target block with the covers
		var coversTarget = getCoverList(target);
		for(var cover: coversTarget) {
			handler = cover.getHandlerToBlock(id, target, handler);
		}
		
		//Wrap the source block with covers. Iteration order is reversed.
		if(source != null) {
			List<Cover> coversSource = getCoverList(source);
			for(int i = coversSource.size() - 1; i >= 0; i--) {
				Cover cover = coversSource.get(i);
				handler = cover.getHandlerFromBlock(id, target, handler);
			}
		}
		
		return handler;
	}
}

final class HandlerMappedCoverList extends AbstractList<Cover> {
	private final HandlerSystem system;
	private final HandlerKey key;

	/** Start offset into the real list */
	private final int offset;

	/**
	 * Length of this view.
	 * -1 means "dynamic full tail from offset to backing size".
	 */
	private final int length;

	@Nil
	private List<Cover> referenceList;

	public HandlerMappedCoverList(HandlerSystem system, HandlerKey key) {
		this(system, key, 0, -1);
	}

	private HandlerMappedCoverList(HandlerSystem system, HandlerKey key, int offset, int length) {
		this.system = system;
		this.key = key;
		this.offset = offset;
		this.length = length;
		this.referenceList = system.map.get(key);
	}

	// ------------------------------------------------------------
	// Internal helpers
	// ------------------------------------------------------------

	@Nil
	private List<Cover> rawList() {
		if (referenceList == null) {
			referenceList = system.map.get(key);
		}
		return referenceList;
	}

	private List<Cover> makeList() {
		List<Cover> list = rawList();
		if (list != null) return list;
		referenceList = system.makeList(key);
		return referenceList;
	}

	private int realSize() {
		List<Cover> list = rawList();
		int backingSize = (list == null ? 0 : list.size());

		if (offset > backingSize) return 0;

		int available = backingSize - offset;
		return (length < 0) ? available : Math.min(length, available);
	}

	private int toBackingIndex(int index) {
		int size = realSize();
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		return offset + index;
	}

	private int toBackingIndexForAdd(int index) {
		int size = realSize();
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		return offset + index;
	}

	private void onDelete() {
		List<Cover> list = rawList();
		if (list != null && list.isEmpty()) {
			system.map.remove(key);
			referenceList = null;
		}
	}

	// ------------------------------------------------------------
	// Read methods
	// ------------------------------------------------------------

	@Override
	public int size() {
		return realSize();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public Cover get(int index) {
		List<Cover> list = rawList();
		if (list == null) throw new IndexOutOfBoundsException("List of covers not available");
		return list.get(toBackingIndex(index));
	}

	@Override
	public boolean contains(@Nil Object o) {
		return indexOf(o) >= 0;
	}

	@Override
	public int indexOf(@Nil Object o) {
		int size = realSize();
		for (int i = 0; i < size; i++) {
			if (java.util.Objects.equals(get(i), o)) return i;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(@Nil Object o) {
		for (int i = realSize() - 1; i >= 0; i--) {
			if (java.util.Objects.equals(get(i), o)) return i;
		}
		return -1;
	}

	@Override
	public Object[] toArray() {
		int size = realSize();
		Object[] out = new Object[size];
		for (int i = 0; i < size; i++) out[i] = get(i);
		return out;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		int size = realSize();
		if (a.length < size) {
			a = java.util.Arrays.copyOf(a, size);
		}
		for (int i = 0; i < size; i++) {
			@SuppressWarnings("unchecked")
			T value = (T) get(i);
			a[i] = value;
		}
		if (a.length > size) a[size] = null;
		return a;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) return false;
		}
		return true;
	}

	// ------------------------------------------------------------
	// Write methods
	// ------------------------------------------------------------

	@Override
	public boolean add(Cover e) {
		add(size(), e);
		return true;
	}

	@Override
	public void add(int index, Cover element) {
		List<Cover> list = makeList();
		list.add(toBackingIndexForAdd(index), element);
	}

	@Override
	public boolean addAll(Collection<? extends Cover> c) {
		return addAll(size(), c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Cover> c) {
		if (c.isEmpty()) return false;
		List<Cover> list = makeList();
		return list.addAll(toBackingIndexForAdd(index), c);
	}

	@Override
	public Cover set(int index, Cover element) {
		List<Cover> list = makeList();
		return list.set(toBackingIndex(index), element);
	}

	@Override
	public boolean remove(Object o) {
		int idx = indexOf(o);
		if (idx < 0) return false;
		remove(idx);
		return true;
	}

	@Override
	public Cover remove(int index) {
		List<Cover> list = rawList();
		if (list == null) throw new IndexOutOfBoundsException("List of covers not available");
		Cover result = list.remove(toBackingIndex(index));
		onDelete();
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		ListIterator<Cover> it = listIterator();
		while (it.hasNext()) {
			if (c.contains(it.next())) {
				it.remove();
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean changed = false;
		ListIterator<Cover> it = listIterator();
		while (it.hasNext()) {
			if (!c.contains(it.next())) {
				it.remove();
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public void clear() {
		int size = size();
		if (size == 0) return;

		List<Cover> list = rawList();
		if (list == null) return;

		int from = offset;
		int to = offset + size;
		list.subList(from, to).clear();
		onDelete();
	}

	// ------------------------------------------------------------
	// Iterators
	// ------------------------------------------------------------

	@Override
	public Iterator<Cover> iterator() {
		return listIterator();
	}

	@Override
	public ListIterator<Cover> listIterator() {
		return new HandlerMappedCoverListIterator(this, 0);
	}

	@Override
	public ListIterator<Cover> listIterator(int index) {
		int size = size();
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		return new HandlerMappedCoverListIterator(this, index);
	}

	// ------------------------------------------------------------
	// Sublist
	// ------------------------------------------------------------

	@Override
	public List<Cover> subList(int fromIndex, int toIndex) {
		int size = size();
		if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
			throw new IndexOutOfBoundsException(
				"fromIndex=" + fromIndex + ", toIndex=" + toIndex + ", size=" + size
			);
		}
		return new HandlerMappedCoverList(system, key, offset + fromIndex, toIndex - fromIndex);
	}
}

final class HandlerMappedCoverListIterator implements ListIterator<Cover> {
	private final HandlerMappedCoverList list;

	/** Cursor points between elements, as in ListIterator */
	private int cursor;

	/** Last returned element index in view coordinates, or -1 */
	private int lastRet = -1;

	HandlerMappedCoverListIterator(HandlerMappedCoverList list, int index) {
		this.list = list;
		this.cursor = index;
	}

	@Override
	public boolean hasNext() {
		return cursor < list.size();
	}

	@Override
	public Cover next() {
		if (!hasNext()) throw new NoSuchElementException();
		Cover result = list.get(cursor);
		lastRet = cursor;
		cursor++;
		return result;
	}

	@Override
	public boolean hasPrevious() {
		return cursor > 0;
	}

	@Override
	public Cover previous() {
		if (!hasPrevious()) throw new NoSuchElementException();
		cursor--;
		Cover result = list.get(cursor);
		lastRet = cursor;
		return result;
	}

	@Override
	public int nextIndex() {
		return cursor;
	}

	@Override
	public int previousIndex() {
		return cursor - 1;
	}

	@Override
	public void remove() {
		if (lastRet < 0) throw new IllegalStateException();
		list.remove(lastRet);
		if (lastRet < cursor) cursor--;
		lastRet = -1;
	}

	@Override
	public void set(Cover e) {
		if (lastRet < 0) throw new IllegalStateException();
		list.set(lastRet, e);
	}

	@Override
	public void add(Cover e) {
		list.add(cursor, e);
		cursor++;
		lastRet = -1;
	}
}