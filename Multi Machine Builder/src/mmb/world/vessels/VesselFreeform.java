package mmb.world.vessels;

import java.util.ArrayList;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import mmb.files.data.contents.TreeNode;
import mmb.parts.Part;

public class VesselFreeform {
	//Static
	static ArrayList<VesselFreeform> vesselsinExistence;
		
	//Dynamic
	public TreeNode<Part> tree;
	
	/**
	 * VesselFreeform from part tree
	 */
	public VesselFreeform(TreeNode<Part> r) {
		tree = r;
	}
	//Static
	
	public static void spawn(VesselFreeform vessel) {
		
	}
	public static VesselFreeform find(Part p) {
		int l = vesselsinExistence.size();
		for(int i = 0; i < l; i++) {
			VesselFreeform v = vesselsinExistence.get(i);
			if(v.tree.value == p) {
				return v;
			}
		}
		return null;
	}

}
