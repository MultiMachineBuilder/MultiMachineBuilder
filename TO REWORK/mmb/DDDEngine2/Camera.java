/**
 * 
 */
package mmb.DDDEngine2;

import static mmb.DDDEngine2.FlightWindow.*;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

/**
 * @author oskar
 *
 */
public class Camera {

	public Vector3f position = new Vector3f();
	public short pitch, yaw, roll;
	public Camera(Vector3f position, short pitch, short yaw, short roll) {
		super();
		this.position = position;
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}
	
	public void move() {
		if(keyboard[GLFW.GLFW_KEY_W]) {
			position.z -= 0.005f;
		}
		if(keyboard[GLFW.GLFW_KEY_S]) {
			position.z += 0.005f;
		}
		if(keyboard[GLFW.GLFW_KEY_D]) {
			position.x += 0.005f;
		}
		if(keyboard[GLFW.GLFW_KEY_A]) {
			position.x -= 0.005;
		}
		pitch = (short) ((DefaultInputHandler.y - 400) * -60);
		yaw = (short) ((DefaultInputHandler.x - 400) * -90);
	}
	/**
	 * @return the position
	 */
	public Vector3f getPosition() {
		return position;
	}
	/**
	 * @return the pitch
	 */
	public short getPitch() {
		return pitch;
	}
	/**
	 * @return the yaw
	 */
	public short getYaw() {
		return yaw;
	}
	/**
	 * @return the roll
	 */
	public short getRoll() {
		return roll;
	}

}
