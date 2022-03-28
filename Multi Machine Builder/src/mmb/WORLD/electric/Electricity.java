/**
 * 
 */
package mmb.WORLD.electric;

import javax.swing.JProgressBar;

import mmb.UnitFormatter;
import mmb.WORLD.block.BlockEntity;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;

/**
 * @author oskar
 *
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
	 * Tries to insert electricity
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
	public VoltageTier voltage();
	/**
	 * @return the power pressure in joules
	 */
	public double pressure();
	/**
	 * @return the power pressure weights
	 */
	public double pressureWeight();
	
	static void formatProgress(JProgressBar bar, double value, double max) {
		bar.setString(UnitFormatter.formatEnergy(value)+"/"+UnitFormatter.formatEnergy(max));
		bar.setValue((int) (value * 100 / max));
	}

	/**
	 * Does nothing. Used to just make wires look good.
	 */
	public static final Electricity NONE = new Electricity() {
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
	public static Electricity extractOnly(Electricity elec) {
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
	 * @param elec
	 * @return
	 */
	public static Electricity insertOnly(Electricity elec) {
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
	
	public static interface SettablePressure extends Electricity{
		/**
		 * Sets the power pressure
		 * @param pressure new power pressure
		 */
		public void setPressure(double pressure);
	}
	
	/**
	 * Balancer power pressure of given block
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
	 * Balancer power pressure of given block
	 * @param block block, which owns the battery
	 * @param proxy the map proxy to schedule calculations
	 * @param bat battery to balance
	 * @param mul retention multiplier. Lower values mean faster leakage
	 */
	public static void equatePPs(BlockEntity block, MapProxy proxy, TransferHelper bat, double mul){
		proxy.later(() -> {
			double weight = bat.pressureWt; //the initial weight sum is current weight
			double sum = weight * bat.pressure; //the initial sum is current power pressure volume
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
			bat.pressure = newPressure;
			if(Double.isNaN(newPressure)) bat.pressure = 0;
		});
	}
}
