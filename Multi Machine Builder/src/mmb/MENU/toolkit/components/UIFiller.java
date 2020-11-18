/**
 * 
 */
package mmb.MENU.toolkit.components;

import java.awt.Graphics;

import mmb.MENU.toolkit.ComponentEventHandler;
import mmb.MENU.toolkit.UIComponent;

/**
 * @author oskar
 *
 */
public class UIFiller extends UIComponent {
	@Override
	public void prepare(Graphics g) {
		//Nothing to do
	}

	@Override
	public void render(Graphics g) {
		drawBound(g);
	}

	@Override
	public ComponentEventHandler getHandler() {
		return null;
	}

}
