/**
 * 
 */
package monniasza.collects;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 * A read-only wrapper of a list model
 * @author oskar
 * @param <E> type of elements
 */
public class ReadOnlyListModel<E> implements ListModel<E> {
	private final ListModel<? extends E> model;
	/** 
	 * Wraps a list model
	 * @param model list model to be wrapped
	 */
	public ReadOnlyListModel(ListModel<? extends E> model) {
		this.model = model;
	}
	@Override
	public void addListDataListener(@SuppressWarnings("null") ListDataListener arg0) {
		model.addListDataListener(arg0);
	}

	@Override
	public E getElementAt(int arg0) {
		return model.getElementAt(arg0);
	}

	@Override
	public int getSize() {
		return model.getSize();
	}

	@Override
	public void removeListDataListener(@SuppressWarnings("null") ListDataListener arg0) {
		model.removeListDataListener(arg0);
	}

}
