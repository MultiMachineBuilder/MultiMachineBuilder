/**
 * 
 */
package mmb.BEANS;

import mmb.WORLD.block.Rotation;

/**
 * @author oskar
 *
 */
public interface Rotable {
	public void setRotation(Rotation rotation);
	public Rotation getRotation();
	public default void cw() {
		setRotation(getRotation().cw());
	}
	public default void ccw() {
		setRotation(getRotation().ccw());
	}
}
