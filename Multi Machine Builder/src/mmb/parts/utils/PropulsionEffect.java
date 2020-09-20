/**
 * 
 */
package mmb.parts.utils;

import mmb.material.FuelRate;
import mmb.parts.Part;

/**
 * @author oskar
 *
 */
public class PropulsionEffect implements PartEffect {
	public FuelRate[] fuelRates;
	public double intakeRequirement;
	/**
	 * 
	 */
	public PropulsionEffect() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static PropulsionEffect SRB(NumericalCurve ISP) {
		
	}


	/* (non-Javadoc)
	 * @see mmb.parts.utils.PartEffect#apply(mmb.parts.utils.Part)
	 */
	@Override
	public void apply(Part p) {
		// TODO Auto-generated method stub
		
	}


	
	
}
