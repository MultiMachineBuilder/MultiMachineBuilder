/**
 * 
 */
package mmb.WORLD.electric;

import java.util.function.DoubleConsumer;

/**
 * @author oskar
 *
 */
public interface Electricity {
	/**
	 * @return stored electricity in joules, or NaN if not monitored
	 */
	public double amount();
	/**
	 * @return maximum storeable electricity in joules
	 */
	public double capacity();
	/**
	 * Tries to insert electricity
	 * @param amt amount to insert in joules
	 * @return amount inserted
	 */
	public double insert(double amt);
	
	public static Electricity runOnInsert(DoubleConsumer cons, Electricity elec) {
		return new Electricity() {
			@Override
			public double amount() {
				return elec.amount();
			}
			@Override
			public double capacity() {
				return elec.capacity();
			}
			@Override
			public double insert(double amt) {
				cons.accept(amt);
				return elec.insert(amt);
			}
		};
	}
	
	/**
	 * Does nothing. Used to just make wires look good.
	 */
	public static final Electricity NONE = new Electricity() {
		@Override
		public double amount() {
			return 0;
		}

		@Override
		public double capacity() {
			return 0;
		}

		@Override
		public double insert(double amt) {
			return 0;
		}	
	};

	public default double remain() {
		return capacity() - amount();
	}
}
