/**
 * 
 */
package mmb.DDDEngine2;

import static mmb.DDDEngine2.FlightWindow.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
/**
 * @author oskar
 *
 */
public class DefaultInputHandler implements InputHandler {
	FlightWindow fw;
	public static float x, y;
	/* (non-Javadoc)
	 * @see mmb.DDDEngine2.InputHandler#key(long, int, int, int, int)
	 */
	@Override
	public void key(long window, int key, int scancode, int action, int mods) {
		if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
			glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		}else{
			if(key > -1) {
				if(action == GLFW_PRESS) {
					keyboard[key] = true;
				}else if(action == GLFW_RELEASE) {
					keyboard[key] = false;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see mmb.DDDEngine2.InputHandler#cursor(long, double, double)
	 */
	@Override
	public void cursor(long window, double xpos, double ypos) {
		//fw.entity.setRotY((float) ((xpos - 400) / -200));
		//fw.entity.setRotX((float) ((ypos - 300) / -200));
		x = (float) xpos;
		y = (float) ypos;
	}

	/* (non-Javadoc)
	 * @see mmb.DDDEngine2.InputHandler#mouse(long, int, int, int)
	 */
	@Override
	public void mouse(long window, int action, int button, int mouse) {
		// TODO Auto-generated method stub
		
	}

	protected DefaultInputHandler(FlightWindow fw) {
		super();
		this.fw = fw;
	}
	

}
