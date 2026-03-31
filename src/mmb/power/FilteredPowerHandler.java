package mmb.power;

import java.util.Objects;

/**
 * A power handler wrapper that limits insertion and extraction throughput.
 * <p>
 * This wrapper does not change the underlying handler's storage, voltage, or
 * actual energy behavior. It only restricts how much energy may be inserted
 * or extracted per tick through this view.
 */
public final class FilteredPowerHandler implements PowerHandler {
	private final PowerHandler delegate;
	private final double maxInsertPowerPerTick;
	private final double maxExtractPowerPerTick;

	private FilteredPowerHandler(
			PowerHandler delegate,
			double maxInsertPowerPerTick,
			double maxExtractPowerPerTick
	) {
		if (!(maxInsertPowerPerTick >= 0)) throw new IllegalArgumentException("maxInsertPowerPerTick must be >= 0");
		if (!(maxExtractPowerPerTick >= 0)) throw new IllegalArgumentException("maxExtractPowerPerTick must be >= 0");

		this.delegate = Objects.requireNonNull(delegate, "delegate");
		this.maxInsertPowerPerTick = maxInsertPowerPerTick;
		this.maxExtractPowerPerTick = maxExtractPowerPerTick;
	}

	/**
	 * Creates a filtered power handler view.
	 * <p>
	 * If the given handler is already a {@code FilteredPowerHandler}, this method
	 * returns a new wrapper around its original delegate with combined limits rather
	 * than nesting wrappers.
	 *
	 * @param handler underlying handler
	 * @param maxInsertPowerPerTick insertion limit in joules per tick
	 * @param maxExtractPowerPerTick extraction limit in joules per tick
	 * @return filtered handler view
	 */
	public static PowerHandler of(
			PowerHandler handler,
			double maxInsertPowerPerTick,
			double maxExtractPowerPerTick
	) {
		Objects.requireNonNull(handler, "handler");

		if (handler instanceof FilteredPowerHandler f) {
			return new FilteredPowerHandler(
					f.delegate,
					Math.min(f.maxInsertPowerPerTick, maxInsertPowerPerTick),
					Math.min(f.maxExtractPowerPerTick, maxExtractPowerPerTick)
			);
		}

		return new FilteredPowerHandler(handler, maxInsertPowerPerTick, maxExtractPowerPerTick);
	}

	@Override
	public double joulesPerVolt() {
		return delegate.joulesPerVolt();
	}

	@Override
	public double voltRating() {
		return delegate.voltRating();
	}

	@Override
	public double storedJoules() {
		return delegate.storedJoules();
	}

	@Override
	public double maxInsertPowerPerTick() {
		return Math.min(delegate.maxInsertPowerPerTick(), maxInsertPowerPerTick);
	}

	@Override
	public double maxExtractPowerPerTick() {
		return Math.min(delegate.maxExtractPowerPerTick(), maxExtractPowerPerTick);
	}

	@Override
	public double toInsert(PowerPacket pp) {
		if (pp == null) return 0;
		if (!(pp.joules() > 0)) return 0;

		double allowed = Math.min(pp.joules(), maxInsertPowerPerTick());
		return delegate.toInsert(new PowerPacket(allowed, pp.volts()));
	}

	@Override
	public double insert(PowerPacket pp) {
		if (pp == null) return 0;
		if (!(pp.joules() > 0)) return 0;

		double allowed = Math.min(pp.joules(), maxInsertPowerPerTick());
		return delegate.insert(new PowerPacket(allowed, pp.volts()));
	}

	@Override
	public PowerPacket toExtract(double joules) {
		double allowed = Math.min(Math.max(0, joules), maxExtractPowerPerTick());
		return delegate.toExtract(allowed);
	}

	@Override
	public PowerPacket extract(double joules) {
		double allowed = Math.min(Math.max(0, joules), maxExtractPowerPerTick());
		return delegate.extract(allowed);
	}
}