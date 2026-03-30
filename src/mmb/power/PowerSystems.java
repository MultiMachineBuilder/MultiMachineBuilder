package mmb.power;

import java.util.Objects;

/**
 * Contains methods for calculations for power transfer
 */
public class PowerSystems {
	private PowerSystems() {}
	
	public double jouleCapacity(PowerHandler ph) {
		Objects.requireNonNull(ph, "ph is null");
		return ph.voltRating() * ph.joulesPerVolt();
	}
}
