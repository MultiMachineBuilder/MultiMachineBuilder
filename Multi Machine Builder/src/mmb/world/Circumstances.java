/**
 * 
 */
package mmb.world;

import e3d.d3.Vector3;
import mmb.control.ControlPoint;
import mmb.world.parts.Part;
import mmb.world.vessels.*;

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
