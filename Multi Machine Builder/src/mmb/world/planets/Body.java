/**
 * 
 */
package mmb.world.planets;

import java.util.List;

import mmb.files.data.contents.TreeNode;

/**
 * @author oskar
 *
 */
public class Body {
	protected TreeNode<Body> myNode;
	public double weight, size;
	
	public Orbit orbits(Body b) {
		myNode.removeFrom();
		myNode.addTo(b.myNode);
		Orbit out = new Orbit();
		return out;
	}
	public TreeNode<Body> getNode() {
		return myNode;
	}
}
