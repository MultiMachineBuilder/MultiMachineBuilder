/**
 * 
 */
package mmb.DDDEngine2;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import mmb.debug.Debugger;

import static org.lwjgl.glfw.GLFW.*;

import java.awt.Dimension;

/**
 * @author oskar
 *
 */
public class Renderer {
	
	protected long window;
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	private static final Debugger debug = new Debugger("MATRIX");

	private Matrix4f projectionMatrix;
	
	private Dimension size;
	
	public Renderer(long w, StaticShader shader) {
		window = w;
		this.size = FlightWindow.getSize(window);
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	public Renderer(long w, StaticShader shader, Dimension size) {
		this.size = size;
		window = w;
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	public void prepare() {
		GL11.glClearColor(1, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	public void render(Entity y, StaticShader shader) {
		TexturedModel tmodel = y.getModel();
		RawModel model = tmodel.getRawModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(y.getPosition(), y.getRotX(), y.getRotY(), y.getRotZ(), y.getScale());
		shader.loadTransformaionMatrix(transformationMatrix);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmodel.getTexture().getID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void createProjectionMatrix() {
		
		float aspectRatio =  size.width / size.height;
		
		projectionMatrix = Maths.projMatrix(aspectRatio, FOV, NEAR_PLANE, FAR_PLANE);
		
		debug.printl(projectionMatrix.toString());
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		return viewMatrix.rotateLocalX((float) Maths.shortToRad((short) -camera.pitch))
					.rotateLocalY((float) Maths.shortToRad((short) -camera.yaw))
					.rotateLocalZ((float) Maths.shortToRad((short) -camera.roll))
					.translate(-camera.position.x, -camera.position.y, -camera.position.z);
	}
}
