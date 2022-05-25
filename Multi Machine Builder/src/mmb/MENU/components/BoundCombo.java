/**
 * 
 */
package mmb.MENU.components;

import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JComboBox;

import mmb.DATA.variables.ListenableValue;

/**
 * @author oskar
 *
 */
public class BoundCombo<E> extends JComboBox<E> {
	private static final long serialVersionUID = -3081047509881544718L;
	private transient ListenableValue<E> bvar;
	@Nonnull private transient Consumer<E> update = this::setSelectedItem;
	private boolean valueChangeUnderway = false;
	public void setVariable(@Nullable ListenableValue<E> var) {
		if(var != null) setSelectedItem(var.get());
		if(bvar != null) bvar.remove(update);
		bvar = var;
		if(bvar != null) bvar.add(update);
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
