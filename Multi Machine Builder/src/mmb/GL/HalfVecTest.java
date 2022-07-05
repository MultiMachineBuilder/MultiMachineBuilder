/**
 * 
 */
package mmb.GL;

import org.joml.Matrix2f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import mmb.DATA.contents.Textures;
import mmb.DATA.contents.Textures.Texture;
import mmb.RANDOM.Randomizer;
import mmb.WORLD.rotate.ChiralRotation;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author oskar
 *
 */
public class HalfVecTest{

	/**
	 * Launch the application.
	 */
	public static void run() {
		//Init GLFW
		if(!glfwInit()) 
			throw new IllegalStateException("GLFW not initialized");
		long window = glfwCreateWindow(640, 480, "HalfVec test", 0, 0);
		if(window == 0)
			throw new IllegalStateException("Failed to create a window");
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		if(vidMode == null) throw new IllegalStateException("Failed to get video mode");
		glfwSetWindowPos(window, (vidMode.width()-640)/2, (vidMode.width()-480)/2);
		glfwShowWindow(window);
		glfwMakeContextCurrent(window);
		
		
		GL.createCapabilities();
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		SimpleCtx ctx = new SimpleCtx();
		Texture tex = Textures.get1("machine/wiremill.png");
		Texture texChiral = Textures.get1("example rotation.png");
		Texture tex2 = Textures.get1("machine/pipe 4-section.png");
		Texture tex3 = Textures.get1("player/Machinius The Engineer.png");
		ctx.setup();
		
		//OpenGL stuff for loading in converted data
        
        int[] w = new int[1];
        int[] h = new int[1];
        
        long startTime = System.nanoTime();
        long time = System.nanoTime();
        long time2 = 0;
        int frames = 0;
        boolean toggle2 = false;
        boolean toggle = false;
		while(!glfwWindowShouldClose(window)) {
			//FPS
			long time3 = System.nanoTime();
			time2 += time3-time;
			time = time3;
			frames++;
			if(time2 > 1000_000_000) {
				glfwSetWindowTitle(window, "HalfVec test "+frames);
				frames = 0;
				time2 -= 1000_000_000;
				toggle = !toggle;
			}
			glfwPollEvents();
			glClear(GL_COLOR_BUFFER_BIT);
			glfwGetWindowSize(window, w, h);
			glViewport(0, 0, w[0], h[0]);
			
			//Scale the context so 1 unit = 1 pixel
			int width = w[0];
			int height = h[0];
			float scale = 64;
			float desiredBlocks = height/scale;
			float ratio = (float)width/height;
			ctx.scale(2/(desiredBlocks*ratio), -2/desiredBlocks);
			
			ChiralRotation.Nl.renderGL(texChiral, -1.5f, -0.5f, 1f, 1f, ctx);
			ChiralRotation.Nr.renderGL(texChiral,  0.5f, -0.5f, 1f, 1f, ctx);
			ChiralRotation.El.renderGL(texChiral, -1.5f, -1.5f, 1f, 1f, ctx);
			ChiralRotation.Er.renderGL(texChiral,  0.5f, -1.5f, 1f, 1f, ctx);
			ChiralRotation.Sl.renderGL(texChiral, -1.5f, -2.5f, 1f, 1f, ctx);
			ChiralRotation.Sr.renderGL(texChiral,  0.5f, -2.5f, 1f, 1f, ctx);
			ChiralRotation.Wl.renderGL(texChiral, -1.5f, -3.5f, 1f, 1f, ctx);
			ChiralRotation.Wr.renderGL(texChiral,  0.5f, -3.5f, 1f, 1f, ctx);
			
			ctx.fillCircle(0, 2, 1, 0, 1, 1, 1);
			ctx.renderCircle(0, 2, 0.1f, 0, 0, 0, 1);
			
			//performance test - 12000 textured or textureless quads @ 30fps, 1080p
			if(toggle2) {
				if(toggle) for(int i = 0; i < 12000; i++) {
					ctx.renderColoredTexQuad(
							tex3,
							Randomizer.random4fp(0), Randomizer.random2f(), Randomizer.random2f(), 
							Randomizer.random4fp(0), Randomizer.random2f(), Randomizer.random2f(), 
							Randomizer.random4fp(0), Randomizer.random2f(), Randomizer.random2f(),  
							Randomizer.random4fp(0), Randomizer.random2f(), Randomizer.random2f());
				}
				
				if(!toggle) for(int i = 0; i < 12000; i++) {
					ctx.renderColoredQuad(
							Randomizer.random4fp(0), Randomizer.random2f(), 
							Randomizer.random4fp(0), Randomizer.random2f(), 
							Randomizer.random4fp(0), Randomizer.random2f(),  
							Randomizer.random4fp(0), Randomizer.random2f());
				}
			}
			ctx.render();
			
			glfwSwapBuffers(window);
		}
		ctx.close();
		glfwTerminate();
	}

}
