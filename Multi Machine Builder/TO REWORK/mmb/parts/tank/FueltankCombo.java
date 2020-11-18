/**
 * 
 */
package mmb.parts.tank;

import java.util.Hashtable;

import mmb.material.types.Fluid;
import mmb.science.RnD.RnD;

/**
 * @author oskar
 *
 */
public class FueltankCombo {
	public Hashtable<Fluid, Double> contents = new Hashtable<Fluid, Double>();
	public RnD require = RnD.start;
	/**
	 * 
	 */
	public FueltankCombo() {
		// TODO Auto-generated constructor stub
	}
	
	public FueltankCombo add(Fluid f, double amt) {
		contents.put(f, amt);
		return this;
	}
	public FueltankCombo requireRnD(RnD req) {
		require = req;
		return this;
	}

}
