/**
 * 
 */
package mmb.world2;

import e3d.d3.Vector3;
import mmb.control.ControlPoint;
import mmb.parts.Part;
import mmb.world2.vessels.*;

/**
 * @author oskar
 *
 */
public class Circumstances {
	public VesselFreeform v;
	public Assembly a;
	public Part p;
	
	public double density, pressure, altitude, airspeed;
	
	public ControlPoint cp;
	
	public Vector3 wind;
	
}
