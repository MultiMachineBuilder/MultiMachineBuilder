/**
 * 
 */
package mmb.MENU.toolkit.components;

import java.awt.Graphics;

import mmb.MENU.toolkit.ComponentEventHandler;
import mmb.MENU.toolkit.UIComponent;

/**
 * @author oskar
 * Do not change weighted dimensions of the object
 */
public class UILabel extends UIComponent {
	public String text = "";
	
	public UILabel(String text) {
		super();
		this.text = text;
	}

	public UILabel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Text margins
	 */
	public int left = 2, right = 2, top = 2, down = 2;
	/**
	 * Text alignment
	 */
	public double alignX = 0, alignY = 0.5;
	
	public int offsetX, offsetY;

	@Override
	public void prepare(Graphics g) {
			int width = g.getFontMetrics().stringWidth(text);
			int height = g.getFontMetrics().getHeight();
			int maxX = getWidth()-width-right;
			int minX = left;
			int maxY = getHeight()-height-down;
			int minY = top;
			offsetX = (int) ((maxX*alignX)+((1-alignX)*minX));
			offsetY = (int) ((maxY*alignY)+((1-alignY)*minY));
	}

	@Override
	public void render(Graphics g) {
		drawBound(g);
		if(g.getFontMetrics() == null) return;
		g.drawString(text, offsetX, offsetY);
	}

	@Override
	public ComponentEventHandler getHandler() {
		return null;
	}

}
