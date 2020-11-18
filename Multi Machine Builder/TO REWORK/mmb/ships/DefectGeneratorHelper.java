/**
 * 
 */
package mmb.ships;

import mmb.ships.DefectGenerator.DefectKeys;

/**
 * @author oskar
 *
 */
public class DefectGeneratorHelper {
	DefectKeys rates;
	/**
	 * 
	 */
	public DefectGeneratorHelper() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Randomly generate boolean with same probability as order defect rate
	 */
	public boolean randomizeOrder() {
		return Math.random() < rates.defectRateOrder;
	}
	
	/**
	 * Randomly generate boolean with same probability as lot defect rate
	 */
	public boolean randomizeLot() {
		return Math.random() < rates.defectRateLot;
	}
	
	/**
	 * Randomly generate boolean with same probability as unit defect rate
	 */
	public boolean randomizeUnit() {
		return Math.random() < rates.defectRateUnit;
	}

}
