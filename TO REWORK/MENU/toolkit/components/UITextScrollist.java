/**
 * 
 */
package mmb.MENU.toolkit.components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import mmb.MENU.toolkit.ComponentEventHandler;
import mmb.MENU.toolkit.UIComponent;
import mmb.MENU.toolkit.UIDefaults;
import mmb.MENU.toolkit.events.UIMouseEvent;

/**
 * @author oskar
 *
 */
public class UITextScrollist extends UIComponent {
	//EVENT HANDLERS
	public Color selColor = UIDefaults.selection;
	public IntConsumer selector = (i) -> {};
	public int selection = -1;
	public List<String> contents = new ArrayList<String>();
	/**
	 * Position progress from the start
	 */
	public int progress = 0;
	private double screenStartIndex = 0;
	private int fontHeight = 0;
	public int period = 0;
	public int separation = 2;
	private ComponentEventHandler ceh = new ScrollistCEH();
	@Override
	public void prepare(Graphics g) {
		fontHeight = g.getFontMetrics().getHeight();
		screenStartIndex = progress / fontHeight;
		period = separation + fontHeight;
	}

	@Override
	public void render(Graphics g) {
		drawBound(g);
		int sepHalf = separation / 2;
		for(int i = 0; i <= contents.size(); i++) {
			if(i >= 0 && i < contents.size()) {
				int pos = renderPositionOfIndex(i);
				int pos2 = 2+pos+(period/2);
				if(i == selection) {
					g.setColor(selColor);
					g.fillRect(1, pos+1, getWidth()-2, fontHeight);
				}
				g.setColor(foreground);
				g.drawString(contents.get(i), 2, pos2+sepHalf);
			}
		}
	}
	/**
	 * @param index
	 * @param element
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add(int index, String element) {
		contents.add(index, element);
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(String e) {
		return contents.add(e);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends String> c) {
		return contents.addAll(c);
	}

	/**
	 * @param index
	 * @param c
	 * @return
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	public boolean addAll(int index, Collection<? extends String> c) {
		return contents.addAll(index, c);
	}

	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void clear() {
		contents.clear();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return contents.contains(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> c) {
		return contents.containsAll(c);
	}


	/**
	 * @param arg0
	 * @see java.lang.Iterable#forEach(java.util.function.Consumer)
	 */
	public void forEach(Consumer<? super String> arg0) {
		contents.forEach(arg0);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public String get(int index) {
		return contents.get(index);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf(Object o) {
		return contents.indexOf(o);
	}

	/**
	 * @return
	 * @see java.util.List#isEmpty()
	 */
	public boolean isEmpty() {
		return contents.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.List#iterator()
	 */
	public Iterator<String> iterator() {
		return contents.iterator();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object o) {
		return contents.lastIndexOf(o);
	}

	/**
	 * @return
	 * @see java.util.List#listIterator()
	 */
	public ListIterator<String> listIterator() {
		return contents.listIterator();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#listIterator(int)
	 */
	public ListIterator<String> listIterator(int index) {
		return contents.listIterator(index);
	}

	/**
	 * @return
	 * @see java.util.Collection#parallelStream()
	 */
	public Stream<String> parallelStream() {
		return contents.parallelStream();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#remove(int)
	 */
	public String remove(int index) {
		return contents.remove(index);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		return contents.remove(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> c) {
		return contents.removeAll(c);
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.Collection#removeIf(java.util.function.Predicate)
	 */
	public boolean removeIf(Predicate<? super String> arg0) {
		return contents.removeIf(arg0);
	}

	/**
	 * @param operator
	 * @see java.util.List#replaceAll(java.util.function.UnaryOperator)
	 */
	public void replaceAll(UnaryOperator<String> operator) {
		contents.replaceAll(operator);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> c) {
		return contents.retainAll(c);
	}

	/**
	 * @param index
	 * @param element
	 * @return
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public String set(int index, String element) {
		return contents.set(index, element);
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int size() {
		return contents.size();
	}

	/**
	 * @param c
	 * @see java.util.List#sort(java.util.Comparator)
	 */
	public void sort(Comparator<? super String> c) {
		contents.sort(c);
	}

	/**
	 * @return
	 * @see java.util.List#spliterator()
	 */
	public Spliterator<String> spliterator() {
		return contents.spliterator();
	}

	/**
	 * @return
	 * @see java.util.Collection#stream()
	 */
	public Stream<String> stream() {
		return contents.stream();
	}

	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 * @see java.util.List#subList(int, int)
	 */
	public List<String> subList(int fromIndex, int toIndex) {
		return contents.subList(fromIndex, toIndex);
	}

	/**
	 * @return
	 * @see java.util.List#toArray()
	 */
	public String[] toArray() {
		return (String[]) contents.toArray();
	}

	/**
	 * @param a
	 * @return
	 * @see java.util.List#toArray(java.lang.Object[])
	 */
	public String[] toArray(String[] a) {
		return contents.toArray(a);
	}
	private class ScrollistCEH implements ComponentEventHandler{
		@Override
		public void press(UIMouseEvent e) {
			if(fontHeight == 0) return;
			int index;
			if(period == 0) {
				index = -1;
			}else {
				index = indexByPosition(e.p.y);
			}
			
			if(index >= 0) {
				selection = index;
				if(selector != null) selector.accept(index);
			}
		}
		
	}
	@Override
	public ComponentEventHandler getHandler() {
		return ceh;
	}
	
	public int renderPositionOfIndex(int index) {
		return (index*period)-progress;
	}
	public int indexByPosition(int pos) {
		/*
		 * (index*period)-progress = pos
		 * index*period = pos+progress
		 * index = (pos+progress)/period
		 */
		if(period == 0) return -1;
		return (pos+progress)/period;
	}

}
