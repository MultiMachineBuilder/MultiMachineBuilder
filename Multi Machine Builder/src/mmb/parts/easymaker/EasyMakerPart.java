/**
 * 
 */
package mmb.parts.easymaker;

import mmb.debug.Debugger;

/**
 * @author oskar
 * 
 * List of 
 */
public class EasyMakerPart {
	public String id = "unknown";
	public double elec = -1;
	/**
	 * 
	 */
	public EasyMakerPart(String iid) {
		id = iid;
	}
	
	/**
	 * Add given amount of energy storage
	 * @param amt
	 */
	public void addElectricity(double amt) {
		elec = amt;
	}
	
	
	/**
	 * compile this EasyMaker
	 * @return
	 */
	public PartSpecProto compile() {
		PartSpecProto result = new PartSpecProto();
		//Electricity
		if(elec > 0) {
			Debugger.printl("("+id+") Creating electricity buffer of " + elec + " joules");
		}
		return result;
	}

}
