/**
 * 
 */
package mmb.menu.components;

import java.awt.Color;
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
		Component c = renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if(isSelected) {
			c.setBackground(list.getSelectionBackground());
			c.setForeground(list.getSelectionForeground());
		}else {
			c.setBackground(new Color(204, 204, 204));
			c.setForeground(list.getForeground());
		}
		return c;
	}
}
