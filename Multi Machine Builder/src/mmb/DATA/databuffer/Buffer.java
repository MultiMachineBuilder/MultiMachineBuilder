/**
 * 
 */
package mmb.DATA.databuffer;

import java.util.List;
import java.util.Stack;

import mmb.DATA.GetterSetter;
import mmb.SECURITY.PermissionControl.AccessPermissionsControl;

/**
 * @author oskar
 * 
 * This buffer supports security.
 */
public class Buffer<T> {
	//private
	private List<T> data;
	private Stack<Integer> freeSpaces;
	
}