/**
 * 
 */
package mmb.COLLECTIONS;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 * @author oskar
 *
 */
public class ReadOnlyListModel<E> implements ListModel<E> {
	private final ListModel<E> model;
	public ReadOnlyListModel(ListModel model) {
		this.model = model;
	}
	@Override
	public void addListDataListener(ListDataListener arg0) {
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
	public void removeListDataListener(ListDataListener arg0) {
		model.removeListDataListener(arg0);
	}

}
