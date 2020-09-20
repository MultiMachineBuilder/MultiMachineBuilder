/**
 * 
 */
package mmb.ships.vessel;

import java.util.List;

import mmb.ships.Defects;

/**
 * @author oskar
 *
 */
public class VesselOrder {
	public Defects defects;
	public List<VesselLot> lots;
	/**
	 * 
	 */
	public VesselOrder() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 */
	public void assign() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * @param lot
	 */
	public void add(VesselLot lot) {
		lots.add(lot);
	}

}
