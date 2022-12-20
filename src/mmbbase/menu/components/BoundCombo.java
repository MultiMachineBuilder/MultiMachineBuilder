/**
 * 
 */
package mmbbase.menu.components;

import java.util.function.Consumer;

import javax.swing.JComboBox;

import mmb.NN;
import mmb.Nil;
import mmbbase.data.variables.ListenableValue;

/**
 * @author oskar
 *
 */
public class BoundCombo<E> extends JComboBox<E> {
	private static final long serialVersionUID = -3081047509881544718L;
	private transient ListenableValue<E> bvar;
	@NN private transient Consumer<E> update = this::setSelectedItem;
	private boolean valueChangeUnderway = false;
	public void setVariable(@Nil ListenableValue<E> var) {
		if(var != null) setSelectedItem(var.get());
		if(bvar != null) bvar.unlistenadd(update);
		bvar = var;
		if(bvar != null) bvar.listenadd(update);
	}
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
