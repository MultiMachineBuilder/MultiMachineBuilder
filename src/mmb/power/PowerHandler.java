package mmb.power;

/**
 * Represents a passive electrical endpoint that can store, accept, and/or provide energy.
 * <p>
 * A {@code PowerHandler} does <b>not</b> actively move energy by itself. Actual transfer is
 * expected to be orchestrated externally by power conduits, cables, networks, or other
 * transport logic. Machines expose their electrical state and transfer capabilities through
 * this interface, while conduits decide when and where energy is moved.
 * <p>
 * The electrical model used by this interface is based on:
 * <ul>
 *   <li><b>Stored energy</b> measured in <b>joules (J)</b></li>
 *   <li><b>Transfer throughput</b> measured in <b>joules per tick (J/t)</b></li>
 *   <li><b>Voltage</b> measured in <b>volts (V)</b></li>
 * </ul>
 * <p>
 * Implementations are expected to derive their current electrical state from the amount of
 * stored energy. In the standard model, actual voltage scales linearly with fill level:
 * <pre>
 * capacityJoules = joulesPerVolt() * voltRating()
 * fillRatio      = storedJoules() / capacityJoules
 * actualVoltage  = voltRating() * fillRatio
 * </pre>
 * <p>
 * This interface distinguishes between:
 * <ul>
 *   <li><b>Simulation methods</b> ({@link #toInsert(PowerPacket)} and {@link #toExtract(double)})
 *       which report what <i>would</i> happen without changing state</li>
 *   <li><b>Execution methods</b> ({@link #insert(PowerPacket)} and {@link #extract(double)})
 *       which actually modify stored energy</li>
 * </ul>
 * <p>
 * Implementations are responsible for handling unsafe input themselves. In particular, if
 * an incoming packet exceeds the safe voltage of the machine, the machine may reject the
 * transfer, fail, explode, or otherwise apply its own overvoltage behavior.
 * <p>
 * Unless otherwise documented by an implementation, all returned values are expected to be
 * finite and non-negative.
 */
public interface PowerHandler {
	/**
	 * Gets the amount of energy this handler can store per volt of rated capacity.
	 * <p>
	 * This value determines the total storage capacity together with {@link #voltRating()}:
	 * <pre>
	 * capacityJoules = joulesPerVolt() * voltRating()
	 * </pre>
	 * <p>
	 * Larger values represent greater energy storage at the same voltage rating.
	 *
	 * @return storage capacity density in joules per volt (J/V)
	 */
	public double joulesPerVolt();

	/**
	 * Gets the maximum safe operating voltage of this handler.
	 * <p>
	 * This is the highest voltage the machine is designed to tolerate safely. Incoming
	 * packets above this voltage are considered overvoltage and should be handled by the
	 * implementation, typically by rejecting the transfer or triggering machine failure.
	 * <p>
	 * This value is also used together with {@link #joulesPerVolt()} and
	 * {@link #storedJoules()} to derive the handler's current fill state and actual voltage.
	 *
	 * @return maximum safe voltage in volts (V)
	 */
	public double voltRating();

	/**
	 * Gets the amount of energy currently stored in this handler.
	 * <p>
	 * This value represents the handler's present electrical charge state and is used to
	 * derive fill percentage and actual voltage under the standard linear-voltage model.
	 * <p>
	 * Implementations should generally keep this value within the range:
	 * <pre>
	 * 0 <= storedJoules() <= joulesPerVolt() * voltRating()
	 * </pre>
	 *
	 * @return stored energy in joules (J)
	 */
	public double storedJoules();

	/**
	 * Gets the maximum amount of energy that can be inserted into this handler during one tick.
	 * <p>
	 * This is a throughput limit, not a storage limit. Even if the handler has enough free
	 * capacity, insertion should not exceed this amount during a single simulation step.
	 * <p>
	 * This limit is typically enforced by {@link #toInsert(PowerPacket)} and
	 * {@link #insert(PowerPacket)}.
	 *
	 * @return maximum insertion throughput in joules per tick (J/t)
	 */
	public double maxInsertPowerPerTick();

	/**
	 * Gets the maximum amount of energy that can be extracted from this handler during one tick.
	 * <p>
	 * This is a throughput limit, not a storage limit. Even if the handler contains enough
	 * energy, extraction should not exceed this amount during a single simulation step.
	 * <p>
	 * This limit is typically enforced by {@link #toExtract(double)} and
	 * {@link #extract(double)}.
	 *
	 * @return maximum extraction throughput in joules per tick (J/t)
	 */
	public double maxExtractPowerPerTick();

	/**
	 * Inserts energy from the given packet into this handler.
	 * <p>
	 * This is the <b>executing</b> form of insertion and is expected to modify the internal
	 * stored energy of the handler.
	 * <p>
	 * The packet specifies:
	 * <ul>
	 *   <li>the amount of energy being offered, in joules</li>
	 *   <li>the voltage at which that energy is being delivered</li>
	 * </ul>
	 * <p>
	 * The implementation may accept only part of the packet due to:
	 * <ul>
	 *   <li>insufficient free storage capacity</li>
	 *   <li>insertion throughput limits</li>
	 *   <li>voltage incompatibility</li>
	 *   <li>machine-specific safety or behavior rules</li>
	 * </ul>
	 * <p>
	 * If the packet voltage exceeds {@link #voltRating()}, the implementation is responsible
	 * for applying its own overvoltage behavior. This may include rejecting the transfer,
	 * damaging the machine, or causing catastrophic failure.
	 *
	 * @param pp incoming power packet to insert
	 * @return amount of energy actually accepted, in joules (J)
	 */
	public double insert(PowerPacket pp);

	/**
	 * Simulates how much energy from the given packet would be accepted by this handler.
	 * <p>
	 * This is the <b>non-mutating</b> form of insertion and must not change internal state.
	 * It is intended for planning and routing by conduits or networks before committing an
	 * actual transfer.
	 * <p>
	 * The returned value should match the amount that would be accepted by
	 * {@link #insert(PowerPacket)} under the same current conditions, assuming no intervening
	 * state changes occur.
	 *
	 * @param pp incoming power packet to test
	 * @return amount of energy that would be accepted, in joules (J)
	 */
	public double toInsert(PowerPacket pp);

	/**
	 * Simulates extraction of energy from this handler.
	 * <p>
	 * This is the <b>non-mutating</b> form of extraction and must not change internal state.
	 * It is intended for planning and routing by conduits or networks before committing an
	 * actual transfer.
	 * <p>
	 * The returned packet describes the energy that would currently be available for extraction,
	 * limited by:
	 * <ul>
	 *   <li>the requested energy amount</li>
	 *   <li>available stored energy</li>
	 *   <li>extraction throughput limits</li>
	 *   <li>the handler's current actual voltage</li>
	 * </ul>
	 * <p>
	 * The packet voltage should represent the effective or average voltage of the extracted
	 * energy under the implementation's model.
	 *
	 * @param joules maximum amount of energy to test for extraction, in joules (J)
	 * @return power packet that would be extracted
	 */
	public PowerPacket toExtract(double joules);

	/**
	 * Extracts energy from this handler.
	 * <p>
	 * This is the <b>executing</b> form of extraction and is expected to modify the internal
	 * stored energy of the handler.
	 * <p>
	 * The returned packet describes the actual energy removed and the voltage at which it was
	 * supplied. The amount extracted may be lower than requested due to:
	 * <ul>
	 *   <li>insufficient stored energy</li>
	 *   <li>extraction throughput limits</li>
	 *   <li>machine-specific behavior or restrictions</li>
	 * </ul>
	 * <p>
	 * The packet voltage should represent the effective or average voltage of the extracted
	 * energy under the implementation's model.
	 *
	 * @param joules maximum amount of energy to extract, in joules (J)
	 * @return power packet actually extracted
	 */
	public PowerPacket extract(double joules);
}