/**
 * 
 */
package mmb.MENU.toolkit.components;

import java.awt.Graphics;
import java.awt.Point;
import mmb.MENU.toolkit.Container;
import mmb.MENU.toolkit.UIComponent;
import mmb.MENU.toolkit.auxiliary.Pane;

/**
 * @author oskar
 *
 */
public class UILayoutMargins extends Container {
	/**
	 * Margin sizes.
	 * Set to 0 to make corresponding edges coincide
	 * Set below 0 to make corresponding edge go outside the box
	 * Set above 0 to make edges stay clear
	 * Set to 1 to make corresponding edges touch
	 */
	@SuppressWarnings("javadoc")
	public int left = 0, right = 0, top = 0, down = 0;
	/**
	 * The component which appears inside
	 */
	public UIComponent contents;
	/**
	 * 
	 */
	public UILayoutMargins() {
		super();
	}
	
	/**
	 * @param contents contained item
	 */
	public UILayoutMargins(UIComponent contents) {
		super();
		this.contents = contents;
	}

	@Override
	public void prepare(Graphics g) {
		int remainWidth = getWidth()-left-right;
		contents.setWidth(contents.baseWidth);
		if(contents.weightWidth > 0) {
			contents.setWidth(remainWidth);
		}
		int remainHeight = getHeight()-left-right;
		setHeight(contents.baseHeight);
		if(contents.weightHeight > 0) {
			setHeight(remainHeight);
		}
		comppos = new Pane[] {new Pane(new Point(left, top), contents)};
		Graphics g1 = g.create(left, top, contents.getWidth()+1, contents.getHeight()+1);
		contents.prepare(g1);
	}

	@Override
	public UIComponent[] getComponents() {
		return new UIComponent[] {contents};
	}
}
