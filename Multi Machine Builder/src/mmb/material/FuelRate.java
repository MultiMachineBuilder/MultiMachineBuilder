/**
 * 
 */
package mmb.material;

import mmb.material.types.Fluid;

/**
 * @author oskar
 *
 */
public class FuelRate {
	public Fluid material;
	public double rate; //units/s
	
	/**
	 * @param material
	 * @param rate
	 */
	public FuelRate(Fluid material, double rate) {
		super();
		this.material = material;
		this.rate = rate;
	}
	
	/**
	 * 
	 */
	public FuelRate() {
		// TODO Auto-generated constructor stub
	}

}
