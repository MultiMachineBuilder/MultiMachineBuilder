/**
 * 
 */
package mmb.menu.components;

import java.util.function.Consumer;

import javax.swing.JComboBox;

import mmb.NN;
import mmb.Nil;
import mmb.data.variables.ListenableValue;

/**
 * A combo box bound to the variable
 * @author oskar
 * @param <E> type of values
 */
public class BoundCombo<E> extends JComboBox<E> {
	private static final long serialVersionUID = -3081047509881544718L;
	private transient ListenableValue<E> bvar;
	@NN private transient Consumer<E> update = this::setSelectedItem;
	private boolean valueChangeUnderway = false;
	/**
	 * Sets the databinding
	 * @param var databinding to use
	 */
	public void setVariable(@Nil ListenableValue<E> var) {
		if(var != null) setSelectedItem(var.get());
		if(bvar != null) bvar.unlistenadd(update);
		bvar = var;
		if(bvar != null) bvar.listenadd(update);
	}
	/** Creates a combo box */
	public BoundCombo() {
		super();
		addActionListener(e -> {
			if(valueChangeUnderway) return;
			valueChangeUnderway = true;
			if(bvar != null) bvar.set((E) getSelectedItem());
			valueChangeUnderway = false;
		});
	}	
}
