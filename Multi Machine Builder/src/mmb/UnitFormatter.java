/**
 * 
 */
package mmb;

/**
 * @author oskar
 * A class constaining formatting utilities
 */
public class UnitFormatter {
	private UnitFormatter() {}
	public static String formatEnergy(double energy) {
		if(energy >= 10e24)
			//Yottajoules
			return (energy/1e24)+" YJ";
		if(energy >= 10e21)
			//Zettajoules
			return (energy/1e21)+" ZJ";
		if(energy >= 10e18)
			//Exajoules
			return (energy/1e18)+" EJ";
		if(energy >= 10e15)
			//Petajoules
			return (energy/1e15)+" PJ";
		if(energy >= 10e12)
			//Teraajoules
			return (energy/1e12)+" TJ";
		if(energy >= 10e9)
			//Gigajoules
			return (energy/1e9)+" GJ";
		if(energy >= 10e6)
			//Megajoules
			return (energy/1e6)+" MJ";
		if(energy >= 10000)
			//Kilojoules
			return (energy/1000)+" kJ";
		if(energy > 10)
			//Joules
			return energy+" J";
		if(energy > 0.01)
			//Milijoules
			return (energy*1000)+" mJ";
		if(energy > 10e-6)
			//Microjoules
			return (energy*1e6)+" μJ";
		if(energy > 10e-9)
			//Nanojoules
			return (energy*1e9)+"nJ";
		if(energy > 10e-12)
			//Picojoules
			return (energy*1e12)+" pJ";
		if(energy > 10e-15)
			//Femtojoules
			return (energy*1e15)+" fJ";
		if(energy > 10e-18)
			//Attojoules
			return (energy*1e18)+" aJ";
		if(energy > 10e-21)
			//Zoctojoules
			return (energy*1e21)+" zJ";
		//Yoctojoules
		return (energy*1e24)+" yJ";
	}
}
