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
	public final AtomicInteger counter = new AtomicInteger();
	public void join() {
		while(counter.get() > 0); //wait
	}
}
