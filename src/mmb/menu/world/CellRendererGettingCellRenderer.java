/**
 * 
 */
package mmb.menu.world;

import java.awt.Component;
import java.util.function.Function;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * @author oskar
 * A cell renderer which renders entries, whose have their own cell renderers
 * @param <E> type of data
 */
public class CellRendererGettingCellRenderer<E> implements ListCellRenderer<E> {
	private final Function<E, ListCellRenderer<E>> getter;
	public CellRendererGettingCellRenderer(Function<E, ListCellRenderer<E>> getter) {
		this.getter = getter;
	}
	@Override
	public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected,
			boolean cellHasFocus) {
		ListCellRenderer<E> renderer = getter.apply(value);
		return renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	}
}
