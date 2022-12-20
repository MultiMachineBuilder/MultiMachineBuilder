/**
 * 
 */
package mmb.engine.rotate;

import mmb.NN;

/**
 * @author oskar
 *
 */
public interface Rotable {
	public void setRotation(Rotation rotation);
	public @NN Rotation getRotation();
	public default void cw() {
		setRotation(getRotation().cw());
	}
	public default void ccw() {
		setRotation(getRotation().ccw());
	}
}
