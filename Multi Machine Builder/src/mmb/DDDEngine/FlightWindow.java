/**
 * 
 */
package mmb.DDDEngine;

import org.joml.Vector3f;
/**
 * @author oskar
 *
 */
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import mmb.debug.Debugger;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.nio.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class FlightWindow {
	private static Debugger debug = new Debugger("Flight");
	private static final int FPS_CAP = 120;
	// The window handle
	protected long window;
	private Timer fps = new Timer();
	public int FPS = 0;
	private int counter = 0;
	public static boolean[] keyboard = new boolean[GLFW_KEY_LAST];
	protected Entity entity;
	private Camera camera;
	
	private RawModel model;
	private ModelTexture texture;
	private TexturedModel texturedModel;
	private Loader loader;
	private StaticShader shader;
	private Renderer renderer;
	private Light light;

	public void run() {
		debug.printl("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		for(int i = 0; i < GLFW_KEY_LAST; i++) {
			keyboard[i] = false;
		}
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(800, 600, "MultiMachineBuilder", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
		DefaultInputHandler ih = new DefaultInputHandler(this);
		ih.set(window);
		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*
			
			////Resize
			//glfwSetWindowSize(window, 800, 600);

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			
			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
		fps.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				FPS = counter;
				counter = 0;
				GLFW.glfwSetWindowTitle(window, "FPS: " + FPS);
			}
			
		}, 1, 1000);
		try {
			// This line is critical for LWJGL's interoperation with GLFW's
			// OpenGL context, or any context that is managed externally.
			// LWJGL detects the context that is current in the current thread,
			// creates the GLCapabilities instance and makes the OpenGL
			// bindings available for use.
			GL.createCapabilities();
			camera = new Camera(new Vector3f(0, 0, 3), (short) 0, (short) 0, (short) 0);
			shader = new StaticShader();
			loader = new Loader();
			renderer = new Renderer(window, shader, new Dimension(800, 600));
			model = OBJLoader.loadObjModel("dragon", loader);
			texture = new ModelTexture(loader.loadTexture("stallTexture"));
			texturedModel = new TexturedModel(model, texture);
			entity = new Entity(texturedModel, new Vector3f(0,0,-25),0,0,0,1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 0));
	}

	private void loop() {
		

		// Set the clear color
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) ) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			//entity.increasePosition(0.002f, 0, 0);
			entity.increaseRotation(0, 0.01f, 0);
			renderer.prepare();
			gameLogic();
			shader.start();
			shader.loadLight(light);
			shader.loadViewMatrix(camera);
			renderer.render(entity, shader);
			shader.stop();
			glfwSwapBuffers(window); // swap the color buffers
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
			//debug.printl(camera.position.toString());
			debug.printl(entity.getPosition().toString());
			counter++;
		}
	}

	public static void main(String[] args) {
		new FlightWindow().run();
	}
	
	private void gameLogic() {
		/*if(keyboard[GLFW_KEY_W]) {
			entity.increasePosition(0, 0.003f, 0);
		}
		if(keyboard[GLFW_KEY_S]) {
			entity.increasePosition(0, -0.003f, 0);
		}
		if(keyboard[GLFW_KEY_A]) {
			entity.increasePosition(-0.003f, 0, 0);
		}
		if(keyboard[GLFW_KEY_D]) {
			entity.increasePosition(0.003f, 0, 0);
		}
		if(keyboard[GLFW_KEY_Q]) {
			entity.increasePosition(0, 0, -0.003f);
		}
		if(keyboard[GLFW_KEY_E]) {
			entity.increasePosition(0, 0, 0.003f);
		}*/
		camera.move();
	}
	
	public static Dimension getSize(long window) {
		int[] x = new int[1];
		int[] y = new int[1];
		glfwGetWindowSize(window, x, y);
		return new Dimension(x[0], y[0]);
	}

}