package mmb.world.vessels.linkages;

import java.util.ArrayList;

import mmb.data.contents.TreeNode;
import mmb.debug.Debugger;
import mmb.world.parts.Part;
import mmb.world.vessels.MechanicalLinkage;
import mmb.world.vessels.VesselFreeform;

public class Jettisoner {

	public Jettisoner() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Jettison a linkage
	 * @param link
	 */
	public static void jettison(MechanicalLinkage link) {
		Debugger.printl("Jettisoning MechanicalLinkage " + link.hashCode());
		if(link.A.mechanicalLinkages.remove(link)) {
			Debugger.printl("Succesfully deleted unnecessary jettisoned link from " + link.A.name());
		}else {
			Debugger.printl("Failed to delete unnecessary jettisoned link from " + link.A.name());
		}
		if(link.B.mechanicalLinkages.remove(link)) {
			Debugger.printl("Succesfully deleted unnecessary jettisoned link from " + link.B.name());
		}else {
			Debugger.printl("Failed to delete unnecessary jettisoned link from " + link.B.name());
		}
		link.jettisoned = true;
	}
	/**
	 * Jettison a part, disconnecting it from its parent
	 * @param p
	 */
	public static void jettison(Part p) {
		Debugger.printl("Jettisoning "+ p.name());
		TreeNode<Part> tree = p.myNode;
		TreeNode<Part> parent = tree.getParent();
		if(parent == null) {
			return;
		}
		tree.removeFrom();
		ArrayList<MechanicalLinkage> links = p.mechanicalLinkages;
		all:{
			for(int i = 0; i < links.size(); i++) {
				MechanicalLinkage ml = links.get(i);
				if(ml.contains(p)) {
					if(ml.A == p) {
						if(!(ml.B.myNode.root().value == p)) tree.addTo(ml.B.myNode); //Regular adding would create connection loop
					}else {
						if(!(ml.A.myNode.root().value == p)) tree.addTo(ml.A.myNode); //Regular adding would create connection loop
					}
				break all;
				}
			}
			VesselFreeform.spawn(new VesselFreeform(tree));
		}	
	}
}
