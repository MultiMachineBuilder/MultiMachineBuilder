package mmb.beans;

import mmb.engine.rotate.ChiralRotation;

/**
 * Assigns a direction to an object.
 */
public interface IRotation {
	/** Gets the orientation of the object*/
	public ChiralRotation getRotation();
	/**
	 * Sets the orientation of the object
	 * @param rotation new orientation
	 * @implSpec Do not throw {@link UnsupportedOperationException}, as this is used by wrenches without checking if the object supports rotations.
	 */
	public void setRotation(ChiralRotation rotation);
}
