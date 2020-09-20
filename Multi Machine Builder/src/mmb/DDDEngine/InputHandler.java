/**
 * 
 */
package mmb.DDDEngine;
import static org.lwjgl.glfw.GLFW.*;
/**
 * @author oskar
 *
 */
public interface InputHandler {
	public void key(long window, int key, int scancode, int action, int mods);
	public void cursor(long window, double xpos, double ypos);
	public void mouse(long window, int action, int button, int mouse);
	default public void set(long win) {
		glfwSetKeyCallback(win, (window, key, scancode, action, mods)->{
			key(window, key, scancode, action, mods);
		});
		glfwSetCursorPosCallback(win, (window, xpos, ypos)->{
			cursor(window, xpos, ypos);
		});
		glfwSetMouseButtonCallback(win, (window, action, button, mouse) -> {
			mouse(window, action, button, mouse);
		});
	}
}
