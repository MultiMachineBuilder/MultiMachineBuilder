/**
 * 
 */
package mmb.MENU.toolkit.interfaces;

import java.awt.Dimension;

/**
 * @author oskar
 *
 */
@SuppressWarnings("javadoc")
public interface Sized {
	/**
	 * Get the bound size
	 */
	
	public Dimension getSize();
	/**
	 * Set size, but keep input disconnected
	 */
	default public void setSize(Dimension size) {
		setWidth(size.width);
		setHeight(size.height);
	}
	default public void setSize(int x, int y) {
		setWidth(x);
		setHeight(y);
	}
	/**
	 * Create the new size which is bound from the original object
	 * @param size source size
	 */
	default public Dimension createBoundSize(Dimension size) {
		Dimension result = (Dimension) size.clone();
		bindSize(result);
		return result;
	}
	
	/**
	 * Create a bound size using width and height
	 * @param width @param height dimensions
	 */
	default public Dimension createBoundSize(int width, int height) {
		Dimension result = new Dimension(width, height);
		bindSize(result);
		return result;
	}
	
	default public void setWidth(int width) {
		getSize().width = width;
	}
	default public void setHeight(int height) {
		getSize().height = height;
	}
	
	/**
	 * Bind given size to the component
	 */
	public void bindSize(Dimension size);
	
	default public int getWidth() {
		return getSize().width;
	}
	default public int getHeight() {
		return getSize().height;
	}
	default public Dimension getUnboundSize() {
		return (Dimension) getSize().clone();
	}
}
