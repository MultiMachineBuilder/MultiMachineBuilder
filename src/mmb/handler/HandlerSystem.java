package mmb.handler;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;

import com.google.common.collect.Iterators;

import mmb.annotations.Nil;
import mmb.content.modular.cover.Cover;
import mmb.engine.worlds.world.World;

public final class HandlerSystem {
	public final World world;
	public HandlerSystem(World world) {
		this.world = world;
	}
	
	Map<HandlerKey, List<Cover>> map;
	private Map<HandlerKey, HandlerMappedCoverList> handlerMap = new WeakHashMap<>();
	public List<Cover> getCoverList(HandlerKey key){
		HandlerMappedCoverList mapper = handlerMap.get(key);
		if(mapper != null) return mapper;
		mapper = new HandlerMappedCoverList(this, key);
		handlerMap.put(key, mapper);
		return mapper;
	}
	List<Cover> makeList(HandlerKey key){
		List<Cover> list = map.get(key);
		if(list != null) return list;
		list = new ArrayList<>();
		map.put(key, list);
		return list;
	}

}

class HandlerMappedCoverList implements List<Cover>{
	private final HandlerSystem system;
	private final HandlerKey key;
	@Nil List<Cover> referenceList;
	
	public HandlerMappedCoverList(HandlerSystem system, HandlerKey key) {
		super();
		this.system = system;
		this.key = key;
		this.referenceList = system.map.get(key);
	}

	@Override
	public int size() {
		if(referenceList == null) return 0;
		return referenceList.size();
	}

	@Override
	public Cover get(int index) {
		if(referenceList == null) throw new IndexOutOfBoundsException("List of covers not available");
		return referenceList.get(index);
	}

	@Override
	public boolean isEmpty() {
		if(referenceList == null) return true;
		return referenceList.isEmpty();
	}

	@Override
	public boolean contains(@Nil Object o) {
		if(referenceList == null) return false;
		return referenceList.contains(o);
	}

	@Override
	public Iterator<Cover> iterator() {
		if(referenceList == null) return Iterators.forArray();
		return referenceList.iterator();
	}

	@Override
	public Object[] toArray() {
		if(referenceList == null) return new Object[0];
		return referenceList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		if(referenceList != null) referenceList.toArray(a);
		return a;
	}

	@Override
	public boolean add(Cover e) {
		return makeList().add(e);
	}

	@Override
	public boolean remove(Object o) {
		if(referenceList == null) return false;
		boolean result = referenceList.remove(o);
		boolean isEmpty = referenceList.isEmpty();
		onDelete();
		return result;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if(referenceList == null) return c.isEmpty();
		return referenceList.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Cover> list) {
		return makeList().addAll(list);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Cover> list) {
		return makeList().addAll(index, list);
	}

	@Override
	public boolean removeAll(Collection<?> list) {
		if(referenceList == null) return false;
		boolean result = referenceList.removeAll(list);
		onDelete();
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> list) {
		if(referenceList == null) return false;
		boolean result = referenceList.retainAll(list);
		onDelete();
		return result;
	}

	@Override
	public void clear() {
		//Delete the mapping
		system.map.remove(key);
		referenceList = null;
	}

	@Override
	public Cover set(int index, Cover element) {
		makeList().set(index, element);
	}

	@Override
	public void add(int index, Cover element) {
		makeList().add(index, element);
	}

	@Override
	public Cover remove(int index) {
		if(referenceList == null) throw new IndexOutOfBoundsException("List of covers not available");
		Cover result = referenceList.remove(index);
		onDelete();
		return result;
	}

	@Override
	public int indexOf(@Nil Object o) {
		if(referenceList == null) return -1;
		return referenceList.indexOf(o);
	}

	@Override
	public int lastIndexOf(@Nil Object o) {
		if(referenceList == null) return -1;
		return referenceList.indexOf(o);
	}

	@Override
	public ListIterator<Cover> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<Cover> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cover> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private List<Cover> makeList() {
		if(referenceList != null) return referenceList;
		return referenceList = system.makeList(key);
	}
	private void onDelete() {
		boolean isEmpty = referenceList.isEmpty();
		if(isEmpty) {
			//Delete the mapping
			system.map.remove(key);
			referenceList = null;
		}
	}
}