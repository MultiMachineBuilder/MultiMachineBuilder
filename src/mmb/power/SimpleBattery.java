package mmb.power;

import java.util.Objects;
import java.util.function.DoubleConsumer;

/**
 * A simple mutable energy storage with linearly scaled voltage.
 * <p>
 * Stored energy is measured in joules, while actual voltage scales linearly
 * from {@code 0 V} at empty to {@code voltRating()} at full charge.
 * <p>
 * If an overvoltage packet is inserted, the battery still accepts the energy
 * normally, but also emits an overvoltage event.
 */
public class SimpleBattery implements PowerHandler {
	private final double joulesPerVolt;
	private final double voltRating;
	private final double maxInsertPowerPerTick;
	private final double maxExtractPowerPerTick;
	private final DoubleConsumer overvoltageListener;

	private double storedJoules;

	/**
	 * Creates a new simple battery.
	 *
	 * @param joulesPerVolt storage capacity density in joules per volt
	 * @param voltRating maximum safe voltage in volts
	 * @param maxInsertPowerPerTick maximum insertion throughput in joules per tick
	 * @param maxExtractPowerPerTick maximum extraction throughput in joules per tick
	 * @param storedJoules initial stored energy in joules
	 * @param overvoltageListener listener invoked when an inserted packet exceeds
	 *        the voltage rating; receives the incoming voltage in volts
	 */
	public SimpleBattery(
			double joulesPerVolt,
			double voltRating,
			double maxInsertPowerPerTick,
			double maxExtractPowerPerTick,
			double storedJoules,
			DoubleConsumer overvoltageListener
	) {
		if (!(joulesPerVolt >= 0)) throw new IllegalArgumentException("joulesPerVolt must be >= 0");
		if (!(voltRating >= 0)) throw new IllegalArgumentException("voltRating must be >= 0");
		if (!(maxInsertPowerPerTick >= 0)) throw new IllegalArgumentException("maxInsertPowerPerTick must be >= 0");
		if (!(maxExtractPowerPerTick >= 0)) throw new IllegalArgumentException("maxExtractPowerPerTick must be >= 0");

		this.joulesPerVolt = joulesPerVolt;
		this.voltRating = voltRating;
		this.maxInsertPowerPerTick = maxInsertPowerPerTick;
		this.maxExtractPowerPerTick = maxExtractPowerPerTick;
		this.overvoltageListener = Objects.requireNonNull(overvoltageListener, "overvoltageListener");

		this.storedJoules = clamp(storedJoules, 0, capacityJoules());
	}

	/**
	 * Creates a new simple battery with no-op overvoltage handling.
	 *
	 * @param joulesPerVolt storage capacity density in joules per volt
	 * @param voltRating maximum safe voltage in volts
	 * @param maxInsertPowerPerTick maximum insertion throughput in joules per tick
	 * @param maxExtractPowerPerTick maximum extraction throughput in joules per tick
	 * @param storedJoules initial stored energy in joules
	 */
	public SimpleBattery(
			double joulesPerVolt,
			double voltRating,
			double maxInsertPowerPerTick,
			double maxExtractPowerPerTick,
			double storedJoules
	) {
		this(
				joulesPerVolt,
				voltRating,
				maxInsertPowerPerTick,
				maxExtractPowerPerTick,
				storedJoules,
				v -> {}
		);
	}

	@Override
	public double joulesPerVolt() {
		return joulesPerVolt;
	}

	@Override
	public double voltRating() {
		return voltRating;
	}

	@Override
	public double storedJoules() {
		return storedJoules;
	}

	@Override
	public double maxInsertPowerPerTick() {
		return maxInsertPowerPerTick;
	}

	@Override
	public double maxExtractPowerPerTick() {
		return maxExtractPowerPerTick;
	}

	@Override
	public double toInsert(PowerPacket pp) {
		Objects.requireNonNull(pp, "pp is null");
		if (!(pp.joules() > 0)) return 0;

		double free = capacityJoules() - storedJoules;
		if (!(free > 0)) return 0;

		return Math.min(pp.joules(), Math.min(maxInsertPowerPerTick, free));
	}

	@Override
	public double insert(PowerPacket pp) {
		Objects.requireNonNull(pp, "pp is null");
		if (!(pp.joules() > 0)) return 0;

		if (pp.volts() > voltRating) {
			overvoltageListener.accept(pp.volts());
		}

		double accepted = toInsert(pp);
		storedJoules += accepted;
		return accepted;
	}

	@Override
	public PowerPacket toExtract(double joules) {
		if (!(joules > 0)) return new PowerPacket(0, 0);

		double available = Math.min(joules, Math.min(maxExtractPowerPerTick, storedJoules));
		if (!(available > 0)) return new PowerPacket(0, 0);

		double start = actualVoltage();
		double end = voltageAfterExtraction(available);
		double avg = (start + end) * 0.5;

		return new PowerPacket(available, avg);
	}

	@Override
	public PowerPacket extract(double joules) {
		PowerPacket packet = toExtract(joules);
		storedJoules -= packet.joules();
		return packet;
	}

	/**
	 * Gets the current actual voltage based on current fill level.
	 *
	 * @return actual voltage in volts
	 */
	public double actualVoltage() {
		double cap = capacityJoules();
		if (!(cap > 0)) return 0;
		return voltRating * (storedJoules / cap);
	}

	/**
	 * Gets the total storage capacity of this battery.
	 *
	 * @return total capacity in joules
	 */
	public double capacityJoules() {
		return joulesPerVolt * voltRating;
	}

	private double voltageAfterExtraction(double extractedJoules) {
		double cap = capacityJoules();
		if (!(cap > 0)) return 0;
		double remaining = Math.max(0, storedJoules - extractedJoules);
		return voltRating * (remaining / cap);
	}

	private static double clamp(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}
}