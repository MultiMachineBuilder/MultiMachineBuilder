/**
 * 
 */
package mmb.content.electric;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.util.concurrent.Runnables;

import mmb.NN;
import mmb.Nil;
import mmb.content.electric.Electricity.SettablePressure;
import mmb.engine.block.BlockEntity;
import mmb.engine.json.JsonTool;
import mmbbase.beans.Saver;

/**
 * @author oskar
 *
 */
public class Battery implements SettablePressure, Comparable<@NN Battery>, Saver{
	/** Maximum power in coulombs per tick */
	public double maxPower;
	/** The energy capacity in coulombs */
	public double capacity;
	/** Stored energy in coulombs */
	public double stored;
	/** Current power pressure in joules */
	public double pressure = 0;
	/** Current power pressure weight */
	public double pressureWt = 1;
	@Nil private final BlockEntity blow;
	/** The voltage tier of this battery */
	@NN public VoltageTier voltage;

	/**
	 * Create battery with capacity and power limits
	 * @param maxPower max power in coulombs per tick
	 * @param capacity power capacity in coulombs
	 * @param blow block which owns this battery. Used to blow up the block if the battery is overvoltaged
	 * @param voltage voltage tier
	 */
	public Battery(double maxPower, double capacity, @Nil BlockEntity blow, VoltageTier voltage) {
		super();
		this.maxPower = maxPower;
		this.capacity = capacity;
		this.blow = blow;
		this.voltage = voltage;
	}

	/**
	 * Creates a copy of the battery
	 * @param bat battery to copy
	 */
	public Battery(Battery bat) {
		maxPower = bat.maxPower;
		capacity = bat.capacity;
		stored = bat.stored;
		blow = bat.blow;
		voltage = bat.voltage;
	}

	public double amount() {
		return stored;
	}
	
	private void blow() {
		final BlockEntity blow2 = blow;
		if (blow2 != null) 
			blow2.owner().place(blow2.type().leaveBehind(), blow2.posX(), blow2.posY());
	}

	@Override
	public double insert(double amt, VoltageTier volt) {
		int cmp = volt.compareTo(voltage);
		if(cmp > 0) blow();
		if(cmp != 0) return 0;
		if(amt < 0) return 0;
		double max = Math.min(amt, Math.min(maxPower, remain()));
		if(pressure < 0) {
			pressure += amt;
			if(pressure > 0) pressure = 0;
		}
		double dpressure = amt - max;
		pressure += dpressure;
		this.stored += max;
		return max;
	}
	@Override
	public double extract(double amt, VoltageTier volt, Runnable blow) {
		int cmp = voltage.compareTo(volt);
		if(cmp > 0) blow.run();
		if(cmp != 0) return 0;
		if(volt != voltage) return 0;
		if(amt < 0) return 0;
		double max = Math.min(amt, Math.min(maxPower, this.stored));
		if(pressure > 0) {
			pressure -= amt;
			if(pressure < 0) pressure = 0;
		}
		double dpressure = amt - max;
		pressure -= dpressure;
		this.stored -= max;
		return max;
	}

	@Override
	public void load(@Nil JsonNode data) {
		if(data == null || data.isEmpty()) return;
		maxPower = data.get(0).asDouble();
		capacity = data.get(1).asDouble();
		stored = data.get(2).asDouble();
		if(data.size() >= 5) {
			pressure = data.get(3).asDouble();
			pressureWt = data.get(4).asDouble();
		}
	}

	@Override
	public JsonNode save() {
		return JsonTool.newArrayNode().add(maxPower).add(capacity).add(stored).add(pressure).add(pressureWt);
	}
	
	@Override
	public VoltageTier voltage() {
		return voltage;
	}

	@Override
	public double pressure() {
		return pressure;
	}

	@Override
	public double pressureWeight() {
		return pressureWt;
	}

	/**
	 * @return remaining charge in coulombs
	 */
	public double remain() {
		return capacity - stored;
	}
	
	/**
	 * @param elec
	 */
	public void extractTo(Electricity elec) {
		double insert = elec.insert(Math.min(stored, maxPower), voltage);
		if(maxPower > stored) pressure -= stored*voltage.volts;
		stored -= insert;
	}
	
	/**
	 * @param elec
	 */
	public void takeFrom(Electricity elec) {
		double remain = remain();
		BlockEntity blow2 = blow;
		double insert = elec.extract(Math.min(remain, maxPower), voltage, blow2==null?Runnables.doNothing():blow2::blow);
		if(maxPower > remain) pressure += remain*voltage.volts;
		stored += insert;
	}

	/**
	 * Sets all properties of this battery to those of other battery
	 * @param bat battery with new settings
	 */
	public void set(Battery bat) {
		maxPower = bat.maxPower;
		capacity = bat.capacity;
		stored = bat.stored;
		pressure = bat.pressure;
		pressureWt = bat.pressureWt;
	}

	@Override
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	/** @return stored energy in joules */
	public double energy() {
		return stored*voltage.volts;
	}

	
	//Value
	@Override
	public int compareTo(Battery o) {
		int cmp = voltage.compareTo(o.voltage);
		if(cmp != 0) return cmp;
		cmp = Double.compare(stored, o.stored);
		if(cmp != 0) return cmp;
		cmp = Double.compare(capacity, o.capacity);
		if(cmp != 0) return cmp;
		cmp = Double.compare(maxPower, o.maxPower);
		if(cmp != 0) return cmp;
		cmp = Double.compare(pressure, o.pressure);
		if(cmp != 0) return cmp;
		return Double.compare(pressureWt, o.pressureWt);
	}
	@Override
	public String toString() {
		return "Battery [maxPower=" + maxPower + ", capacity=" + capacity + ", amt=" + stored + ", pressure=" + pressure
				+ ", pressureWt=" + pressureWt + ", voltage=" + voltage + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(stored);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(capacity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maxPower);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(pressure);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(pressureWt);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + voltage.hashCode();
		return result;
	}
	@Override
	public boolean equals(@Nil Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Battery other = (Battery) obj;
		if (Double.doubleToLongBits(stored) != Double.doubleToLongBits(other.stored))
			return false;
		if (Double.doubleToLongBits(capacity) != Double.doubleToLongBits(other.capacity))
			return false;
		if (Double.doubleToLongBits(maxPower) != Double.doubleToLongBits(other.maxPower))
			return false;
		if (Double.doubleToLongBits(pressure) != Double.doubleToLongBits(other.pressure))
			return false;
		if (Double.doubleToLongBits(pressureWt) != Double.doubleToLongBits(other.pressureWt))
			return false;
		if (voltage != other.voltage)
			return false;
		return true;
	}
}
