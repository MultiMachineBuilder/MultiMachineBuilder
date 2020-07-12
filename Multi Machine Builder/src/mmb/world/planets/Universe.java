package mmb.world.planets;

import static mmb.data.contents.AuxiliaryConstants.*;

import mmb.data.contents.TreeNode;

public class Universe {
	static public TreeNode<Body> orbitalSystems;
	
	static {
		Body SgrAStar = new Body();
		SgrAStar.weight = solMass * 4100000;
		
		Body sun = new Body();
		sun.weight = solMass;
		sun.orbits(SgrAStar);
		
		Body earth = new Body();
		Orbit eo = earth.orbits(sun);
		
		
	}
}
