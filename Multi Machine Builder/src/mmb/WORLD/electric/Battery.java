/**
 * 
 */
package mmb.WORLD.electric;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.BEANS.Saver;
import mmb.DATA.json.JsonTool;
import mmb.WORLD.block.BlockEntity;

/**
 * @author oskar
 *
 */
public class Battery implements Electricity, Saver<JsonNode>{
	public double maxPower;
	/**
	 * The energy capacity in coulombs
	 */
	public double capacity;
	/**
	 * Stored energy in coulombs
	 */
	public double amt;
	public double pressure = 0;
	public double pressureWt = 1;
	private final BlockEntity blow;
	@Nonnull public VoltageTier voltage;

	/**
	 * Create battery with capacity and power limits
	 * @param maxPower max powr in coulombs per tick
	 * @param capacity power capacity in coulombs
	 * @param blow block which owns this battery. Used to blow up the block if the battery is overvoltaged
	 */
	public Battery(double maxPower, double capacity, BlockEntity blow, VoltageTier voltage) {
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
		amt = bat.amt;
		blow = bat.blow;
		voltage = bat.voltage;
	}

	public double amount() {
		return amt;
	}
	
	private void blow() {
		blow.owner().place(blow.type().leaveBehind(), blow.posX(), blow.posY());
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
		this.amt += max;
		return max;
	}
	@Override
	public double extract(double amt, VoltageTier volt, Runnable blow) {
		int cmp = voltage.compareTo(volt);
		if(cmp > 0) blow.run();
		if(cmp != 0) return 0;
		if(volt != voltage) return 0;
		if(amt < 0) return 0;
		double max = Math.min(amt, Math.min(maxPower, this.amt));
		if(pressure > 0) {
			pressure -= amt;
			if(pressure < 0) pressure = 0;
		}
		double dpressure = amt - max;
		pressure -= dpressure;
		this.amt -= max;
		return max;
	}

	@Override
	public void load(@Nullable JsonNode data) {
		if(data == null) return;
		maxPower = data.get(0).asDouble();
		capacity = data.get(1).asDouble();
		amt = data.get(2).asDouble();
		if(data.size() >= 5) {
			pressure = data.get(3).asDouble();
			pressureWt = data.get(4).asDouble();
		}
	}

	@Override
	public JsonNode save() {
		return JsonTool.newArrayNode().add(maxPower).add(capacity).add(amt).add(pressure).add(pressureWt);
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
		return capacity - amt;
	}
	
	/**
	 * @param elec
	 */
	public void extractTo(Electricity elec) {
		double insert = elec.insert(amt, voltage);
		amt -= insert;
	}
	
	/**
	 * @param elec
	 */
	public void takeFrom(Electricity elec) {
		double insert = elec.extract(Math.min(remain(), amt), voltage, () -> blow.blow());
		amt -= insert;
	}

}
