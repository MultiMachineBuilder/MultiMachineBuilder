/**
 * 
 */
package mmb.WORLD.transit;

import org.joml.Vector2d;

/**
 * @author oskar
 * This interface describes a terminal, a location where vehicles can arrive
 */
public interface Terminal extends TransitConnection{
	//public void dock(Vehicle v);
	
	//Docking location
	/**
	 * Sets the given vector to the in-world approach location
	 * @param dest
	 */
	public void location(Vector2d dest);
	public default Vector2d location() {
		Vector2d result = new Vector2d();
		location(result);
		return result;
	}	
}
