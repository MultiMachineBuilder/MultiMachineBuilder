package mmb.power;

/**
 * Represents a quantity of power that can be extracted, inserted or transported.
 * If insert
 * @param volts voltage in volts
 * @param joules energy in this packet in joules
 */
public record PowerPacket(double volts, double joules) {

}
