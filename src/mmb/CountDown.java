/**
 * 
 */
package mmb;

import java.time.Instant;
import java.time.temporal.TemporalUnit;

/**
 * @author oskar
 *
 */
public class CountDown {
	private Instant toEnd = Instant.EPOCH;
	
	public long remaining(TemporalUnit tu) {
		return Instant.now().until(toEnd, tu);
	}
	public void reset() {
		resetNanos(0);
	}
	public void resetNanos(long time) {
		toEnd = Instant.now().plusNanos(time);
	}
	public void resetMilis(long time) {
		toEnd = Instant.now().plusMillis(time);
	}
	public void resetSeconds(long time) {
		toEnd = Instant.now().plusSeconds(time);
	}
}
