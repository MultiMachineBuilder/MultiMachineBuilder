/**
 * 
 */
package mmb.DATA.contents;

import mmb.material.Fuel;
import static e3d.MathPlus.*;

/**
 * @author oskar
 *
 */
public class UnmoddedGameContent {
	public void unmodded() {
		
	}
	
	void rockets() {
		
	}
	
	void tanks() {
		//Premanufactured tanks
		
	}
	
	
	private void materials() {
		Fuel gasoline = new Fuel();
		
		gasoline.criticalTemperature = degC(280);
		gasoline.criticalPressure = 4500000;
		
		Fuel diesel = new Fuel();
		
	}
	
	/**
	 * Pressure strength of tanks:
	 * XHP : 1000 bar
	 * HHP : 200 bar
	 * UHP : 100 bar
	 * SHP : 50 bar
	 *  HP : 10 bar
	 *  MP :  5 bar
	 *  LP :  2 bar
	 *  UP :  0.1 bar
	 */
	/**
	 * Each tank material has strength coefficient (strength/density)
	 * Graphene   25'200'000
	 * Carbon Composite 666'666 2/3
	 * Glass Composite 250'000
	 * SuperAlloy 100'000
	 * Stainless 50'000
	 * Aluminum 35'000
	 * Hardened 25'000
	 * Steel 10'000
	 * Iron 5'000
	 * Copper 2'500
	 * Tin 1'500
	 * Lead 1'000
	 * Calculate shell thickness : sqrt(size coefficient) * pressure / strength coefficient
	 * 1*1m wide cylinder 1 MPa stainless steel tank : 1MPa / 100k = 10 mm
	 * inner cylinder : 980mm * 980mm = 739.210 l
	 * outer cylinder : 1m * 1m = 785.398l
	 * difference : 785.398l - 739.210 l = 56.188 l
	 * LPG mass : 739.210 l * 0.5 kg/l = 369.605 kg
	 * wet mass : 425.793 kg
	 * mass ratio : 7,5780059
	 * 
	 * Same pressure, but with graphene
	 * 
	 */
	/**
	 * Strength coefficient | volume of cell shapes:
	 * Zero thick plane : 0
	 * Cuboid (infinite rod-like) : 2/3
	 * Cube : 1
	 * Cylinder (near cubic) : 2
	 * Cyliner (infinite rod-like) : 1 1/3
	 * Sphere : 3
	 */
	/**
	 * Create tank materials
	 *
	 */
	private void createTanks() {
		
	}
}
