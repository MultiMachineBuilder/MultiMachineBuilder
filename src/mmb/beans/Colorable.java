/**
 * 
 */
package mmb.beans;

import java.awt.Color;

import mmb.annotations.NN;
import mmb.data.variables.Variable;

/**
 * An object which has a color.
 * Objects with this type can be painted with a paintbrush
 * @author oskar
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
	@NN public default Variable<@NN Color> getColorVariable(){
		return Variable.delegate(this::getColor, this::setColor);
	}
}
