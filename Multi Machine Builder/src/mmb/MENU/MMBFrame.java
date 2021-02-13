/**
 * 
 */
package mmb.MENU;

import javax.swing.JFrame;

/**
 * @author oskar
 * An auxiliary frame class, which supports full screen control
 */
@SuppressWarnings("serial")
public abstract class MMBFrame extends JFrame {
	
	boolean undergoingScreenTransform = false;
	/**
	 * Check if the screen is currently undergoing a full-screen transform. It serves to disable destroy() method when using dispose();
	 * @return the undergoingScreenTransform
	 */
	public boolean isUndergoingScreenTransform() {
		return undergoingScreenTransform;
	}
	@Override
	public synchronized void dispose() {
		if(!undergoingScreenTransform) destroy();
		super.dispose();
	}
	/**
	 * Destroy any involved data, resetting for next set-up
	 */
	public abstract void destroy();
}
