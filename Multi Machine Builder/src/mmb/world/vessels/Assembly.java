/**
 * 
 */
package mmb.world.vessels;

import e3d.d3.Rotation3;
import e3d.d3.Vector3;
import mmb.files.data.contents.TreeNode;
import mmb.parts.*;
/**
 * @author oskar
 * A part of vessel
 */
public class Assembly {
	public TreeNode<Part> partTree;
	public Mount hingedFrom;
	public Vector3 speed, acc, pos;
	public Rotation3 rotation, orientation;
	
	/**
	 * 
	 */
	public Assembly() {
		// TODO Auto-generated constructor stub
		
	}

	public void pushFromLocal(Vector3 thrustVec, Vector3 from) {
		
	}
}
