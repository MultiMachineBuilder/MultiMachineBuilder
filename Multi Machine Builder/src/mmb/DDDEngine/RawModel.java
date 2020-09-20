/**
 * 
 */
package mmb.DDDEngine;

/**
 * @author oskar
 *
 */
public class RawModel {
	private int vaoID, vertexCount;

	public RawModel(int vaoID, int vertexCount) {
		super();
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	/**
	 * @return the vaoID
	 */
	public int getVaoID() {
		return vaoID;
	}

	/**
	 * @return the vertexCount
	 */
	public int getVertexCount() {
		return vertexCount;
	}
	

}
