/**
 * 
 */
package mmbbase.beans;

import java.awt.Color;

import mmb.NN;
import mmbbase.data.variables.Variable;

/**
 * @author oskar
 * Used for objects which have a color
 */
public interface Colorable {
	/**
	 * @return current color
	 */
	@NN public Color getColor();
	/**
	 * Sets the color of the object
	 * @param c new color
	 */
	public void setColor(Color c);
	/**
	 * @return this object's color as a variable object
	 */
	@NN public default Variable<Color> getColorVariable(){
		return Variable.delegate(this::getColor, this::setColor);
	}
}
