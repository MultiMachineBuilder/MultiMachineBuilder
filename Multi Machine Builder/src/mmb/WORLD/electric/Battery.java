/**
 * 
 */
package mmb.WORLD.electric;

/**
 * @author oskar
 *
 */
public class Battery implements Electricity {
	public double maxPower;
	public double capacity;
	public double amt;

	/**
	 * Create battery with capacity and 
	 * @param maxPower
	 * @param capacity
	 */
	public Battery(double maxPower, double capacity) {
		super();
		this.maxPower = maxPower;
		this.capacity = capacity;
	}

	/**
	 * Create a zero capacity and power battery
	 */
	public Battery() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public double amount() {
		return amt;
	}

	@Override
	public double capacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double insert(double amt) {
		double remain = remain();
		double max = Math.min(Math.min(amt, maxPower), remain);
		this.amt += max;
		return max;
	}

}
