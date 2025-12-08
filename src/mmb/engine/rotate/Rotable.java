/**
 * 
 */
package mmb.engine.rotate;

import mmb.annotations.NN;

/**
 * Adds rotation support to any object
 * @author oskar
 */
public interface Rotable {
	/**
	 * Sets the orientation of this object
	 * @param rotation new orientation
	 */
	public void setRotation(Rotation rotation);
	/** @return current orientation of this object */
	public @NN Rotation getRotation();
	/** Rotates this object clockwise */
	public default void cw() {
		setRotation(getRotation().cw());
	}
	/** Rotates this object counter-clockwise */
	public default void ccw() {
		setRotation(getRotation().ccw());
	}
}
