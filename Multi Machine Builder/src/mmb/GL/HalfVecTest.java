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
		Texture texChiral = Textures.get1("example rotation.png");
		Texture frame = Textures.get1("item/frame 1.png");
		Texture beer = Textures.get1("item/beer.png");
		Texture pipeline = Textures.get1("pipeline.png");
		ctx.setup();
		
		//OpenGL stuff for loading in converted data
        
        int[] w = new int[1];
        int[] h = new int[1];
        
        long startTime = System.nanoTime();
        long time = startTime;
        long time2 = 0;
        int frames = 0;
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
			}
			glfwPollEvents();
			glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
			glfwGetWindowSize(window, w, h);
			glViewport(0, 0, w[0], h[0]);
			
			//Get time
			float seconds = (time-startTime)/1000_000_000f;
			//Scale the context so 1 unit = 1 pixel
			int width = w[0];
			int height = h[0];
			float scale = 64;
			float desiredBlocks = height/scale;
			float ratio = (float)width/height;
			
			float scaleX = 2/(desiredBlocks*ratio);
			float scaleY = -2/desiredBlocks;
			float rotation = (float) Math.sin(seconds/4)/2;
			Matrix2f matrix = new Matrix2f().rotation(rotation).scale(scaleX, scaleY);
			ctx.transform(matrix);
			
			ctx.zindex(0.1f);
			ChiralRotation.Nl.renderGL(texChiral, -1.5f, -0.5f, 1f, 1f, ctx);
			ChiralRotation.Nr.renderGL(texChiral,  0.5f, -0.5f, 1f, 1f, ctx);
			ChiralRotation.El.renderGL(texChiral, -1.5f, -1.5f, 1f, 1f, ctx);
			ChiralRotation.Er.renderGL(texChiral,  0.5f, -1.5f, 1f, 1f, ctx);
			ChiralRotation.Sl.renderGL(texChiral, -1.5f, -2.5f, 1f, 1f, ctx);
			ChiralRotation.Sr.renderGL(texChiral,  0.5f, -2.5f, 1f, 1f, ctx);
			ChiralRotation.Wl.renderGL(texChiral, -1.5f, -3.5f, 1f, 1f, ctx);
			ChiralRotation.Wr.renderGL(texChiral,  0.5f, -3.5f, 1f, 1f, ctx);
			
			ctx.zindex(0.5f);
			ctx.fillCircle(0, 2, 1, 0, 1, 1, 1);
			ctx.zindex(0.4f);
			ctx.renderCircle(0, 2, 0.1f, 0, 0, 0, 1);
			
			//The blue rectangle is behind the green one, despite being drawn later
			ctx.zindex(0.4f);
			ctx.renderRectSimple(-3, 2, 1, 1, 0, 1, 0, 1);
			ctx.zindex(0.5f);
			ctx.renderRectSimple(-2.5f, 2.5f, 1, 1, 0, 0, 1, 1);
			
			//The beer should be clearly seen through a frame
			ctx.zindex(0.4f);
			ctx.renderTextureSimple(frame, 3, 2, 2, 2);
			ctx.zindex(0.5f);
			ctx.renderTextureSimple(beer, 3.5f, 2.5f, 1, 1);
			
			//standard performance - 4784 blocks@30fps - 143 520 draws per second
			//performance test - 12000 textured or textureless quads @ 30fps, 1080p
			//performance test - 40000 beers @ 60fps, 1080p - 2 400 000 draws per second
			
			//performance test - 40000 plank blocks @ 60fps, 1080p - 2 400 000draws per second (cold)
			//performance test - 40000 plank blocks @ 30fps, 1080p - 1 200 000draws per second (hot)
			
			//variable size plank blocks
			//performance test - 40000 plank blocks @ 60fps, 1080p - 2 400 000draws per second (cold)
			//performance test - 40000 plank blocks @ 30fps, 1080p - 1 200 000draws per second (hot)
			
			//test line rendering
			float x1 = 3.5f;
			float y1 = -2.5f;
			float x2 = x1 + org.joml.Math.sin(seconds);
			float y2 = y1 + org.joml.Math.cos(seconds);
			
			ctx.zindex(0.5f);
			ctx.fillCircle(x1, y1, 0.1f, 1, 1, 0, 1);
			ctx.fillCircle(x2, y2, 0.1f, 0, 1, 0, 1);
			ctx.zindex(0.4f);
			ctx.renderLineQuadTex(
					x1, y1, x2, y2,
					-0.1f, 0.1f, pipeline,
					0, 0, 1, 1,
					1, 0, 1, 1);
			
			//render a block grid
			ctx.render();
			glfwSwapBuffers(window);
		}
		ctx.close();
		glfwTerminate();
	}

}
