/**
 * 
 */
package mmb.WORLD.transit;

/**
 * @author oskar
 *
 */
public interface TransitConnection {
	/**
	 * @return the node to which this connection is connected
	 */
	public TransitNode connection();
}
