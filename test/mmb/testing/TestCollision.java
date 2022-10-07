/**
 * 
 */
package mmb.testing;

import org.joml.Vector2d;

import mmb.debug.Debugger;
import mmb.world.block.BlockEntry;

/**
 * @author oskar
 *
 */
public class TestCollision {
	public static void main(String[] args) {
		Vector2d off = new Vector2d();
		int result = BlockEntry.displace(-12, -3, -11, -2, -12.41, -3.367, -11.81, -2.767, off);
		Debugger debug = new Debugger("TEST COLLISIONS");
		debug.printl("Collision result: "+result);
		debug.printl("Collision offset: ("+off.x+","+off.y+")");
	}
}
