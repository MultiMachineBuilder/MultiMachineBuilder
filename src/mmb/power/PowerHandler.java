package mmb.power;

/**
 * Defines an interface to a power machine. 
 */
public interface PowerHandler {
	/**
	 * Gets the energy capacity per volt
	 * @return joules per volt
	 */
	public double joulesPerVolt();
	/**
	 * Gets the maximum allowed voltage for this power handler.
	 * When the actual voltage exceeds its rating, the connected machine will explode.
	 * @return voltage rating of this power handler in volts
	 */
	public double voltRating();
	/**
	 * Gets the currently stored energy in joules.
	 * It is used to derive fill % and current voltage.
	 * @return energy stored in joules
	 */
	public double storedJoules();
	/**
	 * Gets the maximum power that can be inserted into this machine.
	 * @return maximum power in joules per tick
	 */
	public double maxInsertPowerPerTick();
	/**
	 * Gets the maximum power that can be inserted into this machine.
	 * @return maximum power in joules per tick
	 */
	public double maxExtractPowerPerTick();
	
	/**
	 * Inserts energy from the packet up to its voltage rating.
	 * If overvoltaged, the machine explodes
	 * @param pp desired power transfer with max voltage and energy.
	 * @return energy inserted in joules
	 */
	public double insert(PowerPacket pp);
	/**
	 * Checks how much energy machine will accept
	 * @param pp desired power transfer with max voltage and energy.
	 * @return energy to be inserted in joules
	 */
	public double toInsert(PowerPacket pp);
	/**
	 * Extracts energy from the machine
	 * @param joules energy to extract
	 * @return average voltage and total energy to be extracted
	 */
	public PowerPacket toExtract(double joules);
	/**
	 * Extracts energy from the machine
	 * @param joules energy to extract
	 * @return average voltage and total energy of extraction
	 */
	public PowerPacket extract(double joules);
}
