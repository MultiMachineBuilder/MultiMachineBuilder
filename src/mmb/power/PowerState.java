package mmb.power;

import mmb.engine.Verify;

/**
 * Represents a state of a {@link PowerHandler} at an instant. 
 * @param maxVoltage voltage rating of this power state
 * @param energy energy stored in this power state
 * @param joulesPerVolt how much energy is stored per volt
 * @param maxInsertPerTick maximum insertion rate
 * @param maxExtractPerTick maximum extraction rate
 */
public record PowerState(double maxVoltage, double energy, double joulesPerVolt, double maxInsertPerTick, double maxExtractPerTick) {
	/**
	 * Constructs a new PowerState
	 * @param maxVoltage voltage rating of this power state
	 * @param energy energy stored in this power state
	 * @param joulesPerVolt how much energy is stored per volt
	 * @param maxInsertPerTick maximum insertion rate
	 * @param maxExtractPerTick maximum extraction rate
	 * @throws IllegalArgumentException when maxVoltage <= 0
	 * @throws IllegalArgumentException when energy < 0
	 * @throws IllegalArgumentException when joulesPerVolt <= 0
	 * @throws IllegalArgumentException when maxExtractPerTick < 0
	 * @throws IllegalArgumentException when maxInsertPerTick < 0
	 */
	public PowerState{
		Verify.requirePositive(maxVoltage);
		Verify.requireNonNegative(energy);
		Verify.requirePositive(joulesPerVolt);
		Verify.requireNonNegative(maxInsertPerTick);
		Verify.requireNonNegative(maxExtractPerTick);
	}
	
	/**
	 * Dumps the contents of a power handler into a {@link PowerState}
	 * @param ph source power handler
	 * @return contents of a given power handler
	 * @throws IllegalArgumentException when ph.voltRating() <= 0
	 * @throws IllegalArgumentException when ph.storedJoules() < 0
	 * @throws IllegalArgumentException when ph.joulesPerVolt() <= 0
	 * @throws IllegalArgumentException when ph.maxInsertPowerPerTick() < 0
	 * @throws IllegalArgumentException when ph.maxExtractPowerPerTick() < 0
	 */
	public static PowerState dump(PowerHandler ph) {
		double maxVoltage = ph.voltRating();
		double energy = ph.storedJoules();
		double joulesPerVolt = ph.joulesPerVolt();
		double maxInsertPerTick = ph.maxInsertPowerPerTick();
		double maxExtractPerTick = ph.maxExtractPowerPerTick();
		return new PowerState(maxVoltage, energy, joulesPerVolt, maxInsertPerTick, maxExtractPerTick);
	}
	
	/**
	 * Calculates max storable energy for given power state
	 * @return maximum energy capacity of this power state in joules
	 */
	public double maxJoules() {
		return maxVoltage * joulesPerVolt;
	}
	/**
	 * Calculates actual voltage of given power state
	 * @return actual voltage of this power state in joules
	 */
	public double actualVolts() {
		return energy / joulesPerVolt;
	}
	/**
	 * Calculates how much out of full capacity the given power state is filled
	 * @return ratio of stored energy to maximum energy
	 */
	public double fillRate() {
		return energy / maxJoules();
	}
}
