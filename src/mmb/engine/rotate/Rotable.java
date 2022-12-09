/**
 * 
 */
package mmb.engine.rotate;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public interface Rotable {
	public void setRotation(Rotation rotation);
	public @Nonnull Rotation getRotation();
	public default void cw() {
		setRotation(getRotation().cw());
	}
	public default void ccw() {
		setRotation(getRotation().ccw());
	}
}
