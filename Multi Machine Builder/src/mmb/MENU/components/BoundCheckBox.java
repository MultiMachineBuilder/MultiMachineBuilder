/**
 * 
 */
package mmb.MENU.components;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;

import mmb.BooleanConsumer;
import mmb.DATA.variables.ListenerBooleanVariable;

/**
 * @author oskar
 * A checkbox which is connected to a variable
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
	
	private transient ListenerBooleanVariable bvar;
	@Nonnull private transient BooleanConsumer update = this::setSelected;
	public void setVariable(@Nullable ListenerBooleanVariable var) {
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
	public BoundCheckBox() {
		super();
		initialize();
	}
	public BoundCheckBox(Action a) {
		super(a);
		initialize();
	}
	public BoundCheckBox(Icon icon, boolean selected) {
		super(icon, selected);
		initialize();
	}
	public BoundCheckBox(Icon icon) {
		super(icon);
		initialize();
	}
	public BoundCheckBox(String text, boolean selected) {
		super(text, selected);
		initialize();
	}
	public BoundCheckBox(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
		initialize();
	}
	public BoundCheckBox(String text, Icon icon) {
		super(text, icon);
		initialize();
	}
	public BoundCheckBox(String text) {
		super(text);
		initialize();
	}
	
}
