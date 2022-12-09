/**
 * 
 */
package mmb.engine.window;

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
	boolean isDisposing;
	@Override
	public synchronized void dispose() {
		if(isDisposing) return;
		try {
			isDisposing = true;
			if(!undergoingScreenTransform) destroy();
			super.dispose();
		}finally {
			isDisposing = false;
		}
		
	}
	/**
	 * Destroy any involved data, resetting for next set-up
	 */
	public abstract void destroy();
}
