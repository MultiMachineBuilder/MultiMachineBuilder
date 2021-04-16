/**
 * 
 */
package mmb.RUNTIME;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author oskar
 *
 */
public class LockCounter {
	/**
	 * The counter used by join() method
	 */
	// deepcode ignore MissingAPI: used as a public counter	public final AtomicInteger counter = new AtomicInteger();
	/**
	 * Waits until counter goes to 0 or negative
	 */
	public void join() {
		while(counter.get() > 0); //wait
	}
}
