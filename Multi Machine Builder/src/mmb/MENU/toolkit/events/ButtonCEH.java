/**
 * 
 */
package mmb.MENU.toolkit.events;

import mmb.MENU.toolkit.ComponentEventHandler;

/**
 * @author oskar
 *
 */
public class ButtonCEH implements ComponentEventHandler{
	private Runnable action;

	public ButtonCEH(Runnable action) {
		super();
		this.action = action;
	}

	@Override
	public void press(UIMouseEvent e) {
		action.run();
	}
	

}
