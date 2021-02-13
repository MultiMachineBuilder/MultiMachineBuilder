/**
 * 
 */
package mmb.MENU.components;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;

import mmb.BooleanConsumer;
import mmb.DATA.variables.ListenerBooleanVariable;
import mmb.debug.Debugger;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * @author oskar
 *
 */
public class BoundCheckBoxMenuItem extends JCheckBoxMenuItem {
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
	
	private transient ListenerBooleanVariable bvar;
	private transient BooleanConsumer update = this::setSelected;
	public void setVariable(ListenerBooleanVariable var) {
		if(var != null) setSelected(var.getValue());
		if(bvar != null) bvar.remove(update);
		bvar = var;
		if(bvar != null) bvar.add(update);
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
	public BoundCheckBoxMenuItem() {
		super();
		initialize();
	}
	public BoundCheckBoxMenuItem(Action a) {
		super(a);
		initialize();
	}
	public BoundCheckBoxMenuItem(Icon icon) {
		super(icon);
		initialize();
	}
	public BoundCheckBoxMenuItem(String text, boolean b) {
		super(text, b);
		initialize();
	}
	public BoundCheckBoxMenuItem(String text, Icon icon, boolean b) {
		super(text, icon, b);
		initialize();
	}
	public BoundCheckBoxMenuItem(String text, Icon icon) {
		super(text, icon);
		initialize();
	}
	public BoundCheckBoxMenuItem(String text) {
		super(text);
		initialize();
	}
	
}
