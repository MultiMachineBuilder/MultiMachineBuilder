/**
 * 
 */
package mmb.content.electric;

import java.util.function.Supplier;

import javax.swing.JProgressBar;

import mmb.NN;
import mmb.Nil;
import mmb.engine.UnitFormatter;
import mmb.engine.block.BlockEntity;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;

/**
 * An electrical connection
 * @author oskar
 */
public interface Electricity {
	/**
	 * Tries to insert electricity
	 * @param amt amount to insert in coulombs
	 * @param volt voltage tier
	 * @return amount inserted
	 */
	public double insert(double amt, VoltageTier volt);
	/**
	 * Tries to extract electricity
	 * @param amt amount to extract in coulombs
	 * @param volt voltage tier
	 * @param blow runs when expected voltage is lower than voltage of this electricity.
	 * Used to blow up overvoltaged blocks.
	 * @return amount extracted
	 */
	public double extract(double amt, VoltageTier volt, Runnable blow);
	/**
	 * Inserts or extracts electricity
	 * @param amt amount to transfer
	 * @param volt voltage tier
	 * @param blow runs when expected voltage is lower than voltage of this electricity.
	 * Used to blow up overvoltaged blocks.
	 * @return electricity balance change
	 */
	public default double transfer(double amt, VoltageTier volt, Runnable blow) {
		if(amt > 0) return insert(amt, volt);
		if(amt < 0) return -extract(-amt, volt, blow);
		return 0;
	}
	
	/**
	 * @return the maximum voltage
	 */
	@NN public VoltageTier voltage();
	/**
	 * @return the power pressure in joules
	 */
	public double pressure();
	/**
	 * @return the power pressure weights
	 */
	public double pressureWeight();
	
	/**
	 * Sets the progress bar to display the amount of electricity in a container
	 * @param bar the progress bar
	 * @param value the current amount of electricity
	 * @param max the maximum amount of electricity
	 */
	static void formatProgress(JProgressBar bar, double value, double max) {
		bar.setString(UnitFormatter.formatEnergy(value)+"/"+UnitFormatter.formatEnergy(max));
		bar.setValue((int) value);
		bar.setMaximum((int)max);
	}

	/** Does nothing. Used to make wires look good and in {@link #optional(Electricity)}; */
	@NN public static final Electricity NONE = new Electricity() {
		@Override
		public double insert(double amt, VoltageTier volt) {
			return 0;
		}
		@Override
		public double extract(double amt, VoltageTier volt, Runnable blow) {
			return 0;
		}
		@Override
		public VoltageTier voltage() {
			return VoltageTier.V9;
		}
		@Override
		public double pressure() {
			return 0;
		}
		@Override
		public double pressureWeight() {
			return 0;
		}
	};
	
	//Restricted
	/**
	 * Removes ability to insert electricity from this connection
	 * @param elec the electrical connection
	 * @return electrical connection without ability to extract
	 */
	@NN public static Electricity extractOnly(Electricity elec) {
		return new Electricity() {

			@Override
			public double insert(double amt, VoltageTier volt) {
				return 0;
			}

			@Override
			public double extract(double amt, VoltageTier volt, Runnable blow) {
				return elec.extract(amt, volt, blow);
			}

			@Override
			public VoltageTier voltage() {
				return elec.voltage();
			}

			@Override
			public double pressure() {
				return elec.pressure();
			}

			@Override
			public double pressureWeight() {
				return elec.pressureWeight();
			}
			
		};
	}
	/**
	 * Removes ability to extract electricity from this connection
	 * @param elec the electrical connection
	 * @return electrical connection without ability to insert
	 */
	@NN public static Electricity insertOnly(Electricity elec) {
		return new Electricity() {

			@Override
			public double insert(double amt, VoltageTier volt) {
				return elec.insert(amt, volt);
			}

			@Override
			public double extract(double amt, VoltageTier volt, Runnable blow) {
				return 0;
			}

			@Override
			public VoltageTier voltage() {
				return elec.voltage();
			}

			@Override
			public double pressure() {
				return elec.pressure();
			}

			@Override
			public double pressureWeight() {
				return elec.pressureWeight();
			}
		};
	}
	/**
	 * Protects the underlying electrical connection from overvoltage
	 * @param elec underlying electrical connection
	 * @param max maximum voltage
	 * @param blow block to blow, optional
	 * @return a new electrical connection
	 */
	@NN public static Electricity circuitBreaker(Electricity elec, VoltageTier max, @Nil BlockEntity blow) {
		return new Electricity() {
			@Override
			public double insert(double amt, VoltageTier volt) {
				if(volt.compareTo(max) > 0) {
					if(blow != null) blow.blow();
					return 0;
				}
				return elec.insert(amt, volt);
			}

			@Override
			public double extract(double amt, VoltageTier volt, Runnable blowr) {
				if(elec.voltage().compareTo(max) > 0) {
					if(blow != null) blow.blow();
					return 0;
				}
				return elec.extract(amt, volt, blowr);
			}

			@Override
			public VoltageTier voltage() {
				return max;
			}

			@Override
			public double pressure() {
				return elec.pressure();
			}

			@Override
			public double pressureWeight() {
				return elec.pressureWeight();
			}
			
		};
	}
	/**
	 * Creates a dynamic electric connection
	 * @param elec supplier of electrical connections (the supplier may return null to indicate lack of connection)
	 * @return an electrical connection based on given supplier of electrical connections
	 */
	@NN public static Electricity dynamicElectricity(Supplier<@Nil Electricity> elec) {
		return new Electricity() {
			private Electricity obtain() {
				Electricity result = elec.get();
				if(result == null) return NONE;
				return result;
			}
			@Override
			public double insert(double amt, VoltageTier volt) {
				return obtain().insert(amt, volt);
			}

			@Override
			public double extract(double amt, VoltageTier volt, Runnable blow) {
				return obtain().extract(amt, volt, blow);
			}

			@Override
			public VoltageTier voltage() {
				return obtain().voltage();
			}

			@Override
			public double pressure() {
				return obtain().pressure();
			}

			@Override
			public double pressureWeight() {
				return obtain().pressureWeight();
			}
		};
	}
	/**
	 * Limits the power output of the electrical connection
	 * @param elec electrical connection
	 * @param pwr maximum current
	 * @return new electrical connection
	 */
	@NN public static Electricity limitCurrent(Electricity elec, double pwr) {
		return new Electricity() {

			@Override
			public double insert(double amt, VoltageTier volt) {
				return elec.insert(Math.min(amt, pwr), volt);
			}

			@Override
			public double extract(double amt, VoltageTier volt, Runnable blow) {
				return elec.extract(Math.min(amt, pwr), volt, blow);
			}

			@Override
			public VoltageTier voltage() {
				return elec.voltage();
			}

			@Override
			public double pressure() {
				return elec.pressure();
			}

			@Override
			public double pressureWeight() {
				return elec.pressureWeight();
			}
			
		};
	}
	
	/**
	 * Extends the electrical connections by adding ability to set power pressures.
	 * @author oskar
	 */
	public static interface SettablePressure extends Electricity{
		/**
		 * Sets the power pressure
		 * @param pressure new power pressure
		 */
		public void setPressure(double pressure);
	}
	
	/**
	 * Balances power pressure of given block
	 * @param block block, which owns the battery
	 * @param proxy the map proxy to schedule calculations
	 * @param bat battery to balance
	 * @param mul retention multiplier. Lower values mean faster leakage
	 */
	public static void equatePPs(BlockEntity block, MapProxy proxy, SettablePressure bat, double mul){
		proxy.later(() -> {
			double weight = bat.pressureWeight(); //the initial weight sum is current weight
			double sum = weight * bat.pressure(); //the initial sum is current power pressure volume
			Electricity uu = block.getAtSide(Side.U).getElectricalConnection(Side.D);
			if(uu != null) {
				weight += uu.pressureWeight();
				sum += uu.pressure() * uu.pressureWeight();
			}
			Electricity dd = block.getAtSide(Side.D).getElectricalConnection(Side.U);
			if(dd != null) {
				weight += dd.pressureWeight();
				sum += dd.pressure() * dd.pressureWeight();
			}
			Electricity ll = block.getAtSide(Side.L).getElectricalConnection(Side.R);
			if(ll != null) {
				weight += ll.pressureWeight();
				sum += ll.pressure() * ll.pressureWeight();
			}
			Electricity rr = block.getAtSide(Side.R).getElectricalConnection(Side.L);
			if(rr != null) {
				weight += rr.pressureWeight();
				sum += rr.pressure() * rr.pressureWeight();
			}
			double newPressure = (mul * sum) / weight;
			
			if(Double.isNaN(newPressure)) bat.setPressure(0);
			else bat.setPressure(newPressure);
		});
	}
	/**
	 * Makes sure that electricity is non-null
	 * @param energy input electrical connection
	 * @return the Electricity.NONE if null or else input value
	 */
	@NN public static Electricity optional(@Nil Electricity energy) {
		return energy==null?NONE:energy;
	}
}
