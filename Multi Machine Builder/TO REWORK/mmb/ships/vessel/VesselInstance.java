/**
 * 
 */
package mmb.ships.vessel;

import e3d.d3.Vector3;

/**
 * @author oskar
 *
 */
public class VesselInstance {
	
	/**
		 * @author oskar
		 *
		 */
	public enum VesselStatus {
		//All newly created vessels are virtual
		VIRTUAL, //No rendering, movement, position and operation
		HANGAR, //No rendering, movement and operation
		DEAD, //No operation
		ORBIT, //Orbital simulation
		ATMO, //Hybrid simulation (orbital+atmospheric)
		PARKED, //No movement (used on ground)
		PARKED_MPARTS, //Parked vessels with moving parts
		DOCKED, //No movement (used for vessel parts)
		DOCKED_MPARTS, //Docked vessels with moving parts
	}

	private boolean stasis = true;
	private boolean locked = true;
	
	public Vector3 pos = null;
	/**
	 * 
	 */
	public VesselInstance() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Hangar the vessel, stopping all physics.
	 * @param hangar
	 */
	public void moveTo(Hangar hangar) {
		
	}
	
	/**
	 * Handle physics
	 */
	protected void perFrameHandle() {
		
	}
	
	

}
