/**
 * 
 */
package mmb.DDDEngine2;

import org.joml.Vector3f;

/**
 * @author oskar
 *
 */
public class Light {
	public Vector3f position;
	public Vector3f color;
	public Light(Vector3f position, Vector3f color) {
		super();
		this.position = position;
		this.color = color;
	}
}
