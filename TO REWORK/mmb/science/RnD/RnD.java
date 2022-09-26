/**
 * 
 */
package mmb.science.RnD;

import java.util.*;
/**
 * @author oskar
 *
 */
public class RnD {
	static public RnD start;
	
	//Rocketry
	static public RnD basicRocket = new RnD("Basic Rocketry"); 
	static public RnD earlyRocket = new RnD("Early Rocketry");
	static public RnD lightestRocket = new RnD("Lightest Rocketry");
	static public RnD vlightRocket = new RnD("Very Light Rocketry");
	static public RnD lighterRocket = new RnD("Lighter Rocketry");
	static public RnD lightRocket = new RnD("Light Rocketry");
	static public RnD midLLightRocket = new RnD("Medium Lighter Rocketry");
	static public RnD midLightRocket = new RnD("Medium Light Rocketry");
	static public RnD midRocket = new RnD("Medium Rocketry");
	static public RnD midHeavyRocket = new RnD("Medium Heavy Rocketry");
	static public RnD midHHeavyRocket = new RnD("Medium Heavier Rocketry");
	static public RnD heavyRocket = new RnD("Heavy Rocketry");
	static public RnD heavierRocket = new RnD("Heavier Rocketry");
	static public RnD vheavyRocket = new RnD("Very Heavy Rocketry");
	static public RnD heaviestRocket = new RnD("Heaviest Rocketry");
	static public RnD gigRocket = new RnD("Gigantic Rocketry");
	static public RnD colRocket = new RnD("Collosal Rocketry");
	static public RnD scolRocket = new RnD("Supercollosal Rocketry");
	
	//Fuel
	static public RnD basicFuels = new RnD("Basic Fuels");
	static public RnD regularFuels = new RnD("Regular Fuels");
	static public RnD betterFuels = new RnD("Better Fuels");
	static public RnD advFuels = new RnD("Advanced Fuels");
	static public RnD supFuels = new RnD("Superior Fuels");
	static public RnD xFuels = new RnD("Extreme Fuels");
	static public RnD sup2Fuels = new RnD("Suprextreme Fuels");
	
	//
	
	//Values
	protected List<RnD> requirements = new ArrayList<RnD>();
	protected String id = "";
	
	public RnD(String string) {
		id = string;
	}
	public boolean equals(RnD other) {
		return id.equals(other.id);
	}
	static {
		
	}
}
