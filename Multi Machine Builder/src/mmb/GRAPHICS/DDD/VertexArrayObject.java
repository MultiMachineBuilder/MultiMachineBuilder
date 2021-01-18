/**
 * 
 */
package mmb.GRAPHICS.DDD;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author oskar
 *
 */
public class VertexArrayObject implements AutoCloseable {
	private int id = 0;
	public VertexArrayObject() {
		id = glGenVertexArrays();
		glBindVertexArray(id);
	}
	
	@Override
	public void close(){
		glDeleteBuffers(id);
		id = -1;
	}
	public int getID() {
		return id;
	}
}
