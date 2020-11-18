/**
 * 
 */
package mmb.WORLD.tileworld.tool;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * @author oskar
 *
 */
public class ToolProxy {
	private boolean hasStatus;
	private String status;
	public Point selectionA, selectionB;
	/**
	 * 
	 */
	public ToolProxy() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Creates the status message
	 * @param status message
	 */
	public void setStatus(String status) {
		this.status = status;
		hasStatus = true;
	}
	
	/**
	 * Clears the status message
	 */
	public void clearStatus() {
		hasStatus = false;
	}

	/**
	 * @return the hasStatus
	 */
	public boolean hasStatus() {
		return hasStatus;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

}
