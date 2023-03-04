/**
 * 
 */
package mmb.menu.components;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import mmb.NN;
import mmb.Nil;
import mmb.content.modular.gui.SafeCloseable;
import mmb.data.variables.ListenableBoolean;

/**
 * A checkbox menu item which is connected to a variable
 * @author oskar
 */
public class BoundCheckBoxMenuItem extends JCheckBoxMenuItem implements SafeCloseable {
	private static final long serialVersionUID = 6007934685540436786L;
	private boolean valueChangeUnderway = false;
	@Override
	public void setSelected(boolean arg0) {
		if(valueChangeUnderway) return;
		valueChangeUnderway = true;
		super.setSelected(arg0);
		if(bvar != null) bvar.setValue(isSelected());
		valueChangeUnderway = false;
	}
	
	private transient ListenableBoolean bvar;
	@NN private transient BooleanConsumer update = this::setSelected;
	/**
	 * Sets the state variable
	 * @param var new state variable
	 */
	public void setVariable(@Nil ListenableBoolean var) {
		if(bvar != null) bvar.remove(update);
		bvar = var;
		if(var != null) setSelected(var.getValue());
		if(bvar != null) bvar.add(update);
	}
	/** @return current state variable */
	public ListenableBoolean getVariable() {
		return bvar;
	}
	private void initialize() {
		setRolloverEnabled(false);
		addChangeListener(e -> {
			if(valueChangeUnderway) return;
			valueChangeUnderway = true;
			if(bvar != null) bvar.setValue(isSelected());
			valueChangeUnderway = false;
		});
	}
	
	/** Creates an empty check box menu item */
	public BoundCheckBoxMenuItem() {
		super();
		initialize();
	}
	/**
	 * Creates a check box menu item specified by the {@code Action}
	 * @param a specifies this check box menu item
	 */
	public BoundCheckBoxMenuItem(Action a) {
		super(a);
		initialize();
	}
	/**
	 * Creates a check box menu item wit an icon
	 * @param icon icon to use
	 */
	public BoundCheckBoxMenuItem(Icon icon) {
		super(icon);
		initialize();
	}
	/**
	 * Creates a check box menu item with an icon and a text
	 * @param text text to use
	 * @param icon icon to use
	 */
	public BoundCheckBoxMenuItem(String text, Icon icon) {
		super(text, icon);
		initialize();
	}
	/**
	 * Creates a check box menu item with a text
	 * @param text text to use
	 */
	public BoundCheckBoxMenuItem(String text) {
		super(text);
		initialize();
	}
	@Override
	public void close() {
		setVariable(null);
	}
	
}
