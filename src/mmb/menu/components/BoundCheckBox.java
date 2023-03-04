/**
 * 
 */
package mmb.menu.components;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import mmb.NN;
import mmb.Nil;
import mmb.data.variables.ListenableBoolean;

/**
 * A checkbox which is connected to a variable
 * @author oskar
 */
public class BoundCheckBox extends JCheckBox {
	private static final long serialVersionUID = -490812297506623203L;
	
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
	/** Creates a checkbox without data */
	public BoundCheckBox() {
		super();
		initialize();
	}
	/**
	 * Creates a checkbox specified by an {@code Action}
	 * @param a specifies this checkbox
	 */
	public BoundCheckBox(Action a) {
		super(a);
		initialize();
	}
	/**
	 * Creates a checkbox with an icon
	 * @param icon icon to use
	 */
	public BoundCheckBox(Icon icon) {
		super(icon);
		initialize();
	}
	/**
	 * Creates a checkbox with an icon and a text
	 * @param text text to use
	 * @param icon icon to use
	 */
	public BoundCheckBox(String text, Icon icon) {
		super(text, icon);
		initialize();
	}
	/**
	 * Creates a checkbox with a text
	 * @param text text to use
	 */
	public BoundCheckBox(String text) {
		super(text);
		initialize();
	}
	
}
