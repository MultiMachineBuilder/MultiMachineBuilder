/**
 * 
 */
package mmb.WORLD.electric;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.BEANS.Loader;
import mmb.BEANS.Saver;
import mmb.DATA.json.JsonTool;

/**
 * @author oskar
 *
 */
public class Battery implements Electricity, Saver<JsonNode>, Loader<@Nullable JsonNode> {
	public double maxPower;
	public double capacity;
	public double amt;

	/**
	 * Create battery with capacity and power limits
	 * @param maxPower each unit is 50 watts
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

	@Override
	public void load(@Nullable JsonNode data) {
		if(data == null) return;
		maxPower = data.get(0).asDouble();
		capacity = data.get(1).asDouble();
		amt = data.get(2).asDouble();
	}

	@Override
	public JsonNode save() {
		return JsonTool.newArrayNode().add(maxPower).add(capacity).add(amt);
	}

	/**
	 * Removes
	 * @param elec
	 */
	public void extractTo(Electricity elec) {
		// TODO Auto-generated method stub
		
	}

}
