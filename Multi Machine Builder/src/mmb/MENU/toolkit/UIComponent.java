/**
 * 
 */
package mmb.MENU.toolkit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import mmb.MENU.toolkit.components.ComponentSource;
import mmb.MENU.toolkit.events.SharedEventHandler;
import mmb.MENU.toolkit.interfaces.Sized;

/**
 * @author oskar
 *
 */
public abstract class UIComponent implements Sized, ComponentSource{
	/**
	 * Width and height: base values, weights and max values
	 * 
	 * Max dimensions:
	 * The largest size that component may get in the parent graphics context.
	 * Used by layouts to determine size of variable size components 
	 * 
	 * Weights:
	 * Numerator of remaining space fraction with denonimator of the sum of fractions
	 * 
	 * Base value:
	 * The value added to dynamic size
	 */
	@SuppressWarnings("javadoc")
	public int  baseWidth = 0, baseHeight = 1,
				weightWidth = 1, weightHeight = 1;
	@Override
	public Dimension getSize() {
		return finalSize;
	}

	@Override
	public void bindSize(Dimension size) {
		finalSize = size;
	}

	/**
	 * @deprecated Use getWidth(), getHeight(), setHeight() and setHeight() instead
	 */
	@SuppressWarnings("javadoc")
	@Deprecated
	public int finalWidth, finalHeight;
	
	/**
	 * Final calculated size
	 */
	private Dimension finalSize = new Dimension();
	
	public Color background = UIDefaults.background;
	public Color foreground = UIDefaults.foreground;
	public Color border = UIDefaults.border;
	
	//CONSTRUCTORS
	public UIComponent() {
		resetColors();
	}	

	//PUBLIC METHODS
	
	public void resetColors() {
		background = UIDefaults.background;
		foreground = UIDefaults.foreground;
		border = UIDefaults.border;
	}
	public void drawBound(Graphics g) {
		g.setColor(background);
		g.fillRect(0, 0, getWidth()-1, getHeight()-1);
		g.setColor(border);
		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
	}
	
	//RESIZING
	public void fillVertical() {
		weightHeight = 1;
		baseHeight = 0;
	}
	public void fillHorizontal() {
		weightWidth = 1;
		baseWidth = 0;
	}
	public void fill() {
		weightHeight = 1;
		baseHeight = 0;
		weightWidth = 1;
		baseWidth = 0;
	}
	public void constantHeight(int h) {
		weightHeight = 0;
		baseHeight = h;
	}
	public void constantWidth(int w) {
		weightHeight = 0;
		baseHeight = w;
	}
	
	

	//INTERFACE METHODS
	/**
	 * Prepare the component before rendering eg. change size of the labels, adjust canvases and resize icons
	 * @param g graphics
	 */
	abstract public void prepare(Graphics g);
	/**
	 * Draw the component itself
	 * @param g graphics
	 */
	abstract public void render(Graphics g);
	/**
	 * Override this method to optionally handle exit
	 */
	public void destroy() {}
	public SharedEventHandler getCommonEventHandler() {return null;}
	
	@Override
	protected void finalize() throws Throwable {
		destroy();
		super.finalize();
	}

	//VALUES
	/**
	 * Alignment values (for alignable objects like buttons and text labels
	 */
	@SuppressWarnings("javadoc")
	public static final double
		ALIGN_TOP = 0,
		ALIGN_CENTER = 0.5,
		ALIGN_BOTTOM = 1,
		ALIGN_LEFT = 0,
		ALIGN_RIGHT = 1;
	
	/**
	 * @return the event handler
	 */
	abstract public ComponentEventHandler getHandler();

	@Override
	public UIComponent getComponent() {
		return this;
	}
	
	public boolean isFocusable = false;
	
}
