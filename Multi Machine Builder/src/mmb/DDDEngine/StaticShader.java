/**
 * 
 */
package mmb.DDDEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.joml.Matrix4f;

/**
 * @author oskar
 *
 */
public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/shader/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shader/fragmentShader.txt";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColor;
	public StaticShader() throws FileNotFoundException {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	@Override
	/* (non-Javadoc)
	 * @see mmb.DDDEngine.render.flight.ShaderProgram#bindAttributes()
	 */
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
		
	}
	/* (non-Javadoc)
	 * @see mmb.DDDEngine.render.flight.ShaderProgram#getAllUniformLocations()
	 */
	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColor = super.getUniformLocation("lightColor");
		
	}
	
	public void loadLight(Light light) {
		super.loadVector(location_lightColor, light.color);
		super.loadVector(location_lightPosition, light.position);
	}
	
	public void loadTransformaionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Matrix4f view) {
		super.loadMatrix(location_viewMatrix, view);
	}
	
	public void loadViewMatrix(Camera camera) {
		loadViewMatrix(Renderer.createViewMatrix(camera));
		
	}
}
