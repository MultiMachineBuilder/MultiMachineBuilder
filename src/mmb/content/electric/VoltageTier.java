/**
 * 
 */
package mmb.content.electric;

import java.awt.Color;
import java.util.List;
import java.util.function.Supplier;

import mmb.NN;
import mmb.content.rawmats.Materials;
import mmb.content.rawmats.MetalGroup;

/**
 * @author oskar
 * V1: rudimentary
 * V2: copper
 * V3: silver
 * V4: gold
 * V5: platinum
 * V6: iridium
 * V7: crystalline
 * V8: stellarine
 * V9: graphene
 */
public enum VoltageTier {
	V1(    100, "ULV", () -> Materials.rudimentary, () -> Materials.rudimentary,     2, new Color(255,   0,   0)),
	V2(    400, "VLV", () -> Materials.iron,        () -> Materials.copper,          4, new Color(255, 128,   0)), 
	V3(   1600,  "LV", () -> Materials.steel,       () -> Materials.silver,          8, new Color(255, 255,   0)),
	V4(   6400,  "MV", () -> Materials.stainless,   () -> Materials.gold,           16, new Color(128, 255,   0)),
	V5(  25600,  "HV", () -> Materials.titanium,    () -> Materials.platinum,       64, new Color(  0, 255, 128)),
	V6( 102400,  "EV", () -> Materials.signalum,    () -> Materials.iridium,       256, new Color(  0, 255, 255)),
	V7( 409600,  "IV", () -> Materials.enderium,    () -> Materials.crystal,      1024, new Color(  0, 200, 255)),
	V8(1638400, "LuV", () -> Materials.duranium,    () -> Materials.stellar,      4096, new Color(  0, 128, 255)),
	V9(6553600, "MAX", () -> Materials.unobtainium, () -> Materials.unobtainium, 16384, new Color(  0,   0, 255));
	
	/** Voltage in volts */
	public final double volts;
	@NN public final String name;
	/** The construction material */
	@NN public final Supplier<MetalGroup> construction;
	/** The electrical material */
	@NN public final Supplier<MetalGroup> electrical;
	public final int speedMul;
	/** Color of machines and conduits for this voltage */
	@NN public final Color c;
	/** List of all voltages */
	@NN public static final List<@NN VoltageTier> VOLTS = List.of(VoltageTier.values());
	/** @return maximum voltage now supported. Subject to change. */
	public static VoltageTier maxVoltage() { 
		return V9;
	}
	
	/**
	 * @param current current or charge
	 * @return the power or energy at given current or charge respectively, and this voltage
	 */
	public double power(double current) {
		return volts*current;
	}
	
	VoltageTier(double volts, String name, Supplier<MetalGroup> construction, Supplier<MetalGroup> electrical, int speedMul, Color c) {
		this.volts = volts;
		this.name = name;
		this.construction = construction;
		this.electrical = electrical;
		this.speedMul = speedMul;
		this.c = c;
	}
}
