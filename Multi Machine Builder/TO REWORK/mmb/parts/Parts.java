/**
 * 
 */
package mmb.parts;
import java.util.*;

import mmb.world2.vessels.*;

/**
 * @author oskar
 *	Tools for parts
 */
public class Parts {
	static public Hashtable<Part, VesselFreeform> correspondingVessels = new Hashtable<Part, VesselFreeform>();
	static public Hashtable<Part, Assembly> correspondingAssemblies = new Hashtable<Part, Assembly>();
	static public Hashtable<String, PartSpec> specs = new Hashtable<String, PartSpec>();
	
	
	//Part categories
	static public PartCategory SRB;
	static public PartCategory NERVA;
	static public PartCategory jets;
	static public PartCategory rockets;
	static public PartCategory fuelTank;
	static public PartCategory parachute;
	static public PartCategory decoupler;
	static public PartCategory dock;
	static public PartCategory SAS;
	static public PartCategory cockpit;
	static public PartCategory strut;
	static public PartCategory fairing;
	static public PartCategory lab;
	static public PartCategory RCS;
}
