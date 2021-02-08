/**
 * 
 */
package mmb.WORLD.beans;

/**
 * @author oskar
 *
 */
public interface Beacon {
	/**
	 * Check if beacon emits a signal upwards
	 * @return is beacon active upwards?
	 */
	public boolean toUp();
	/**
	 * Check if beacon emits a signal downwards
	 * @return is beacon active downwards?
	 */
	public boolean toDown();
	/**
	 * Check if beacon emits a signal leftwards
	 * @return is beacon active leftwards?
	 */
	public boolean toLeft();
	/**
	 * Check if beacon emits a signal rightwards
	 * @return is beacon active rightwards?
	 */
	public boolean toRight();
}
