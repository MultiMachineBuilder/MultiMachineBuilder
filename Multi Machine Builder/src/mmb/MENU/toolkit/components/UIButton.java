/**
 * 
 */
package mmb.MENU.toolkit.components;

import mmb.MENU.toolkit.ComponentEventHandler;
import mmb.MENU.toolkit.events.ButtonCEH;

/**
 * @author oskar
 * Convenience class to create buttons.
 */
public class UIButton extends UILabel {
	public Runnable action;
	@Override
	public ComponentEventHandler getHandler() {
		return new ButtonCEH(action);
	}

	public UIButton(String text, Runnable action) {
		super(text);
		this.action = action;
	}
	
	public UIButton(Runnable action) {
		super();
		this.action = action;
	}

	/**
	 * 
	 */
	public UIButton() {
		super();
	}
	
	public UIButton(String text) {
		super(text);
	}

}
