/**
 * 
 */
package mmb.GRAPHICS.DDD;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author oskar
 *
 */
public class VertexBufferObject implements AutoCloseable {
	private int id = 0;
	public VertexBufferObject() {
		id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, id);
	}
	
	@Override
	public void close(){
		glDeleteBuffers(id);
		id = -1;
	}
	public int getID() {
		return id;
	}
	
	//http://wiki.lwjgl.org/wiki/Using_Vertex_Buffer_Objects_(VBO).html
	public static void vertexBufferData(int id, FloatBuffer buffer) { //Not restricted to FloatBuffer
		glBindBuffer(GL_ARRAY_BUFFER, id); //Bind buffer (also specifies type of buffer)
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW); //Send up the data and specify usage hint.
	}
	public static void indexBufferData(int id, IntBuffer buffer) { //Not restricted to IntBuffer
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
	}
}
