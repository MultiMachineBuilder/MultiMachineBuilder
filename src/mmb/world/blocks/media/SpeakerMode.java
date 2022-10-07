/**
 * 
 */
package mmb.world.blocks.media;

/**
 * @author oskar
 * Describes how the sound is played.
 * The playback may be requested by clicking "Play" button or by a Block Clicking Claw
 */
public enum SpeakerMode {
	/**
	 * Plays the sound once when specifically requested
	 */
	ONCE {
		@Override
		public void run(Speaker s, boolean isSignalled) {
			s.handleOnce();
		}
	},
	/**
	 * Plays the sound, repeating it until specifically requested,
	 * starting when specifically requested earlier
	 */
	REPEAT {
		@Override
		public void run(Speaker s, boolean isSignalled) {
			s.handleLoop();
		}
	},
	/** Plays the sound,
	 * repeating while the speaker is powered,
	 * starting when the speaker receives a signal,
	 * but not interrupting the sound prematurely 
	 */
	REPEAT_WHEN_POWERED {
		@Override
		public void run(Speaker s, boolean isSignalled) {
			s.setPlaybackRequested(isSignalled);
			s.handleLoop();
		}
	},
	/** Plays the sound once,
	 * starting when the speaker receives a signal,
	 * and interrupts the sound prematurely when signal is gone
	 */
	ONCE_WHILE_POWERED {
		@Override
		public void run(Speaker s, boolean isSignalled) {
			s.setPlaybackRequested(isSignalled);
			s.handleOnceInterruptible();
		}
	},
	/** Plays the sound,
	 * repeating while the speaker is powered,
	 * starting when the speaker receives a signal,
	 * and interrupts the sound prematurely when signal is gone
	 */
	REPEAT_WHILE_POWERED {
		@Override
		public void run(Speaker s, boolean isSignalled) {
			s.setPlaybackRequested(isSignalled);
			s.handleLoopInterruptible();
		}
	},
	/** Plays the sound once,
	 * starting when the speaker receives a signal,
	 * but not interrupting the sound prematurely
	 */
	ONCE_WHEN_POWERED {
		@Override
		public void run(Speaker s, boolean isSignalled) {
			s.setPlaybackRequested(isSignalled);
			s.handleOnce();
		}
	};
	
	/**
	 * Handles the playback
	 * @param s the speaker
	 * @param isSignalled does the speaker receive power
	 */
	public abstract void run(Speaker s, boolean isSignalled);
}
